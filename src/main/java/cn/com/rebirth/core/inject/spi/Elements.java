/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Elements.java 2012-7-6 10:23:44 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.AbstractModule;
import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.MembersInjector;
import cn.com.rebirth.core.inject.Module;
import cn.com.rebirth.core.inject.PrivateBinder;
import cn.com.rebirth.core.inject.PrivateModule;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.Scope;
import cn.com.rebirth.core.inject.Stage;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.binder.AnnotatedBindingBuilder;
import cn.com.rebirth.core.inject.binder.AnnotatedConstantBindingBuilder;
import cn.com.rebirth.core.inject.binder.AnnotatedElementBuilder;
import cn.com.rebirth.core.inject.internal.AbstractBindingBuilder;
import cn.com.rebirth.core.inject.internal.BindingBuilder;
import cn.com.rebirth.core.inject.internal.ConstantBindingBuilderImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ExposureBuilder;
import cn.com.rebirth.core.inject.internal.PrivateElementsImpl;
import cn.com.rebirth.core.inject.internal.ProviderMethodsModule;
import cn.com.rebirth.core.inject.internal.SourceProvider;
import cn.com.rebirth.core.inject.matcher.Matcher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * The Class Elements.
 *
 * @author l.xue.nong
 */
public final class Elements {

	
	/** The Constant GET_INSTANCE_VISITOR. */
	private static final BindingTargetVisitor<Object, Object> GET_INSTANCE_VISITOR = new DefaultBindingTargetVisitor<Object, Object>() {
		@Override
		public Object visit(InstanceBinding<?> binding) {
			return binding.getInstance();
		}

		@Override
		protected Object visitOther(Binding<?> binding) {
			throw new IllegalArgumentException();
		}
	};

	
	/**
	 * Gets the elements.
	 *
	 * @param modules the modules
	 * @return the elements
	 */
	public static List<Element> getElements(Module... modules) {
		return getElements(Stage.DEVELOPMENT, Arrays.asList(modules));
	}

	
	/**
	 * Gets the elements.
	 *
	 * @param stage the stage
	 * @param modules the modules
	 * @return the elements
	 */
	public static List<Element> getElements(Stage stage, Module... modules) {
		return getElements(stage, Arrays.asList(modules));
	}

	
	/**
	 * Gets the elements.
	 *
	 * @param modules the modules
	 * @return the elements
	 */
	public static List<Element> getElements(Iterable<? extends Module> modules) {
		return getElements(Stage.DEVELOPMENT, modules);
	}

	
	/**
	 * Gets the elements.
	 *
	 * @param stage the stage
	 * @param modules the modules
	 * @return the elements
	 */
	public static List<Element> getElements(Stage stage, Iterable<? extends Module> modules) {
		RecordingBinder binder = new RecordingBinder(stage);
		for (Module module : modules) {
			binder.install(module);
		}
		return Collections.unmodifiableList(binder.elements);
	}

	
	/**
	 * Gets the module.
	 *
	 * @param elements the elements
	 * @return the module
	 */
	public static Module getModule(final Iterable<? extends Element> elements) {
		return new Module() {
			public void configure(Binder binder) {
				for (Element element : elements) {
					element.applyTo(binder);
				}
			}
		};
	}

	
	/**
	 * Gets the instance visitor.
	 *
	 * @param <T> the generic type
	 * @return the instance visitor
	 */
	@SuppressWarnings("unchecked")
	static <T> BindingTargetVisitor<T, T> getInstanceVisitor() {
		return (BindingTargetVisitor<T, T>) GET_INSTANCE_VISITOR;
	}

	
	/**
	 * The Class RecordingBinder.
	 *
	 * @author l.xue.nong
	 */
	private static class RecordingBinder implements Binder, PrivateBinder {

		
		/** The stage. */
		private final Stage stage;

		
		/** The modules. */
		private final Set<Module> modules;

		
		/** The elements. */
		private final List<Element> elements;

		
		/** The source. */
		private final Object source;

		
		/** The source provider. */
		private final SourceProvider sourceProvider;

		
		/** The parent. */
		private final RecordingBinder parent;

		
		/** The private elements. */
		private final PrivateElementsImpl privateElements;

		
		/**
		 * Instantiates a new recording binder.
		 *
		 * @param stage the stage
		 */
		private RecordingBinder(Stage stage) {
			this.stage = stage;
			this.modules = Sets.newHashSet();
			this.elements = Lists.newArrayList();
			this.source = null;
			this.sourceProvider = new SourceProvider().plusSkippedClasses(Elements.class, RecordingBinder.class,
					AbstractModule.class, ConstantBindingBuilderImpl.class, AbstractBindingBuilder.class,
					BindingBuilder.class);
			this.parent = null;
			this.privateElements = null;
		}

		
		/**
		 * Instantiates a new recording binder.
		 *
		 * @param prototype the prototype
		 * @param source the source
		 * @param sourceProvider the source provider
		 */
		private RecordingBinder(RecordingBinder prototype, Object source, SourceProvider sourceProvider) {
			checkArgument(source == null ^ sourceProvider == null);

			this.stage = prototype.stage;
			this.modules = prototype.modules;
			this.elements = prototype.elements;
			this.source = source;
			this.sourceProvider = sourceProvider;
			this.parent = prototype.parent;
			this.privateElements = prototype.privateElements;
		}

		
		/**
		 * Instantiates a new recording binder.
		 *
		 * @param parent the parent
		 * @param privateElements the private elements
		 */
		private RecordingBinder(RecordingBinder parent, PrivateElementsImpl privateElements) {
			this.stage = parent.stage;
			this.modules = Sets.newHashSet();
			this.elements = privateElements.getElementsMutable();
			this.source = parent.source;
			this.sourceProvider = parent.sourceProvider;
			this.parent = parent;
			this.privateElements = privateElements;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bindScope(java.lang.Class, cn.com.rebirth.search.commons.inject.Scope)
		 */
		public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
			elements.add(new ScopeBinding(getSource(), annotationType, scope));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#requestInjection(java.lang.Object)
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		
		public void requestInjection(Object instance) {
			requestInjection((TypeLiteral) TypeLiteral.get(instance.getClass()), instance);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#requestInjection(cn.com.rebirth.search.commons.inject.TypeLiteral, java.lang.Object)
		 */
		public <T> void requestInjection(TypeLiteral<T> type, T instance) {
			elements.add(new InjectionRequest<T>(getSource(), type, instance));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#getMembersInjector(cn.com.rebirth.search.commons.inject.TypeLiteral)
		 */
		public <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
			final MembersInjectorLookup<T> element = new MembersInjectorLookup<T>(getSource(), typeLiteral);
			elements.add(element);
			return element.getMembersInjector();
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#getMembersInjector(java.lang.Class)
		 */
		public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
			return getMembersInjector(TypeLiteral.get(type));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bindListener(cn.com.rebirth.search.commons.inject.matcher.Matcher, cn.com.rebirth.search.commons.inject.spi.TypeListener)
		 */
		public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
			elements.add(new TypeListenerBinding(getSource(), listener, typeMatcher));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#requestStaticInjection(java.lang.Class<?>[])
		 */
		public void requestStaticInjection(Class<?>... types) {
			for (Class<?> type : types) {
				elements.add(new StaticInjectionRequest(getSource(), type));
			}
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#install(cn.com.rebirth.search.commons.inject.Module)
		 */
		public void install(Module module) {
			if (modules.add(module)) {
				Binder binder = this;
				if (module instanceof PrivateModule) {
					binder = binder.newPrivateBinder();
				}

				try {
					module.configure(binder);
				} catch (RuntimeException e) {
					Collection<Message> messages = Errors.getMessagesFromThrowable(e);
					if (!messages.isEmpty()) {
						elements.addAll(messages);
					} else {
						addError(e);
					}
				}
				binder.install(ProviderMethodsModule.forModule(module));
			}
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#currentStage()
		 */
		public Stage currentStage() {
			return stage;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#addError(java.lang.String, java.lang.Object[])
		 */
		public void addError(String message, Object... arguments) {
			elements.add(new Message(getSource(), Errors.format(message, arguments)));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#addError(java.lang.Throwable)
		 */
		public void addError(Throwable t) {
			String message = "An exception was caught and reported. Message: " + t.getMessage();
			elements.add(new Message(ImmutableList.of(getSource()), message, t));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#addError(cn.com.rebirth.search.commons.inject.spi.Message)
		 */
		public void addError(Message message) {
			elements.add(message);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bind(cn.com.rebirth.search.commons.inject.Key)
		 */
		public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
			return new BindingBuilder<T>(this, elements, getSource(), key);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bind(cn.com.rebirth.search.commons.inject.TypeLiteral)
		 */
		public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
			return bind(Key.get(typeLiteral));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bind(java.lang.Class)
		 */
		public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
			return bind(Key.get(type));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#bindConstant()
		 */
		public AnnotatedConstantBindingBuilder bindConstant() {
			return new ConstantBindingBuilderImpl<Void>(this, elements, getSource());
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#getProvider(cn.com.rebirth.search.commons.inject.Key)
		 */
		public <T> Provider<T> getProvider(final Key<T> key) {
			final ProviderLookup<T> element = new ProviderLookup<T>(getSource(), key);
			elements.add(element);
			return element.getProvider();
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#getProvider(java.lang.Class)
		 */
		public <T> Provider<T> getProvider(Class<T> type) {
			return getProvider(Key.get(type));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#convertToTypes(cn.com.rebirth.search.commons.inject.matcher.Matcher, cn.com.rebirth.search.commons.inject.spi.TypeConverter)
		 */
		public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
			elements.add(new TypeConverterBinding(getSource(), typeMatcher, converter));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#withSource(java.lang.Object)
		 */
		public RecordingBinder withSource(final Object source) {
			return new RecordingBinder(this, source, null);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#skipSources(java.lang.Class[])
		 */
		@SuppressWarnings("rawtypes")
		public RecordingBinder skipSources(Class... classesToSkip) {
			
			if (source != null) {
				return this;
			}

			SourceProvider newSourceProvider = sourceProvider.plusSkippedClasses(classesToSkip);
			return new RecordingBinder(this, null, newSourceProvider);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binder#newPrivateBinder()
		 */
		public PrivateBinder newPrivateBinder() {
			PrivateElementsImpl privateElements = new PrivateElementsImpl(getSource());
			elements.add(privateElements);
			return new RecordingBinder(this, privateElements);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.PrivateBinder#expose(cn.com.rebirth.search.commons.inject.Key)
		 */
		public void expose(Key<?> key) {
			exposeInternal(key);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.PrivateBinder#expose(java.lang.Class)
		 */
		public AnnotatedElementBuilder expose(Class<?> type) {
			return exposeInternal(Key.get(type));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.PrivateBinder#expose(cn.com.rebirth.search.commons.inject.TypeLiteral)
		 */
		public AnnotatedElementBuilder expose(TypeLiteral<?> type) {
			return exposeInternal(Key.get(type));
		}

		
		/**
		 * Expose internal.
		 *
		 * @param <T> the generic type
		 * @param key the key
		 * @return the annotated element builder
		 */
		private <T> AnnotatedElementBuilder exposeInternal(Key<T> key) {
			if (privateElements == null) {
				addError("Cannot expose %s on a standard binder. "
						+ "Exposed bindings are only applicable to private binders.", key);
				return new AnnotatedElementBuilder() {
					public void annotatedWith(Class<? extends Annotation> annotationType) {
					}

					public void annotatedWith(Annotation annotation) {
					}
				};
			}

			ExposureBuilder<T> builder = new ExposureBuilder<T>(this, getSource(), key);
			privateElements.addExposureBuilder(builder);
			return builder;
		}

		
		/**
		 * Gets the source.
		 *
		 * @return the source
		 */
		protected Object getSource() {
			return sourceProvider != null ? sourceProvider.get() : source;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Binder";
		}
	}
}
