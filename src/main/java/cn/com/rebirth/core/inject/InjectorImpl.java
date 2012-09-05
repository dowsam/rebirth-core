/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectorImpl.java 2012-7-6 10:23:50 l.xue.nong$$
 */
package cn.com.rebirth.core.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.commons.Classes;
import cn.com.rebirth.commons.Nullable;
import cn.com.rebirth.core.inject.internal.Annotations;
import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InstanceBindingImpl;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.LinkedBindingImpl;
import cn.com.rebirth.core.inject.internal.LinkedProviderBindingImpl;
import cn.com.rebirth.core.inject.internal.MatcherAndConverter;
import cn.com.rebirth.core.inject.internal.Scoping;
import cn.com.rebirth.core.inject.internal.SourceProvider;
import cn.com.rebirth.core.inject.internal.ToStringBuilder;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.ConvertedConstantBinding;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.ProviderBinding;
import cn.com.rebirth.core.inject.spi.ProviderKeyBinding;
import cn.com.rebirth.core.inject.util.Providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class InjectorImpl.
 *
 * @author l.xue.nong
 */
class InjectorImpl implements Injector, Lookups {

	/** The state. */
	final State state;

	/** The parent. */
	final InjectorImpl parent;

	/** The bindings multimap. */
	final BindingsMultimap bindingsMultimap = new BindingsMultimap();

	/** The initializer. */
	final Initializer initializer;

	/** The jit bindings. */
	Map<Key<?>, BindingImpl<?>> jitBindings = Maps.newHashMap();

	/** The lookups. */
	Lookups lookups = new DeferredLookups(this);

	/**
	 * Instantiates a new injector impl.
	 *
	 * @param parent the parent
	 * @param state the state
	 * @param initializer the initializer
	 */
	InjectorImpl(@Nullable InjectorImpl parent, State state, Initializer initializer) {
		this.parent = parent;
		this.state = state;
		this.initializer = initializer;

		if (parent != null) {
			localContext = parent.localContext;
		} else {
			localContext = new ThreadLocal<Object[]>() {
				protected Object[] initialValue() {
					return new Object[1];
				}
			};
		}
	}

	/**
	 * Index.
	 */
	void index() {
		for (Binding<?> binding : state.getExplicitBindingsThisLevel().values()) {
			index(binding);
		}
	}

