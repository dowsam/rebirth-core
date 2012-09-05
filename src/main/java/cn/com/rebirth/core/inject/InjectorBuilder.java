/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectorBuilder.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.Stopwatch;
import cn.com.rebirth.core.inject.spi.Dependency;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


/**
 * The Class InjectorBuilder.
 *
 * @author l.xue.nong
 */
class InjectorBuilder {

	
	/** The stopwatch. */
	private final Stopwatch stopwatch = new Stopwatch();

	
	/** The errors. */
	private final Errors errors = new Errors();

	
	/** The stage. */
	private Stage stage;

	
	/** The initializer. */
	private final Initializer initializer = new Initializer();

	
	/** The binding procesor. */
	private final BindingProcessor bindingProcesor;

	
	/** The injection request processor. */
	private final InjectionRequestProcessor injectionRequestProcessor;

	
	/** The shell builder. */
	private final InjectorShell.Builder shellBuilder = new InjectorShell.Builder();

	
	/** The shells. */
	private List<InjectorShell> shells;

	
	/**
	 * Instantiates a new injector builder.
	 */
	InjectorBuilder() {
		injectionRequestProcessor = new InjectionRequestProcessor(errors, initializer);
		bindingProcesor = new BindingProcessor(errors, initializer);
	}

	
	/**
	 * Stage.
	 *
	 * @param stage the stage
	 * @return the injector builder
	 */
	InjectorBuilder stage(Stage stage) {
		shellBuilder.stage(stage);
		this.stage = stage;
		return this;
	}

	
	/**
	 * Parent injector.
	 *
	 * @param parent the parent
	 * @return the injector builder
	 */
	InjectorBuilder parentInjector(InjectorImpl parent) {
		shellBuilder.parent(parent);
		return stage(parent.getInstance(Stage.class));
	}

	
	/**
	 * Adds the modules.
	 *
	 * @param modules the modules
	 * @return the injector builder
	 */
	InjectorBuilder addModules(Iterable<? extends Module> modules) {
		shellBuilder.addModules(modules);
		return this;
	}

	
	/**
	 * Builds the.
	 *
	 * @return the injector
	 */
	Injector build() {
		if (shellBuilder == null) {
			throw new AssertionError("Already built, builders are not reusable.");
		}

		
		
		synchronized (shellBuilder.lock()) {
			shells = shellBuilder.build(initializer, bindingProcesor, stopwatch, errors);
			stopwatch.resetAndLog("Injector construction");

			initializeStatically();
		}

		
		if (stage == Stage.TOOL) {
			return new ToolStageInjector(primaryInjector());
		}

		injectDynamically();

		return primaryInjector();
	}

	
	/**
	 * Initialize statically.
	 */
	private void initializeStatically() {
		bindingProcesor.initializeBindings();
		stopwatch.resetAndLog("Binding initialization");

		for (InjectorShell shell : shells) {
			shell.getInjector().index();
		}
		stopwatch.resetAndLog("Binding indexing");

		injectionRequestProcessor.process(shells);
		stopwatch.resetAndLog("Collecting injection requests");

		bindingProcesor.runCreationListeners();
		stopwatch.resetAndLog("Binding validation");

		injectionRequestProcessor.validate();
		stopwatch.resetAndLog("Static validation");

		initializer.validateOustandingInjections(errors);
		stopwatch.resetAndLog("Instance member validation");

		new LookupProcessor(errors).process(shells);
		for (InjectorShell shell : shells) {
			((DeferredLookups) shell.getInjector().lookups).initialize(errors);
		}
		stopwatch.resetAndLog("Provider verification");

		for (InjectorShell shell : shells) {
			if (!shell.getElements().isEmpty()) {
				throw new AssertionError("Failed to execute " + shell.getElements());
			}
		}

		errors.throwCreationExceptionIfErrorsExist();
	}

	
	/**
	 * Primary injector.
	 *
	 * @return the injector
	 */
	private Injector primaryInjector() {
		return shells.get(0).getInjector();
	}

	
	/**
	 * Inject dynamically.
	 */
	private void injectDynamically() {
		injectionRequestProcessor.injectMembers();
		stopwatch.resetAndLog("Static member injection");

		initializer.injectAll(errors);
		stopwatch.resetAndLog("Instance injection");
		errors.throwCreationExceptionIfErrorsExist();

		for (InjectorShell shell : shells) {
			loadEagerSingletons(shell.getInjector(), stage, errors);
		}
		stopwatch.resetAndLog("Preloading singletons");
		errors.throwCreationExceptionIfErrorsExist();
	}

	
	/**
	 * Load eager singletons.
	 *
	 * @param injector the injector
	 * @param stage the stage
	 * @param errors the errors
	 */
	@SuppressWarnings("rawtypes")
	public void loadEagerSingletons(InjectorImpl injector, Stage stage, final Errors errors) {
		@SuppressWarnings("unchecked")
		
		Set<BindingImpl<?>> candidateBindings = ImmutableSet.copyOf(Iterables.concat((Collection) injector.state
				.getExplicitBindingsThisLevel().values(), injector.jitBindings.values()));
		for (final BindingImpl<?> binding : candidateBindings) {
			if (binding.getScoping().isEagerSingleton(stage)) {
				try {
					injector.callInContext(new ContextualCallable<Void>() {
						Dependency<?> dependency = Dependency.get(binding.getKey());

						public Void call(InternalContext context) {
							context.setDependency(dependency);
							Errors errorsForBinding = errors.withSource(dependency);
							try {
								binding.getInternalFactory().get(errorsForBinding, context, dependency);
							} catch (ErrorsException e) {
								errorsForBinding.merge(e.getErrors());
							} finally {
								context.setDependency(null);
							}

							return null;
						}
					});
				} catch (ErrorsException e) {
					throw new AssertionError();
				}
			}
		}
	}

	
	/**
	 * The Class ToolStageInjector.
	 *
	 * @author l.xue.nong
	 */
	static class ToolStageInjector implements Injector {

		
		/** The delegate injector. */
		private final Injector delegateInjector;

		
		/**
		 * Instantiates a new tool stage injector.
		 *
		 * @param delegateInjector the delegate injector
		 */
		ToolStageInjector(Injector delegateInjector) {
			this.delegateInjector = delegateInjector;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#injectMembers(java.lang.Object)
		 */
		public void injectMembers(Object o) {
			throw new UnsupportedOperationException("Injector.injectMembers(Object) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getBindings()
		 */
		public Map<Key<?>, Binding<?>> getBindings() {
			return this.delegateInjector.getBindings();
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getBinding(cn.com.rebirth.search.commons.inject.Key)
		 */
		public <T> Binding<T> getBinding(Key<T> key) {
			return this.delegateInjector.getBinding(key);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getBinding(java.lang.Class)
		 */
		public <T> Binding<T> getBinding(Class<T> type) {
			return this.delegateInjector.getBinding(type);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#findBindingsByType(cn.com.rebirth.search.commons.inject.TypeLiteral)
		 */
		public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
			return this.delegateInjector.findBindingsByType(type);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getParent()
		 */
		public Injector getParent() {
			return delegateInjector.getParent();
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#createChildInjector(java.lang.Iterable)
		 */
		public Injector createChildInjector(Iterable<? extends Module> modules) {
			return delegateInjector.createChildInjector(modules);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#createChildInjector(cn.com.rebirth.search.commons.inject.Module[])
		 */
		public Injector createChildInjector(Module... modules) {
			return delegateInjector.createChildInjector(modules);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getProvider(cn.com.rebirth.search.commons.inject.Key)
		 */
		public <T> Provider<T> getProvider(Key<T> key) {
			throw new UnsupportedOperationException("Injector.getProvider(Key<T>) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getProvider(java.lang.Class)
		 */
		public <T> Provider<T> getProvider(Class<T> type) {
			throw new UnsupportedOperationException("Injector.getProvider(Class<T>) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getMembersInjector(cn.com.rebirth.search.commons.inject.TypeLiteral)
		 */
		public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
			throw new UnsupportedOperationException(
					"Injector.getMembersInjector(TypeLiteral<T>) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getMembersInjector(java.lang.Class)
		 */
		public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
			throw new UnsupportedOperationException(
					"Injector.getMembersInjector(Class<T>) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getInstance(cn.com.rebirth.search.commons.inject.Key)
		 */
		public <T> T getInstance(Key<T> key) {
			throw new UnsupportedOperationException("Injector.getInstance(Key<T>) is not supported in Stage.TOOL");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Injector#getInstance(java.lang.Class)
		 */
		public <T> T getInstance(Class<T> type) {
			throw new UnsupportedOperationException("Injector.getInstance(Class<T>) is not supported in Stage.TOOL");
		}
	}
}
