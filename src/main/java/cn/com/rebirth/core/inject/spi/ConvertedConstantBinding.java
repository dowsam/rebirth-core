/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConvertedConstantBinding.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.util.Set;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;


/**
 * The Interface ConvertedConstantBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ConvertedConstantBinding<T> extends Binding<T>, HasDependencies {

	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	T getValue();

	
	/**
	 * Gets the source key.
	 *
	 * @return the source key
	 */
	Key<String> getSourceKey();

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.HasDependencies#getDependencies()
	 */
	Set<Dependency<?>> getDependencies();
}