/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InternalFactory.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import cn.com.rebirth.core.inject.spi.Dependency;


/**
 * A factory for creating Internal objects.
 *
 * @param <T> the generic type
 */
public interface InternalFactory<T> {

	
	/**
	 * Gets the.
	 *
	 * @param errors the errors
	 * @param context the context
	 * @param dependency the dependency
	 * @return the t
	 * @throws ErrorsException the errors exception
	 */
	T get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException;
}
