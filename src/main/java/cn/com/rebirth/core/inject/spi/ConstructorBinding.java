/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructorBinding.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.util.Set;

import cn.com.rebirth.core.inject.Binding;


/**
 * The Interface ConstructorBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ConstructorBinding<T> extends Binding<T>, HasDependencies {

	
	/**
	 * Gets the constructor.
	 *
	 * @return the constructor
	 */
	InjectionPoint getConstructor();

	
	/**
	 * Gets the injectable members.
	 *
	 * @return the injectable members
	 */
	Set<InjectionPoint> getInjectableMembers();
}