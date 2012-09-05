/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Module.java 2012-7-6 10:23:43 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface Module.
 *
 * @author l.xue.nong
 */
public interface Module {

	
	/**
	 * Configure.
	 *
	 * @param binder the binder
	 */
	void configure(Binder binder);
}
