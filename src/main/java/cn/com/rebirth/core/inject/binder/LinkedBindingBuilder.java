/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LinkedBindingBuilder.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.binder;

import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.TypeLiteral;


/**
 * The Interface LinkedBindingBuilder.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {

	
	/**
	 * To.
	 *
	 * @param implementation the implementation
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder to(Class<? extends T> implementation);

	
	/**
	 * To.
	 *
	 * @param implementation the implementation
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder to(TypeLiteral<? extends T> implementation);

	
	/**
	 * To.
	 *
	 * @param targetKey the target key
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder to(Key<? extends T> targetKey);

	
	/**
	 * To instance.
	 *
	 * @param instance the instance
	 */
	void toInstance(T instance);

	
	/**
	 * To provider.
	 *
	 * @param provider the provider
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder toProvider(Provider<? extends T> provider);

	
	/**
	 * To provider.
	 *
	 * @param providerType the provider type
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> providerType);

	
	/**
	 * To provider.
	 *
	 * @param providerKey the provider key
	 * @return the scoped binding builder
	 */
	ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> providerKey);
}
