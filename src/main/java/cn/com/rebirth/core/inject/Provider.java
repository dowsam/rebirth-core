/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Provider.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface Provider.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface Provider<T> {

	
	/**
	 * Gets the.
	 *
	 * @return the t
	 */
	T get();
}
