/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ErrorHandler.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import cn.com.rebirth.core.inject.spi.Message;


/**
 * The Interface ErrorHandler.
 *
 * @author l.xue.nong
 */
public interface ErrorHandler {

	
	/**
	 * Handle.
	 *
	 * @param source the source
	 * @param errors the errors
	 */
	void handle(Object source, Errors errors);

	
	/**
	 * Handle.
	 *
	 * @param message the message
	 */
	void handle(Message message);
}
