/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LinkedKeyBinding.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;


/**
 * The Interface LinkedKeyBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface LinkedKeyBinding<T> extends Binding<T> {

	
	/**
	 * Gets the linked key.
	 *
	 * @return the linked key
	 */
	Key<? extends T> getLinkedKey();

}