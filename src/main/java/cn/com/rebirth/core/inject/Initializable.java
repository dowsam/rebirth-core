/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Initializable.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;


/**
 * The Interface Initializable.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
interface Initializable<T> {

	
	/**
	 * Gets the.
	 *
	 * @param errors the errors
	 * @return the t
	 * @throws ErrorsException the errors exception
	 */
	T get(Errors errors) throws ErrorsException;
}
