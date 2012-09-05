/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeListenerBindingProcessor.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;


/**
 * The Class TypeListenerBindingProcessor.
 *
 * @author l.xue.nong
 */
class TypeListenerBindingProcessor extends AbstractProcessor {

	
	/**
	 * Instantiates a new type listener binding processor.
	 *
	 * @param errors the errors
	 */
	TypeListenerBindingProcessor(Errors errors) {
		super(errors);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.TypeListenerBinding)
	 */
	@Override
	public Boolean visit(TypeListenerBinding binding) {
		injector.state.addTypeListener(binding);
		return true;
	}
}