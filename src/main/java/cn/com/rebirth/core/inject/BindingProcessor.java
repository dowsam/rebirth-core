/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BindingProcessor.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.internal.Annotations;
import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.ExposedBindingImpl;
import cn.com.rebirth.core.inject.internal.InstanceBindingImpl;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.LinkedBindingImpl;
import cn.com.rebirth.core.inject.internal.LinkedProviderBindingImpl;
import cn.com.rebirth.core.inject.internal.ProviderInstanceBindingImpl;
import cn.com.rebirth.core.inject.internal.ProviderMethod;
import cn.com.rebirth.core.inject.internal.Scoping;
import cn.com.rebirth.core.inject.internal.UntargettedBindingImpl;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.ConstructorBinding;
import cn.com.rebirth.core.inject.spi.ConvertedConstantBinding;
import cn.com.rebirth.core.inject.spi.ExposedBinding;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.InstanceBinding;
import cn.com.rebirth.core.inject.spi.LinkedKeyBinding;
import cn.com.rebirth.core.inject.spi.PrivateElements;
import cn.com.rebirth.core.inject.spi.ProviderBinding;
import cn.com.rebirth.core.inject.spi.ProviderInstanceBinding;
import cn.com.rebirth.core.inject.spi.ProviderKeyBinding;
import cn.com.rebirth.core.inject.spi.UntargettedBinding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;



/**
 * The Class BindingProcessor.
 *
 * @author l.xue.nong
 */
