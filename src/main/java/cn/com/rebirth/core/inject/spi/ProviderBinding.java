/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderBinding.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Interface ProviderBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ProviderBinding<T extends Provider<?>> extends Binding<T> {

	
	/**
	 * Gets the provided key.
	 *
	 * @return the provided key
	 */
	Key<?> getProvidedKey();
}