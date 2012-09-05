/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MembersInjectorStore.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.FailableCache;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**
 * The Class MembersInjectorStore.
 *
 * @author l.xue.nong
 */
class MembersInjectorStore {

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The type listener bindings. */
	private final ImmutableList<TypeListenerBinding> typeListenerBindings;

	
	/** The cache. */
	private final FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>> cache = new FailableCache<TypeLiteral<?>, MembersInjectorImpl<?>>() {
		@Override
		protected MembersInjectorImpl<?> create(TypeLiteral<?> type, Errors errors) throws ErrorsException {
			return createWithListeners(type, errors);
		}
	};

	
	/**
	 * Instantiates a new members injector store.
	 *
	 * @param injector the injector
	 * @param typeListenerBindings the type listener bindings
	 */
	MembersInjectorStore(InjectorImpl injector, List<TypeListenerBinding> typeListenerBindings) {
		this.injector = injector;
		this.typeListenerBindings = ImmutableList.copyOf(typeListenerBindings);
	}

	
	/**
	 * Checks for type listeners.
	 *
	 * @return true, if successful
	 */
	public boolean hasTypeListeners() {
		return !typeListenerBindings.isEmpty();
	}

	
	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the members injector impl
	 * @throws ErrorsException the errors exception
	 */
	@SuppressWarnings("unchecked")
	
	public <T> MembersInjectorImpl<T> get(TypeLiteral<T> key, Errors errors) throws ErrorsException {
		return (MembersInjectorImpl<T>) cache.get(key, errors);
	}

	
	/**
	 * Creates the with listeners.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param errors the errors
	 * @return the members injector impl
	 * @throws ErrorsException the errors exception
	 */
	private <T> MembersInjectorImpl<T> createWithListeners(TypeLiteral<T> type, Errors errors) throws ErrorsException {
		int numErrorsBefore = errors.size();

		Set<InjectionPoint> injectionPoints;
		try {
			injectionPoints = InjectionPoint.forInstanceMethodsAndFields(type);
		} catch (ConfigurationException e) {
			errors.merge(e.getErrorMessages());
			injectionPoints = e.getPartialValue();
		}
		ImmutableList<SingleMemberInjector> injectors = getInjectors(injectionPoints, errors);
		errors.throwIfNewErrors(numErrorsBefore);

		EncounterImpl<T> encounter = new EncounterImpl<T>(errors, injector.lookups);
		for (TypeListenerBinding typeListener : typeListenerBindings) {
			if (typeListener.getTypeMatcher().matches(type)) {
				try {
					typeListener.getListener().hear(type, encounter);
				} catch (RuntimeException e) {
					errors.errorNotifyingTypeListener(typeListener, type, e);
				}
			}
		}
		encounter.invalidate();
		errors.throwIfNewErrors(numErrorsBefore);

		return new MembersInjectorImpl<T>(injector, type, encounter, injectors);
	}

	
	/**
	 * Gets the injectors.
	 *
	 * @param injectionPoints the injection points
	 * @param errors the errors
	 * @return the injectors
	 */
	ImmutableList<SingleMemberInjector> getInjectors(Set<InjectionPoint> injectionPoints, Errors errors) {
		List<SingleMemberInjector> injectors = Lists.newArrayList();
		for (InjectionPoint injectionPoint : injectionPoints) {
			try {
				Errors errorsForMember = injectionPoint.isOptional() ? new Errors(injectionPoint) : errors
						.withSource(injectionPoint);
				SingleMemberInjector injector = injectionPoint.getMember() instanceof Field ? new SingleFieldInjector(
						this.injector, injectionPoint, errorsForMember) : new SingleMethodInjector(this.injector,
						injectionPoint, errorsForMember);
				injectors.add(injector);
			} catch (ErrorsException ignoredForNow) {
				
			}
		}
		return ImmutableList.copyOf(injectors);
	}
}
