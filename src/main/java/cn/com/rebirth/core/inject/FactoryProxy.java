/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FactoryProxy.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.ToStringBuilder;
import cn.com.rebirth.core.inject.spi.Dependency;


/**
 * The Class FactoryProxy.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class FactoryProxy<T> implements InternalFactory<T>, BindingProcessor.CreationListener {

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The key. */
	private final Key<T> key;

	
	/** The target key. */
	private final Key<? extends T> targetKey;

	
	/** The source. */
	private final Object source;

	
	/** The target factory. */
	private InternalFactory<? extends T> targetFactory;

	
	/**
	 * Instantiates a new factory proxy.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param targetKey the target key
	 * @param source the source
	 */
	FactoryProxy(InjectorImpl injector, Key<T> key, Key<? extends T> targetKey, Object source) {
		this.injector = injector;
		this.key = key;
		this.targetKey = targetKey;
		this.source = source;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.BindingProcessor.CreationListener#notify(cn.com.rebirth.search.commons.inject.internal.Errors)
	 */
	public void notify(final Errors errors) {
		try {
			targetFactory = injector.getInternalFactory(targetKey, errors.withSource(source));
		} catch (ErrorsException e) {
			errors.merge(e.getErrors());
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
	 */
	public T get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException {
		return targetFactory.get(errors.withSource(targetKey), context, dependency);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(FactoryProxy.class).add("key", key).add("provider", targetFactory).toString();
	}
}
