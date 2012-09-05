/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectorShell.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.logging.Logger;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.PrivateElementsImpl;
import cn.com.rebirth.core.inject.internal.ProviderInstanceBindingImpl;
import cn.com.rebirth.core.inject.internal.Scoping;
import cn.com.rebirth.core.inject.internal.SourceProvider;
import cn.com.rebirth.core.inject.internal.Stopwatch;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.Elements;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.PrivateElements;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * The Class InjectorShell.
 *
 * @author l.xue.nong
 */
class InjectorShell {

	
	/** The elements. */
	private final List<Element> elements;

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The private elements. */
	private final PrivateElements privateElements;

	
	/**
	 * Instantiates a new injector shell.
	 *
	 * @param builder the builder
	 * @param elements the elements
	 * @param injector the injector
	 */
	private InjectorShell(Builder builder, List<Element> elements, InjectorImpl injector) {
		this.privateElements = builder.privateElements;
		this.elements = elements;
		this.injector = injector;
	}

	
	/**
	 * Gets the private elements.
	 *
	 * @return the private elements
	 */
	PrivateElements getPrivateElements() {
		return privateElements;
	}

	
	/**
	 * Gets the injector.
	 *
	 * @return the injector
	 */
	InjectorImpl getInjector() {
		return injector;
	}

	
	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	List<Element> getElements() {
		return elements;
	}

	
	/**
	 * The Class Builder.
	 *
	 * @author l.xue.nong
	 */
	static class Builder {

		
		/** The elements. */
		private final List<Element> elements = Lists.newArrayList();

		
		/** The modules. */
		private final List<Module> modules = Lists.newArrayList();

		
		/** The state. */
		private State state;

		
		/** The parent. */
		private InjectorImpl parent;

		
		/** The stage. */
		private Stage stage;

		
		/** The private elements. */
		private PrivateElementsImpl privateElements;

		
		/**
		 * Parent.
		 *
		 * @param parent the parent
		 * @return the builder
		 */
		Builder parent(InjectorImpl parent) {
			this.parent = parent;
			this.state = new InheritingState(parent.state);
			return this;
		}

		
		/**
		 * Stage.
		 *
		 * @param stage the stage
		 * @return the builder
		 */
		Builder stage(Stage stage) {
			this.stage = stage;
			return this;
		}

		
		/**
		 * Private elements.
		 *
		 * @param privateElements the private elements
		 * @return the builder
		 */
		Builder privateElements(PrivateElements privateElements) {
			this.privateElements = (PrivateElementsImpl) privateElements;
			this.elements.addAll(privateElements.getElements());
			return this;
		}

		
		/**
		 * Adds the modules.
		 *
		 * @param modules the modules
		 */
		void addModules(Iterable<? extends Module> modules) {
			for (Module module : modules) {
				this.modules.add(module);
			}
		}

		
		/**
		 * Lock.
		 *
		 * @return the object
		 */
		Object lock() {
			return getState().lock();
		}

		
		/**
		 * Builds the.
		 *
		 * @param initializer the initializer
		 * @param bindingProcessor the binding processor
		 * @param stopwatch the stopwatch
		 * @param errors the errors
		 * @return the list
		 */
		List<InjectorShell> build(Initializer initializer, BindingProcessor bindingProcessor, Stopwatch stopwatch,
				Errors errors) {
			checkState(stage != null, "Stage not initialized");
			checkState(privateElements == null || parent != null, "PrivateElements with no parent");
			checkState(state != null, "no state. Did you remember to lock() ?");

			InjectorImpl injector = new InjectorImpl(parent, state, initializer);
			if (privateElements != null) {
				privateElements.initInjector(injector);
			}

			
			if (parent == null) {
				modules.add(0, new RootModule(stage));
				new TypeConverterBindingProcessor(errors).prepareBuiltInConverters(injector);
			}

			elements.addAll(Elements.getElements(stage, modules));
			stopwatch.resetAndLog("Module execution");

			new MessageProcessor(errors).process(injector, elements);

			new TypeListenerBindingProcessor(errors).process(injector, elements);
			List<TypeListenerBinding> listenerBindings = injector.state.getTypeListenerBindings();
			injector.membersInjectorStore = new MembersInjectorStore(injector, listenerBindings);
			stopwatch.resetAndLog("TypeListeners creation");

			new ScopeBindingProcessor(errors).process(injector, elements);
			stopwatch.resetAndLog("Scopes creation");

			new TypeConverterBindingProcessor(errors).process(injector, elements);
			stopwatch.resetAndLog("Converters creation");

			bindInjector(injector);
			bindLogger(injector);
			bindingProcessor.process(injector, elements);
			stopwatch.resetAndLog("Binding creation");

			List<InjectorShell> injectorShells = Lists.newArrayList();
			injectorShells.add(new InjectorShell(this, elements, injector));

			
			PrivateElementProcessor processor = new PrivateElementProcessor(errors, stage);
			processor.process(injector, elements);
			for (Builder builder : processor.getInjectorShellBuilders()) {
				injectorShells.addAll(builder.build(initializer, bindingProcessor, stopwatch, errors));
			}
			stopwatch.resetAndLog("Private environment creation");

			return injectorShells;
		}

		
		/**
		 * Gets the state.
		 *
		 * @return the state
		 */
		private State getState() {
			if (state == null) {
				state = new InheritingState(State.NONE);
			}
			return state;
		}
	}

	
	/**
	 * Bind injector.
	 *
	 * @param injector the injector
	 */
	private static void bindInjector(InjectorImpl injector) {
		Key<Injector> key = Key.get(Injector.class);
		InjectorFactory injectorFactory = new InjectorFactory(injector);
		injector.state.putBinding(key,
				new ProviderInstanceBindingImpl<Injector>(injector, key, SourceProvider.UNKNOWN_SOURCE,
						injectorFactory, Scoping.UNSCOPED, injectorFactory, ImmutableSet.<InjectionPoint> of()));
	}

	
	/**
	 * A factory for creating Injector objects.
	 */
	private static class InjectorFactory implements InternalFactory<Injector>, Provider<Injector> {

		
		/** The injector. */
		private final Injector injector;

		
		/**
		 * Instantiates a new injector factory.
		 *
		 * @param injector the injector
		 */
		private InjectorFactory(Injector injector) {
			this.injector = injector;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
		 */
		public Injector get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException {
			return injector;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Provider#get()
		 */
		public Injector get() {
			return injector;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "Provider<Injector>";
		}
	}

	
	/**
	 * Bind logger.
	 *
	 * @param injector the injector
	 */
	private static void bindLogger(InjectorImpl injector) {
		Key<Logger> key = Key.get(Logger.class);
		LoggerFactory loggerFactory = new LoggerFactory();
		injector.state.putBinding(key,
				new ProviderInstanceBindingImpl<Logger>(injector, key, SourceProvider.UNKNOWN_SOURCE, loggerFactory,
						Scoping.UNSCOPED, loggerFactory, ImmutableSet.<InjectionPoint> of()));
	}

	
	/**
	 * A factory for creating Logger objects.
	 */
	private static class LoggerFactory implements InternalFactory<Logger>, Provider<Logger> {

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
		 */
		public Logger get(Errors errors, InternalContext context, Dependency<?> dependency) {
			InjectionPoint injectionPoint = dependency.getInjectionPoint();
			return injectionPoint == null ? Logger.getAnonymousLogger() : Logger.getLogger(injectionPoint.getMember()
					.getDeclaringClass().getName());
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Provider#get()
		 */
		public Logger get() {
			return Logger.getAnonymousLogger();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "Provider<Logger>";
		}
	}

	
	/**
	 * The Class RootModule.
	 *
	 * @author l.xue.nong
	 */
	private static class RootModule implements Module {

		
		/** The stage. */
		final Stage stage;

		
		/**
		 * Instantiates a new root module.
		 *
		 * @param stage the stage
		 */
		private RootModule(Stage stage) {
			this.stage = checkNotNull(stage, "stage");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Module#configure(cn.com.rebirth.search.commons.inject.Binder)
		 */
		public void configure(Binder binder) {
			binder = binder.withSource(SourceProvider.UNKNOWN_SOURCE);
			binder.bind(Stage.class).toInstance(stage);
			binder.bindScope(Singleton.class, Scopes.SINGLETON);
		}
	}
}
