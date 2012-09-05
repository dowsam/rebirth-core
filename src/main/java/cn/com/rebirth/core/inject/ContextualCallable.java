/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ContextualCallable.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;


/**
 * The Interface ContextualCallable.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
interface ContextualCallable<T> {

	
	/**
	 * Call.
	 *
	 * @param context the context
	 * @return the t
	 * @throws ErrorsException the errors exception
	 */
	T call(InternalContext context) throws ErrorsException;
}
