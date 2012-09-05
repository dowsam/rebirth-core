/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BoundProviderFactory.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.BindingProcessor.CreationListener;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.spi.Dependency;


/**
 * A factory for creating BoundProvider objects.
 *
 * @param <T> the generic type
 */
class BoundProviderFactory<T> implements InternalFactory<T>, CreationListener {

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The provider key. */
	final Key<? extends Provider<? extends T>> providerKey;

	
	/** The source. */
	final Object source;

	
	/** The provider factory. */
	private InternalFactory<? extends Provider<? extends T>> providerFactory;

	
	/**
	 * Instantiates a new bound provider factory.
	 *
	 * @param injector the injector
	 * @param providerKey the provider key
	 * @param source the source
	 */
	BoundProviderFactory(InjectorImpl injector, Key<? extends Provider<? extends T>> providerKey, Object source) {
		this.injector = injector;
		this.providerKey = providerKey;
		this.source = source;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.BindingProcessor.CreationListener#notify(cn.com.rebirth.search.commons.inject.internal.Errors)
	 */
	public void notify(Errors errors) {
		try {
			providerFactory = injector.getInternalFactory(providerKey, errors.withSource(source));
		} catch (ErrorsException e) {
			errors.merge(e.getErrors());
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
	 */
	public T get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException {
		errors = errors.withSource(providerKey);
		Provider<? extends T> provider = providerFactory.get(errors, context, dependency);
		try {
			return errors.checkForNull(provider.get(), source, dependency);
		} catch (RuntimeException userException) {
			throw errors.errorInProvider(userException).toException();
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return providerKey.toString();
	}
}
