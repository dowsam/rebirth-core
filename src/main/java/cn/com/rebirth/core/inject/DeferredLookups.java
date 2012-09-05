/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons DeferredLookups.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.util.List;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.MembersInjectorLookup;
import cn.com.rebirth.core.inject.spi.ProviderLookup;

import com.google.common.collect.Lists;


/**
 * The Class DeferredLookups.
 *
 * @author l.xue.nong
 */
class DeferredLookups implements Lookups {

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The lookups. */
	private final List<Element> lookups = Lists.newArrayList();

	
	/**
	 * Instantiates a new deferred lookups.
	 *
	 * @param injector the injector
	 */
	public DeferredLookups(InjectorImpl injector) {
		this.injector = injector;
	}

	
	/**
	 * Initialize.
	 *
	 * @param errors the errors
	 */
	public void initialize(Errors errors) {
		injector.lookups = injector;
		new LookupProcessor(errors).process(injector, lookups);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Lookups#getProvider(cn.com.rebirth.search.commons.inject.Key)
	 */
	public <T> Provider<T> getProvider(Key<T> key) {
		ProviderLookup<T> lookup = new ProviderLookup<T>(key, key);
		lookups.add(lookup);
		return lookup.getProvider();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Lookups#getMembersInjector(cn.com.rebirth.search.commons.inject.TypeLiteral)
	 */
	public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type) {
		MembersInjectorLookup<T> lookup = new MembersInjectorLookup<T>(type, type);
		lookups.add(lookup);
		return lookup.getMembersInjector();
	}
}
