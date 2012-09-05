/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ExposedBinding.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Binding;


/**
 * The Interface ExposedBinding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ExposedBinding<T> extends Binding<T>, HasDependencies {

	
	/**
	 * Gets the private elements.
	 *
	 * @return the private elements
	 */
	PrivateElements getPrivateElements();

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	void applyTo(Binder binder);
}