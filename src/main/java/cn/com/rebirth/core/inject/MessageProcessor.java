/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MessageProcessor.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Message;


/**
 * The Class MessageProcessor.
 *
 * @author l.xue.nong
 */
class MessageProcessor extends AbstractProcessor {

	

	
	/**
	 * Instantiates a new message processor.
	 *
	 * @param errors the errors
	 */
	MessageProcessor(Errors errors) {
		super(errors);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.Message)
	 */
	@Override
	public Boolean visit(Message message) {
		
		
		
		
		
		
		

		errors.addMessage(message);
		return true;
	}

	
	/**
	 * Gets the root message.
	 *
	 * @param t the t
	 * @return the root message
	 */
	public static String getRootMessage(Throwable t) {
		Throwable cause = t.getCause();
		return cause == null ? t.toString() : getRootMessage(cause);
	}
}
