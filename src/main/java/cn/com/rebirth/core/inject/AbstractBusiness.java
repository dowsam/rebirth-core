/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core AbstractBusiness.java 2012-8-14 11:30:05 l.xue.nong$$
 */
package cn.com.rebirth.core.inject;

/**
 * The Class AbstractBusiness.
 *
 * @author l.xue.nong
 */
public abstract class AbstractBusiness implements Business {
	
	/**
	 * To after configure.
	 *
	 * @param injector the injector
	 */
	protected abstract void toAfterConfigure(Injector injector);
}
