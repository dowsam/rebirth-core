/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Initializables.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;


/**
 * The Class Initializables.
 *
 * @author l.xue.nong
 */
class Initializables {

	
	/**
	 * Of.
	 *
	 * @param <T> the generic type
	 * @param instance the instance
	 * @return the initializable
	 */
	static <T> Initializable<T> of(final T instance) {
		return new Initializable<T>() {
			public T get(Errors errors) throws ErrorsException {
				return instance;
			}

			@Override
			public String toString() {
				return String.valueOf(instance);
			}
		};
	}
}
