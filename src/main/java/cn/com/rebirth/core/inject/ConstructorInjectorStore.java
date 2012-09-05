/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructorInjectorStore.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.FailableCache;
import cn.com.rebirth.core.inject.spi.InjectionPoint;


/**
 * The Class ConstructorInjectorStore.
 *
 * @author l.xue.nong
 */
class ConstructorInjectorStore {

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The cache. */
	private final FailableCache<TypeLiteral<?>, ConstructorInjector<?>> cache = new FailableCache<TypeLiteral<?>, ConstructorInjector<?>>() {
		protected ConstructorInjector<?> create(TypeLiteral<?> type, Errors errors) throws ErrorsException {
			return createConstructor(type, errors);
		}
	};

	
	/**
	 * Instantiates a new constructor injector store.
	 *
	 * @param injector the injector
	 */
	ConstructorInjectorStore(InjectorImpl injector) {
		this.injector = injector;
	}

	
	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param errors the errors
	 * @return the constructor injector
	 * @throws ErrorsException the errors exception
	 */
	@SuppressWarnings("unchecked")
	
	public <T> ConstructorInjector<T> get(TypeLiteral<T> key, Errors errors) throws ErrorsException {
		return (ConstructorInjector<T>) cache.get(key, errors);
	}

	
	/**
	 * Creates the constructor.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param errors the errors
	 * @return the constructor injector
	 * @throws ErrorsException the errors exception
	 */
	private <T> ConstructorInjector<T> createConstructor(TypeLiteral<T> type, Errors errors) throws ErrorsException {
		int numErrorsBefore = errors.size();

		InjectionPoint injectionPoint;
		try {
			injectionPoint = InjectionPoint.forConstructorOf(type);
		} catch (ConfigurationException e) {
			errors.merge(e.getErrorMessages());
			throw errors.toException();
		}

		SingleParameterInjector<?>[] constructorParameterInjectors = injector.getParametersInjectors(
				injectionPoint.getDependencies(), errors);
		MembersInjectorImpl<T> membersInjector = injector.membersInjectorStore.get(type, errors);

		ConstructionProxyFactory<T> factory = new DefaultConstructionProxyFactory<T>(injectionPoint);

		errors.throwIfNewErrors(numErrorsBefore);

		return new ConstructorInjector<T>(membersInjector.getInjectionPoints(), factory.create(),
				constructorParameterInjectors, membersInjector);
	}
}
