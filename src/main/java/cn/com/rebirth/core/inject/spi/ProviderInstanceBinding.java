/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderInstanceBinding.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.util.Set;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Interface ProviderInstanceBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ProviderInstanceBinding<T> extends Binding<T>, HasDependencies {

	
	/**
	 * Gets the provider instance.
	 *
	 * @return the provider instance
	 */
	Provider<? extends T> getProviderInstance();

	
	/**
	 * Gets the injection points.
	 *
	 * @return the injection points
	 */
	Set<InjectionPoint> getInjectionPoints();

}