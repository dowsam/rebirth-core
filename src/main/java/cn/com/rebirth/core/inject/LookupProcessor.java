/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LookupProcessor.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.spi.MembersInjectorLookup;
import cn.com.rebirth.core.inject.spi.ProviderLookup;


/**
 * The Class LookupProcessor.
 *
 * @author l.xue.nong
 */
class LookupProcessor extends AbstractProcessor {

	
	/**
	 * Instantiates a new lookup processor.
	 *
	 * @param errors the errors
	 */
	LookupProcessor(Errors errors) {
		super(errors);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.MembersInjectorLookup)
	 */
	@Override
	public <T> Boolean visit(MembersInjectorLookup<T> lookup) {
		try {
			MembersInjector<T> membersInjector = injector.membersInjectorStore.get(lookup.getType(), errors);
			lookup.initializeDelegate(membersInjector);
		} catch (ErrorsException e) {
			errors.merge(e.getErrors()); 
		}

		return true;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.ProviderLookup)
	 */
	@Override
	public <T> Boolean visit(ProviderLookup<T> lookup) {
		
		try {
			Provider<T> provider = injector.getProviderOrThrow(lookup.getKey(), errors);
			lookup.initializeDelegate(provider);
		} catch (ErrorsException e) {
			errors.merge(e.getErrors()); 
		}

		return true;
	}
}
