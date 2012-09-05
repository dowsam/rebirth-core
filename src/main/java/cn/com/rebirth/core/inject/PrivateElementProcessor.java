/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PrivateElementProcessor.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.util.List;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.PrivateElements;

import com.google.common.collect.Lists;


/**
 * The Class PrivateElementProcessor.
 *
 * @author l.xue.nong
 */
class PrivateElementProcessor extends AbstractProcessor {

	
	/** The stage. */
	private final Stage stage;

	
	/** The injector shell builders. */
	private final List<InjectorShell.Builder> injectorShellBuilders = Lists.newArrayList();

	
	/**
	 * Instantiates a new private element processor.
	 *
	 * @param errors the errors
	 * @param stage the stage
	 */
	PrivateElementProcessor(Errors errors, Stage stage) {
		super(errors);
		this.stage = stage;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.PrivateElements)
	 */
	@Override
	public Boolean visit(PrivateElements privateElements) {
		InjectorShell.Builder builder = new InjectorShell.Builder().parent(injector).stage(stage)
				.privateElements(privateElements);
		injectorShellBuilders.add(builder);
		return true;
	}

	
	/**
	 * Gets the injector shell builders.
	 *
	 * @return the injector shell builders
	 */
	public List<InjectorShell.Builder> getInjectorShellBuilders() {
		return injectorShellBuilders;
	}
}
