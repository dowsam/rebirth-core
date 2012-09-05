/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderKeyBinding.java 2012-7-6 10:23:40 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Interface ProviderKeyBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ProviderKeyBinding<T> extends Binding<T> {

	
	/**
	 * Gets the provider key.
	 *
	 * @return the provider key
	 */
	Key<? extends Provider<? extends T>> getProviderKey();

}