/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SingleMethodInjector.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.com.rebirth.core.inject.InjectorImpl.MethodInvoker;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.spi.InjectionPoint;


/**
 * The Class SingleMethodInjector.
 *
 * @author l.xue.nong
 */
class SingleMethodInjector implements SingleMemberInjector {

	
	/** The method invoker. */
	final MethodInvoker methodInvoker;

	
	/** The parameter injectors. */
	final SingleParameterInjector<?>[] parameterInjectors;

	
	/** The injection point. */
	final InjectionPoint injectionPoint;

	
	/**
	 * Instantiates a new single method injector.
	 *
	 * @param injector the injector
	 * @param injectionPoint the injection point
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	public SingleMethodInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors)
			throws ErrorsException {
		this.injectionPoint = injectionPoint;
		final Method method = (Method) injectionPoint.getMember();
		methodInvoker = createMethodInvoker(method);
		parameterInjectors = injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
	}

	
	/**
	 * Creates the method invoker.
	 *
	 * @param method the method
	 * @return the method invoker
	 */
	private MethodInvoker createMethodInvoker(final Method method) {

		
		int modifiers = method.getModifiers();
		if (!Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers)) {
		}

		if (!Modifier.isPublic(modifiers)) {
			method.setAccessible(true);
		}

		return new MethodInvoker() {
			public Object invoke(Object target, Object... parameters) throws IllegalAccessException,
					InvocationTargetException {
				return method.invoke(target, parameters);
			}
		};
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.SingleMemberInjector#getInjectionPoint()
	 */
	public InjectionPoint getInjectionPoint() {
		return injectionPoint;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.SingleMemberInjector#inject(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, java.lang.Object)
	 */
	public void inject(Errors errors, InternalContext context, Object o) {
		Object[] parameters;
		try {
			parameters = SingleParameterInjector.getAll(errors, context, parameterInjectors);
		} catch (ErrorsException e) {
			errors.merge(e.getErrors());
			return;
		}

		try {
			methodInvoker.invoke(o, parameters);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e); 
		} catch (InvocationTargetException userException) {
			Throwable cause = userException.getCause() != null ? userException.getCause() : userException;
			errors.withSource(injectionPoint).errorInjectingMethod(cause);
		}
	}
}
