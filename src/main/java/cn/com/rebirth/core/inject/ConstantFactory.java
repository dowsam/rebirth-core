/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstantFactory.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.ToStringBuilder;
import cn.com.rebirth.core.inject.spi.Dependency;


/**
 * A factory for creating Constant objects.
 *
 * @param <T> the generic type
 */
class ConstantFactory<T> implements InternalFactory<T> {

	
	/** The initializable. */
	private final Initializable<T> initializable;

	
	/**
	 * Instantiates a new constant factory.
	 *
	 * @param initializable the initializable
	 */
	public ConstantFactory(Initializable<T> initializable) {
		this.initializable = initializable;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
	 */
	@SuppressWarnings("rawtypes")
	public T get(Errors errors, InternalContext context, Dependency dependency) throws ErrorsException {
		return initializable.get(errors);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(ConstantFactory.class).add("value", initializable).toString();
	}
}
