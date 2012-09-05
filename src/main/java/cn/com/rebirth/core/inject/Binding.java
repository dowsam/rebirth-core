/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Binding.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.spi.BindingScopingVisitor;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.Element;


/**
 * The Interface Binding.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface Binding<T> extends Element {

	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	Key<T> getKey();

	
	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	Provider<T> getProvider();

	
	/**
	 * Accept target visitor.
	 *
	 * @param <V> the value type
	 * @param visitor the visitor
	 * @return the v
	 */
	<V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor);

	
	/**
	 * Accept scoping visitor.
	 *
	 * @param <V> the value type
	 * @param visitor the visitor
	 * @return the v
	 */
	<V> V acceptScopingVisitor(BindingScopingVisitor<V> visitor);
}
