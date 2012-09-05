/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SingleFieldInjector.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.Field;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.InjectionPoint;


/**
 * The Class SingleFieldInjector.
 *
 * @author l.xue.nong
 */
class SingleFieldInjector implements SingleMemberInjector {

	
	/** The field. */
	final Field field;

	
	/** The injection point. */
	final InjectionPoint injectionPoint;

	
	/** The dependency. */
	final Dependency<?> dependency;

	
	/** The factory. */
	final InternalFactory<?> factory;

	
	/**
	 * Instantiates a new single field injector.
	 *
	 * @param injector the injector
	 * @param injectionPoint the injection point
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	public SingleFieldInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors)
			throws ErrorsException {
		this.injectionPoint = injectionPoint;
		this.field = (Field) injectionPoint.getMember();
		this.dependency = injectionPoint.getDependencies().get(0);

		
		field.setAccessible(true);
		factory = injector.getInternalFactory(dependency.getKey(), errors);
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
		errors = errors.withSource(dependency);

		context.setDependency(dependency);
		try {
			Object value = factory.get(errors, context, dependency);
			field.set(o, value);
		} catch (ErrorsException e) {
			errors.withSource(injectionPoint).merge(e.getErrors());
		} catch (IllegalAccessException e) {
			throw new AssertionError(e); 
		} finally {
			context.setDependency(null);
		}
	}
}
