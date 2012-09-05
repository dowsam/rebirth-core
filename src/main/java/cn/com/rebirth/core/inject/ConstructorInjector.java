/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructorInjector.java 2012-7-6 10:23:40 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.InvocationTargetException;

import cn.com.rebirth.core.inject.internal.ConstructionContext;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.spi.InjectionPoint;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ConstructorInjector.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class ConstructorInjector<T> {

	
	/** The injectable members. */
	private final ImmutableSet<InjectionPoint> injectableMembers;

	
	/** The parameter injectors. */
	private final SingleParameterInjector<?>[] parameterInjectors;

	
	/** The construction proxy. */
	private final ConstructionProxy<T> constructionProxy;

	
	/** The members injector. */
	private final MembersInjectorImpl<T> membersInjector;

	
	/**
	 * Instantiates a new constructor injector.
	 *
	 * @param injectableMembers the injectable members
	 * @param constructionProxy the construction proxy
	 * @param parameterInjectors the parameter injectors
	 * @param membersInjector the members injector
	 * @throws ErrorsException the errors exception
	 */
	ConstructorInjector(ImmutableSet<InjectionPoint> injectableMembers, ConstructionProxy<T> constructionProxy,
			SingleParameterInjector<?>[] parameterInjectors, MembersInjectorImpl<T> membersInjector)
			throws ErrorsException {
		this.injectableMembers = injectableMembers;
		this.constructionProxy = constructionProxy;
		this.parameterInjectors = parameterInjectors;
		this.membersInjector = membersInjector;
	}

	
	/**
	 * Gets the injectable members.
	 *
	 * @return the injectable members
	 */
	public ImmutableSet<InjectionPoint> getInjectableMembers() {
		return injectableMembers;
	}

	
	/**
	 * Gets the construction proxy.
	 *
	 * @return the construction proxy
	 */
	ConstructionProxy<T> getConstructionProxy() {
		return constructionProxy;
	}

	
	/**
	 * Construct.
	 *
	 * @param errors the errors
	 * @param context the context
	 * @param expectedType the expected type
	 * @return the object
	 * @throws ErrorsException the errors exception
	 */
	Object construct(Errors errors, InternalContext context, Class<?> expectedType) throws ErrorsException {
		ConstructionContext<T> constructionContext = context.getConstructionContext(this);

		
		if (constructionContext.isConstructing()) {
			
			return constructionContext.createProxy(errors, expectedType);
		}

		
		
		T t = constructionContext.getCurrentReference();
		if (t != null) {
			return t;
		}

		try {
			
			constructionContext.startConstruction();
			try {
				Object[] parameters = SingleParameterInjector.getAll(errors, context, parameterInjectors);
				t = constructionProxy.newInstance(parameters);
				constructionContext.setProxyDelegates(t);
			} finally {
				constructionContext.finishConstruction();
			}

			
			constructionContext.setCurrentReference(t);

			membersInjector.injectMembers(t, errors, context);
			membersInjector.notifyListeners(t, errors);

			return t;
		} catch (InvocationTargetException userException) {
			Throwable cause = userException.getCause() != null ? userException.getCause() : userException;
			throw errors.withSource(constructionProxy.getInjectionPoint()).errorInjectingConstructor(cause)
					.toException();
		} finally {
			constructionContext.removeCurrentReference();
		}
	}
}