class BindingProcessor extends AbstractProcessor {

	
	/** The creation listeners. */
	private final List<CreationListener> creationListeners = Lists.newArrayList();

	
	/** The initializer. */
	private final Initializer initializer;

	
	/** The uninitialized bindings. */
	private final List<Runnable> uninitializedBindings = Lists.newArrayList();

	
	/**
	 * Instantiates a new binding processor.
	 *
	 * @param errors the errors
	 * @param initializer the initializer
	 */
	BindingProcessor(Errors errors, Initializer initializer) {
		super(errors);
		this.initializer = initializer;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.Binding)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> Boolean visit(Binding<T> command) {
		final Object source = command.getSource();

		if (Void.class.equals(command.getKey().getRawType())) {
			if (command instanceof ProviderInstanceBinding
					&& ((ProviderInstanceBinding) command).getProviderInstance() instanceof ProviderMethod) {
				errors.voidProviderMethod();
			} else {
				errors.missingConstantValues();
			}
			return true;
		}

		final Key<T> key = command.getKey();
		Class<? super T> rawType = key.getTypeLiteral().getRawType();

		if (rawType == Provider.class) {
			errors.bindingToProvider();
			return true;
		}

		validateKey(command.getSource(), command.getKey());

		final Scoping scoping = Scopes.makeInjectable(((BindingImpl<?>) command).getScoping(), injector, errors);

		command.acceptTargetVisitor(new BindingTargetVisitor<T, Void>() {

			public Void visit(InstanceBinding<? extends T> binding) {
				Set<InjectionPoint> injectionPoints = binding.getInjectionPoints();
				T instance = binding.getInstance();
				Initializable<T> ref = initializer.requestInjection(injector, instance, source, injectionPoints);
				ConstantFactory<? extends T> factory = new ConstantFactory<T>(ref);
				InternalFactory<? extends T> scopedFactory = Scopes.scope(key, injector, factory, scoping);
				putBinding(new InstanceBindingImpl<T>(injector, key, source, scopedFactory, injectionPoints, instance));
				return null;
			}

			public Void visit(ProviderInstanceBinding<? extends T> binding) {
				Provider<? extends T> provider = binding.getProviderInstance();
				Set<InjectionPoint> injectionPoints = binding.getInjectionPoints();
				Initializable<Provider<? extends T>> initializable = initializer
						.<Provider<? extends T>> requestInjection(injector, provider, source, injectionPoints);
				InternalFactory<T> factory = new InternalFactoryToProviderAdapter<T>(initializable, source);
				InternalFactory<? extends T> scopedFactory = Scopes.scope(key, injector, factory, scoping);
				putBinding(new ProviderInstanceBindingImpl<T>(injector, key, source, scopedFactory, scoping, provider,
						injectionPoints));
				return null;
			}

			public Void visit(ProviderKeyBinding<? extends T> binding) {
				Key<? extends Provider<? extends T>> providerKey = binding.getProviderKey();
				BoundProviderFactory<T> boundProviderFactory = new BoundProviderFactory<T>(injector, providerKey,
						source);
				creationListeners.add(boundProviderFactory);
				InternalFactory<? extends T> scopedFactory = Scopes.scope(key, injector,
						(InternalFactory<? extends T>) boundProviderFactory, scoping);
				putBinding(new LinkedProviderBindingImpl<T>(injector, key, source, scopedFactory, scoping, providerKey));
				return null;
			}

			public Void visit(LinkedKeyBinding<? extends T> binding) {
				Key<? extends T> linkedKey = binding.getLinkedKey();
				if (key.equals(linkedKey)) {
					errors.recursiveBinding();
				}

				FactoryProxy<T> factory = new FactoryProxy<T>(injector, key, linkedKey, source);
				creationListeners.add(factory);
				InternalFactory<? extends T> scopedFactory = Scopes.scope(key, injector, factory, scoping);
				putBinding(new LinkedBindingImpl<T>(injector, key, source, scopedFactory, scoping, linkedKey));
				return null;
			}

			public Void visit(UntargettedBinding<? extends T> untargetted) {
				
				
				
				
				if (key.hasAnnotationType()) {
					errors.missingImplementation(key);
					putBinding(invalidBinding(injector, key, source));
					return null;
				}

				
				final BindingImpl<T> binding;
				try {
					binding = injector.createUnitializedBinding(key, scoping, source, errors);
					putBinding(binding);
				} catch (ErrorsException e) {
					errors.merge(e.getErrors());
					putBinding(invalidBinding(injector, key, source));
					return null;
				}

				uninitializedBindings.add(new Runnable() {
					public void run() {
						try {
							((InjectorImpl) binding.getInjector()).initializeBinding(binding, errors.withSource(source));
						} catch (ErrorsException e) {
							errors.merge(e.getErrors());
						}
					}
				});

				return null;
			}

			public Void visit(ExposedBinding<? extends T> binding) {
				throw new IllegalArgumentException("Cannot apply a non-module element");
			}

			public Void visit(ConvertedConstantBinding<? extends T> binding) {
				throw new IllegalArgumentException("Cannot apply a non-module element");
			}

			public Void visit(ConstructorBinding<? extends T> binding) {
				throw new IllegalArgumentException("Cannot apply a non-module element");
			}

			public Void visit(ProviderBinding<? extends T> binding) {
				throw new IllegalArgumentException("Cannot apply a non-module element");
			}
		});

		return true;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.PrivateElements)
	 */
	@Override
	public Boolean visit(PrivateElements privateElements) {
		for (Key<?> key : privateElements.getExposedKeys()) {
			bindExposed(privateElements, key);
		}
		return false; 
	}

	
	/**
	 * Bind exposed.
	 *
	 * @param <T> the generic type
	 * @param privateElements the private elements
	 * @param key the key
	 */
	private <T> void bindExposed(PrivateElements privateElements, Key<T> key) {
		ExposedKeyFactory<T> exposedKeyFactory = new ExposedKeyFactory<T>(key, privateElements);
		creationListeners.add(exposedKeyFactory);
		putBinding(new ExposedBindingImpl<T>(injector, privateElements.getExposedSource(key), key, exposedKeyFactory,
				privateElements));
	}

	
	/**
	 * Validate key.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param key the key
	 */
	private <T> void validateKey(Object source, Key<T> key) {
		Annotations.checkForMisplacedScopeAnnotations(key.getRawType(), source, errors);
	}

	
	/**
	 * Invalid binding.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @return the untargetted binding impl
	 */
	<T> UntargettedBindingImpl<T> invalidBinding(InjectorImpl injector, Key<T> key, Object source) {
		return new UntargettedBindingImpl<T>(injector, key, source);
	}

	
	/**
	 * Initialize bindings.
	 */
	public void initializeBindings() {
		for (Runnable initializer : uninitializedBindings) {
			initializer.run();
		}
	}

	
	/**
	 * Run creation listeners.
	 */
	public void runCreationListeners() {
		for (CreationListener creationListener : creationListeners) {
			creationListener.notify(errors);
		}
	}

	
	/**
	 * Put binding.
	 *
	 * @param binding the binding
	 */
	private void putBinding(BindingImpl<?> binding) {
		Key<?> key = binding.getKey();

		Class<?> rawType = key.getRawType();
		if (FORBIDDEN_TYPES.contains(rawType)) {
			errors.cannotBindToGuiceType(rawType.getSimpleName());
			return;
		}

		Binding<?> original = injector.state.getExplicitBinding(key);
		if (original != null && !isOkayDuplicate(original, binding)) {
			errors.bindingAlreadySet(key, original.getSource());
			return;
		}

		
		injector.state.parent().blacklist(key);
		injector.state.putBinding(key, binding);
	}

	
	/**
	 * Checks if is okay duplicate.
	 *
	 * @param original the original
	 * @param binding the binding
	 * @return true, if is okay duplicate
	 */
	@SuppressWarnings("rawtypes")
	private boolean isOkayDuplicate(Binding<?> original, BindingImpl<?> binding) {
		if (original instanceof ExposedBindingImpl) {
			ExposedBindingImpl exposed = (ExposedBindingImpl) original;
			InjectorImpl exposedFrom = (InjectorImpl) exposed.getPrivateElements().getInjector();
			return (exposedFrom == binding.getInjector());
		}
		return false;
	}

	
	
	
	
	/** The Constant FORBIDDEN_TYPES. */
	@SuppressWarnings("unchecked")
	private static final Set<Class<?>> FORBIDDEN_TYPES = ImmutableSet.of(AbstractModule.class, Binder.class,
			Binding.class, Injector.class, Key.class, MembersInjector.class, Module.class, Provider.class, Scope.class,
			TypeLiteral.class);

	

	
	/**
	 * The listener interface for receiving creation events.
	 * The class that is interested in processing a creation
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addCreationListener<code> method. When
	 * the creation event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see CreationEvent
	 */
	interface CreationListener {

		
		/**
		 * Notify.
		 *
		 * @param errors the errors
		 */
		void notify(Errors errors);
	}
}
