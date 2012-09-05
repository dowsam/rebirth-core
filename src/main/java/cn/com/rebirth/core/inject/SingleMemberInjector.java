/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SingleMemberInjector.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.spi.InjectionPoint;


/**
 * The Interface SingleMemberInjector.
 *
 * @author l.xue.nong
 */
interface SingleMemberInjector {

	
	/**
	 * Inject.
	 *
	 * @param errors the errors
	 * @param context the context
	 * @param o the o
	 */
	void inject(Errors errors, InternalContext context, Object o);

	
	/**
	 * Gets the injection point.
	 *
	 * @return the injection point
	 */
	InjectionPoint getInjectionPoint();
}