	/**
	 * Index.
	 *
	 * @param <T> the generic type
	 * @param binding the binding
	 */
	<T> void index(Binding<T> binding) {
		bindingsMultimap.put(binding.getKey().getTypeLiteral(), binding);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#findBindingsByType(cn.com.rebirth.search.commons.inject.TypeLiteral)
	 */
	public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
		return bindingsMultimap.getAll(type);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getBinding(cn.com.rebirth.search.commons.inject.Key)
	 */
	public <T> BindingImpl<T> getBinding(Key<T> key) {
		Errors errors = new Errors(key);
		try {
			BindingImpl<T> result = getBindingOrThrow(key, errors);
			errors.throwConfigurationExceptionIfErrorsExist();
			return result;
		} catch (ErrorsException e) {
			throw new ConfigurationException(errors.merge(e.getErrors()).getMessages());
		}
	}

	/**
	 * Gets the binding or throw.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding or throw
	 * @throws ErrorsException the errors exception
	 */
	public <T> BindingImpl<T> getBindingOrThrow(Key<T> key, Errors errors) throws ErrorsException {

		BindingImpl<T> binding = state.getExplicitBinding(key);
		if (binding != null) {
			return binding;
		}

		return getJustInTimeBinding(key, errors);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getBinding(java.lang.Class)
	 */
	public <T> Binding<T> getBinding(Class<T> type) {
		return getBinding(Key.get(type));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getParent()
	 */
	public Injector getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#createChildInjector(java.lang.Iterable)
	 */
	public Injector createChildInjector(Iterable<? extends Module> modules) {
		return new InjectorBuilder().parentInjector(this).addModules(modules).build();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#createChildInjector(cn.com.rebirth.search.commons.inject.Module[])
	 */
	public Injector createChildInjector(Module... modules) {
		return createChildInjector(ImmutableList.copyOf(modules));
	}

	/**
	 * Gets the just in time binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the just in time binding
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<T> getJustInTimeBinding(Key<T> key, Errors errors) throws ErrorsException {
		synchronized (state.lock()) {

			for (InjectorImpl injector = this; injector != null; injector = injector.parent) {
				@SuppressWarnings("unchecked")
				BindingImpl<T> binding = (BindingImpl<T>) injector.jitBindings.get(key);

				if (binding != null) {
					return binding;
				}
			}

			return createJustInTimeBindingRecursive(key, errors);
		}
	}

	/**
	 * Checks if is provider.
	 *
	 * @param key the key
	 * @return true, if is provider
	 */
	static boolean isProvider(Key<?> key) {
		return key.getTypeLiteral().getRawType().equals(Provider.class);
	}

	/**
	 * Checks if is members injector.
	 *
	 * @param key the key
	 * @return true, if is members injector
	 */
	static boolean isMembersInjector(Key<?> key) {
		return key.getTypeLiteral().getRawType().equals(MembersInjector.class) && !key.hasAnnotationType();
	}

	/**
	 * Creates the members injector binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<MembersInjector<T>> createMembersInjectorBinding(Key<MembersInjector<T>> key, Errors errors)
			throws ErrorsException {
		Type membersInjectorType = key.getTypeLiteral().getType();
		if (!(membersInjectorType instanceof ParameterizedType)) {
			throw errors.cannotInjectRawMembersInjector().toException();
		}

		@SuppressWarnings("unchecked")
		TypeLiteral<T> instanceType = (TypeLiteral<T>) TypeLiteral.get(((ParameterizedType) membersInjectorType)
				.getActualTypeArguments()[0]);
		MembersInjector<T> membersInjector = membersInjectorStore.get(instanceType, errors);

		InternalFactory<MembersInjector<T>> factory = new ConstantFactory<MembersInjector<T>>(
				Initializables.of(membersInjector));

		return new InstanceBindingImpl<MembersInjector<T>>(this, key, SourceProvider.UNKNOWN_SOURCE, factory,
				ImmutableSet.<InjectionPoint> of(), membersInjector);
	}

	/**
	 * Creates the provider binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<Provider<T>> createProviderBinding(Key<Provider<T>> key, Errors errors)
			throws ErrorsException {
		Type providerType = key.getTypeLiteral().getType();

		if (!(providerType instanceof ParameterizedType)) {
			throw errors.cannotInjectRawProvider().toException();
		}

		Type entryType = ((ParameterizedType) providerType).getActualTypeArguments()[0];

		@SuppressWarnings("unchecked")
		Key<T> providedKey = (Key<T>) key.ofType(entryType);

		BindingImpl<T> delegate = getBindingOrThrow(providedKey, errors);
		return new ProviderBindingImpl<T>(this, key, delegate);
	}

	/**
	 * The Class ProviderBindingImpl.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	static class ProviderBindingImpl<T> extends BindingImpl<Provider<T>> implements ProviderBinding<Provider<T>> {

		/** The provided binding. */
		final BindingImpl<T> providedBinding;

		/**
		 * Instantiates a new provider binding impl.
		 *
		 * @param injector the injector
		 * @param key the key
		 * @param providedBinding the provided binding
		 */
		ProviderBindingImpl(InjectorImpl injector, Key<Provider<T>> key, Binding<T> providedBinding) {
			super(injector, key, providedBinding.getSource(), createInternalFactory(providedBinding), Scoping.UNSCOPED);
			this.providedBinding = (BindingImpl<T>) providedBinding;
		}

		/**
		 * Creates the internal factory.
		 *
		 * @param <T> the generic type
		 * @param providedBinding the provided binding
		 * @return the internal factory
		 */
		static <T> InternalFactory<Provider<T>> createInternalFactory(Binding<T> providedBinding) {
			final Provider<T> provider = providedBinding.getProvider();
			return new InternalFactory<Provider<T>>() {
				public Provider<T> get(Errors errors, InternalContext context,
						@SuppressWarnings("rawtypes") Dependency dependency) {
					return provider;
				}
			};
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.ProviderBinding#getProvidedKey()
		 */
		public Key<? extends T> getProvidedKey() {
			return providedBinding.getKey();
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
		 */
		public <V> V acceptTargetVisitor(BindingTargetVisitor<? super Provider<T>, V> visitor) {
			return visitor.visit(this);
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
		 */
		public void applyTo(Binder binder) {
			throw new UnsupportedOperationException("This element represents a synthetic binding.");
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
		 */
		@Override
		public String toString() {
			return new ToStringBuilder(ProviderKeyBinding.class).add("key", getKey())
					.add("providedKey", getProvidedKey()).toString();
		}
	}

	/**
	 * Convert constant string binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<T> convertConstantStringBinding(Key<T> key, Errors errors) throws ErrorsException {

		Key<String> stringKey = key.ofType(String.class);
		BindingImpl<String> stringBinding = state.getExplicitBinding(stringKey);
		if (stringBinding == null || !stringBinding.isConstant()) {
			return null;
		}

		String stringValue = stringBinding.getProvider().get();
		Object source = stringBinding.getSource();

		TypeLiteral<T> type = key.getTypeLiteral();
		MatcherAndConverter matchingConverter = state.getConverter(stringValue, type, errors, source);

		if (matchingConverter == null) {

			return null;
		}

		try {
			@SuppressWarnings("unchecked")
			T converted = (T) matchingConverter.getTypeConverter().convert(stringValue, type);

			if (converted == null) {
				throw errors.converterReturnedNull(stringValue, source, type, matchingConverter).toException();
			}

			if (!type.getRawType().isInstance(converted)) {
				throw errors.conversionTypeError(stringValue, source, type, matchingConverter, converted).toException();
			}

			return new ConvertedConstantBindingImpl<T>(this, key, converted, stringBinding);
		} catch (ErrorsException e) {
			throw e;
		} catch (RuntimeException e) {
			throw errors.conversionError(stringValue, source, type, matchingConverter, e).toException();
		}
	}

	/**
	 * The Class ConvertedConstantBindingImpl.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static class ConvertedConstantBindingImpl<T> extends BindingImpl<T> implements ConvertedConstantBinding<T> {

		/** The value. */
		final T value;

		/** The provider. */
		final Provider<T> provider;

		/** The original binding. */
		final Binding<String> originalBinding;

		/**
		 * Instantiates a new converted constant binding impl.
		 *
		 * @param injector the injector
		 * @param key the key
		 * @param value the value
		 * @param originalBinding the original binding
		 */
		ConvertedConstantBindingImpl(Injector injector, Key<T> key, T value, Binding<String> originalBinding) {
			super(injector, key, originalBinding.getSource(), new ConstantFactory<T>(Initializables.of(value)),
					Scoping.UNSCOPED);
			this.value = value;
			provider = Providers.of(value);
			this.originalBinding = originalBinding;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#getProvider()
		 */
		@Override
		public Provider<T> getProvider() {
			return provider;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
		 */
		public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
			return visitor.visit(this);
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.ConvertedConstantBinding#getValue()
		 */
		public T getValue() {
			return value;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.ConvertedConstantBinding#getSourceKey()
		 */
		public Key<String> getSourceKey() {
			return originalBinding.getKey();
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.ConvertedConstantBinding#getDependencies()
		 */
		public Set<Dependency<?>> getDependencies() {
			return ImmutableSet.<Dependency<?>> of(Dependency.get(getSourceKey()));
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
		 */
		public void applyTo(Binder binder) {
			throw new UnsupportedOperationException("This element represents a synthetic binding.");
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
		 */
		@Override
		public String toString() {
			return new ToStringBuilder(ConvertedConstantBinding.class).add("key", getKey())
					.add("sourceKey", getSourceKey()).add("value", value).toString();
		}
	}

	/**
	 * Initialize binding.
	 *
	 * @param <T> the generic type
	 * @param binding the binding
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	@SuppressWarnings("rawtypes")
	<T> void initializeBinding(BindingImpl<T> binding, Errors errors) throws ErrorsException {
		if (binding instanceof ConstructorBindingImpl<?>) {
			Key<T> key = binding.getKey();
			jitBindings.put(key, binding);
			boolean successful = false;
			try {
				((ConstructorBindingImpl) binding).initialize(this, errors);
				successful = true;
			} finally {
				if (!successful) {
					jitBindings.remove(key);
				}
			}
		}
	}

	/**
	 * Creates the unitialized binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param scoping the scoping
	 * @param source the source
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	<T> BindingImpl<T> createUnitializedBinding(Key<T> key, Scoping scoping, Object source, Errors errors)
			throws ErrorsException {
		Class<?> rawType = key.getTypeLiteral().getRawType();

		if (rawType.isArray() || rawType.isEnum()) {
			throw errors.missingImplementation(key).toException();
		}

		if (rawType == TypeLiteral.class) {
			@SuppressWarnings("unchecked")
			BindingImpl<T> binding = (BindingImpl<T>) createTypeLiteralBinding((Key<TypeLiteral<Object>>) key, errors);
			return binding;
		}

		ImplementedBy implementedBy = rawType.getAnnotation(ImplementedBy.class);
		if (implementedBy != null) {
			Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
			return createImplementedByBinding(key, scoping, implementedBy, errors);
		}

		ProvidedBy providedBy = rawType.getAnnotation(ProvidedBy.class);
		if (providedBy != null) {
			Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
			return createProvidedByBinding(key, scoping, providedBy, errors);
		}

		if (Modifier.isAbstract(rawType.getModifiers())) {
			throw errors.missingImplementation(key).toException();
		}

		if (Classes.isInnerClass(rawType)) {
			throw errors.cannotInjectInnerClass(rawType).toException();
		}

		if (!scoping.isExplicitlyScoped()) {
			Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, rawType);
			if (scopeAnnotation != null) {
				scoping = Scopes.makeInjectable(Scoping.forAnnotation(scopeAnnotation), this,
						errors.withSource(rawType));
			}
		}

		return ConstructorBindingImpl.create(this, key, source, scoping);
	}

	/**
	 * Creates the type literal binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<TypeLiteral<T>> createTypeLiteralBinding(Key<TypeLiteral<T>> key, Errors errors)
			throws ErrorsException {
		Type typeLiteralType = key.getTypeLiteral().getType();
		if (!(typeLiteralType instanceof ParameterizedType)) {
			throw errors.cannotInjectRawTypeLiteral().toException();
		}

		ParameterizedType parameterizedType = (ParameterizedType) typeLiteralType;
		Type innerType = parameterizedType.getActualTypeArguments()[0];

		if (!(innerType instanceof Class) && !(innerType instanceof GenericArrayType)
				&& !(innerType instanceof ParameterizedType)) {
			throw errors.cannotInjectTypeLiteralOf(innerType).toException();
		}

		@SuppressWarnings("unchecked")
		TypeLiteral<T> value = (TypeLiteral<T>) TypeLiteral.get(innerType);
		InternalFactory<TypeLiteral<T>> factory = new ConstantFactory<TypeLiteral<T>>(Initializables.of(value));
		return new InstanceBindingImpl<TypeLiteral<T>>(this, key, SourceProvider.UNKNOWN_SOURCE, factory,
				ImmutableSet.<InjectionPoint> of(), value);
	}

	/**
	 * Creates the provided by binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param scoping the scoping
	 * @param providedBy the provided by
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	<T> BindingImpl<T> createProvidedByBinding(Key<T> key, Scoping scoping, ProvidedBy providedBy, Errors errors)
			throws ErrorsException {
		final Class<?> rawType = key.getTypeLiteral().getRawType();
		final Class<? extends Provider<?>> providerType = providedBy.value();

		if (providerType == rawType) {
			throw errors.recursiveProviderType().toException();
		}

		@SuppressWarnings("unchecked")
		final Key<? extends Provider<T>> providerKey = (Key<? extends Provider<T>>) Key.get(providerType);
		final BindingImpl<? extends Provider<?>> providerBinding = getBindingOrThrow(providerKey, errors);

		InternalFactory<T> internalFactory = new InternalFactory<T>() {
			@SuppressWarnings("rawtypes")
			public T get(Errors errors, InternalContext context, Dependency dependency) throws ErrorsException {
				errors = errors.withSource(providerKey);
				Provider<?> provider = providerBinding.getInternalFactory().get(errors, context, dependency);
				try {
					Object o = provider.get();
					if (o != null && !rawType.isInstance(o)) {
						throw errors.subtypeNotProvided(providerType, rawType).toException();
					}
					@SuppressWarnings("unchecked")
					T t = (T) o;
					return t;
				} catch (RuntimeException e) {
					throw errors.errorInProvider(e).toException();
				}
			}
		};

		return new LinkedProviderBindingImpl<T>(this, key, rawType, Scopes.<T> scope(key, this, internalFactory,
				scoping), scoping, providerKey);
	}

	/**
	 * Creates the implemented by binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param scoping the scoping
	 * @param implementedBy the implemented by
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	<T> BindingImpl<T> createImplementedByBinding(Key<T> key, Scoping scoping, ImplementedBy implementedBy,
			Errors errors) throws ErrorsException {
		Class<?> rawType = key.getTypeLiteral().getRawType();
		Class<?> implementationType = implementedBy.value();

		if (implementationType == rawType) {
			throw errors.recursiveImplementationType().toException();
		}

		if (!rawType.isAssignableFrom(implementationType)) {
			throw errors.notASubtype(implementationType, rawType).toException();
		}

		@SuppressWarnings("unchecked")
		Class<? extends T> subclass = (Class<? extends T>) implementationType;

		final Key<? extends T> targetKey = Key.get(subclass);
		final BindingImpl<? extends T> targetBinding = getBindingOrThrow(targetKey, errors);

		InternalFactory<T> internalFactory = new InternalFactory<T>() {
			public T get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException {
				return targetBinding.getInternalFactory().get(errors.withSource(targetKey), context, dependency);
			}
		};

		return new LinkedBindingImpl<T>(this, key, rawType, Scopes.<T> scope(key, this, internalFactory, scoping),
				scoping, targetKey);
	}

	/**
	 * Creates the just in time binding recursive.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> BindingImpl<T> createJustInTimeBindingRecursive(Key<T> key, Errors errors) throws ErrorsException {

		if (parent != null) {
			try {
				return parent.createJustInTimeBindingRecursive(key, new Errors());
			} catch (ErrorsException ignored) {
			}
		}

		if (state.isBlacklisted(key)) {
			throw errors.childBindingAlreadySet(key).toException();
		}

		BindingImpl<T> binding = createJustInTimeBinding(key, errors);
		state.parent().blacklist(key);
		jitBindings.put(key, binding);
		return binding;
	}

	/**
	 * Creates the just in time binding.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the binding impl
	 * @throws ErrorsException the errors exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	<T> BindingImpl<T> createJustInTimeBinding(Key<T> key, Errors errors) throws ErrorsException {
		if (state.isBlacklisted(key)) {
			throw errors.childBindingAlreadySet(key).toException();
		}

		if (isProvider(key)) {

			BindingImpl binding = createProviderBinding((Key) key, errors);
			return binding;
		}

		if (isMembersInjector(key)) {

			BindingImpl binding = createMembersInjectorBinding((Key) key, errors);
			return binding;
		}

		BindingImpl<T> convertedBinding = convertConstantStringBinding(key, errors);
		if (convertedBinding != null) {
			return convertedBinding;
		}

		if (key.hasAnnotationType()) {

			if (key.hasAttributes()) {
				try {
					Errors ignored = new Errors();
					return getBindingOrThrow(key.withoutAttributes(), ignored);
				} catch (ErrorsException ignored) {

				}
			}
			throw errors.missingImplementation(key).toException();
		}

		Object source = key.getTypeLiteral().getRawType();
		BindingImpl<T> binding = createUnitializedBinding(key, Scoping.UNSCOPED, source, errors);
		initializeBinding(binding, errors);
		return binding;
	}

	/**
	 * Gets the internal factory.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the internal factory
	 * @throws ErrorsException the errors exception
	 */
	<T> InternalFactory<? extends T> getInternalFactory(Key<T> key, Errors errors) throws ErrorsException {
		return getBindingOrThrow(key, errors).getInternalFactory();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getBindings()
	 */
	public Map<Key<?>, Binding<?>> getBindings() {
		return state.getExplicitBindingsThisLevel();
	}

	/**
	 * The Class BindingsMultimap.
	 *
	 * @author l.xue.nong
	 */
	private static class BindingsMultimap {

		/** The multimap. */
		final Map<TypeLiteral<?>, List<Binding<?>>> multimap = Maps.newHashMap();

		/**
		 * Put.
		 *
		 * @param <T> the generic type
		 * @param type the type
		 * @param binding the binding
		 */
		<T> void put(TypeLiteral<T> type, Binding<T> binding) {
			List<Binding<?>> bindingsForType = multimap.get(type);
			if (bindingsForType == null) {
				bindingsForType = Lists.newArrayList();
				multimap.put(type, bindingsForType);
			}
			bindingsForType.add(binding);
		}

		/**
		 * Gets the all.
		 *
		 * @param <T> the generic type
		 * @param type the type
		 * @return the all
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		<T> List<Binding<T>> getAll(TypeLiteral<T> type) {
			List<Binding<?>> bindings = multimap.get(type);
			return bindings != null ? Collections.<Binding<T>> unmodifiableList((List) multimap.get(type))
					: ImmutableList.<Binding<T>> of();
		}
	}

	/**
	 * Gets the parameters injectors.
	 *
	 * @param parameters the parameters
	 * @param errors the errors
	 * @return the parameters injectors
	 * @throws ErrorsException the errors exception
	 */
	SingleParameterInjector<?>[] getParametersInjectors(List<Dependency<?>> parameters, Errors errors)
			throws ErrorsException {
		if (parameters.isEmpty()) {
			return null;
		}

		int numErrorsBefore = errors.size();
		SingleParameterInjector<?>[] result = new SingleParameterInjector<?>[parameters.size()];
		int i = 0;
		for (Dependency<?> parameter : parameters) {
			try {
				result[i++] = createParameterInjector(parameter, errors.withSource(parameter));
			} catch (ErrorsException rethrownBelow) {

			}
		}

		errors.throwIfNewErrors(numErrorsBefore);
		return result;
	}

	/**
	 * Creates the parameter injector.
	 *
	 * @param <T> the generic type
	 * @param dependency the dependency
	 * @param errors the errors
	 * @return the single parameter injector
	 * @throws ErrorsException the errors exception
	 */
	<T> SingleParameterInjector<T> createParameterInjector(final Dependency<T> dependency, final Errors errors)
			throws ErrorsException {
		InternalFactory<? extends T> factory = getInternalFactory(dependency.getKey(), errors);
		return new SingleParameterInjector<T>(dependency, factory);
	}

	/**
	 * The Interface MethodInvoker.
	 *
	 * @author l.xue.nong
	 */
	interface MethodInvoker {

		/**
		 * Invoke.
		 *
		 * @param target the target
		 * @param parameters the parameters
		 * @return the object
		 * @throws IllegalAccessException the illegal access exception
		 * @throws InvocationTargetException the invocation target exception
		 */
		Object invoke(Object target, Object... parameters) throws IllegalAccessException, InvocationTargetException;
	}

	/** The constructors. */
	ConstructorInjectorStore constructors = new ConstructorInjectorStore(this);

	/** The members injector store. */
	MembersInjectorStore membersInjectorStore;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#injectMembers(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void injectMembers(Object instance) {
		MembersInjector membersInjector = getMembersInjector(instance.getClass());
		membersInjector.injectMembers(instance);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getMembersInjector(cn.com.rebirth.search.commons.inject.TypeLiteral)
	 */
	public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
		Errors errors = new Errors(typeLiteral);
		try {
			return membersInjectorStore.get(typeLiteral, errors);
		} catch (ErrorsException e) {
			throw new ConfigurationException(errors.merge(e.getErrors()).getMessages());
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getMembersInjector(java.lang.Class)
	 */
	public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
		return getMembersInjector(TypeLiteral.get(type));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getProvider(java.lang.Class)
	 */
	public <T> Provider<T> getProvider(Class<T> type) {
		return getProvider(Key.get(type));
	}

	/**
	 * Gets the provider or throw.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the provider or throw
	 * @throws ErrorsException the errors exception
	 */
	<T> Provider<T> getProviderOrThrow(final Key<T> key, Errors errors) throws ErrorsException {
		final InternalFactory<? extends T> factory = getInternalFactory(key, errors);
		final Dependency<T> dependency = Dependency.get(key);

		return new Provider<T>() {
			public T get() {
				final Errors errors = new Errors(dependency);
				try {
					T t = callInContext(new ContextualCallable<T>() {
						public T call(InternalContext context) throws ErrorsException {
							context.setDependency(dependency);
							try {
								return factory.get(errors, context, dependency);
							} finally {
								context.setDependency(null);
							}
						}
					});
					errors.throwIfNewErrors(0);
					return t;
				} catch (ErrorsException e) {
					throw new ProvisionException(errors.merge(e.getErrors()).getMessages());
				}
			}

			@Override
			public String toString() {
				return factory.toString();
			}
		};
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getProvider(cn.com.rebirth.search.commons.inject.Key)
	 */
	public <T> Provider<T> getProvider(final Key<T> key) {
		Errors errors = new Errors(key);
		try {
			Provider<T> result = getProviderOrThrow(key, errors);
			errors.throwIfNewErrors(0);
			return result;
		} catch (ErrorsException e) {
			throw new ConfigurationException(errors.merge(e.getErrors()).getMessages());
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getInstance(cn.com.rebirth.search.commons.inject.Key)
	 */
	public <T> T getInstance(Key<T> key) {
		return getProvider(key).get();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Injector#getInstance(java.lang.Class)
	 */
	public <T> T getInstance(Class<T> type) {
		return getProvider(type).get();
	}

	/** The local context. */
	final ThreadLocal<Object[]> localContext;

	/**
	 * Call in context.
	 *
	 * @param <T> the generic type
	 * @param callable the callable
	 * @return the t
	 * @throws ErrorsException the errors exception
	 */
	<T> T callInContext(ContextualCallable<T> callable) throws ErrorsException {
		Object[] reference = localContext.get();
		if (reference[0] == null) {
			reference[0] = new InternalContext();
			try {
				return callable.call((InternalContext) reference[0]);
			} finally {

				reference[0] = null;
			}
		} else {

			return callable.call((InternalContext) reference[0]);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(Injector.class).add("bindings", state.getExplicitBindingsThisLevel().values())
				.toString();
	}

	/**
	 * Clear cache.
	 */
	public void clearCache() {
		state.clearBlacklisted();
		constructors = new ConstructorInjectorStore(this);
		membersInjectorStore = new MembersInjectorStore(this, state.getTypeListenerBindings());
		jitBindings = Maps.newHashMap();
	}
}
