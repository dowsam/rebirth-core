/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InstanceBinding.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.util.Set;

import cn.com.rebirth.core.inject.Binding;


/**
 * The Interface InstanceBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface InstanceBinding<T> extends Binding<T>, HasDependencies {

	
	/**
	 * Gets the single instance of InstanceBinding.
	 *
	 * @return single instance of InstanceBinding
	 */
	T getInstance();

	
	/**
	 * Gets the injection points.
	 *
	 * @return the injection points
	 */
	Set<InjectionPoint> getInjectionPoints();

}
