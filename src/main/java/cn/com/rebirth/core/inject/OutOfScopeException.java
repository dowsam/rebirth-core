/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons OutOfScopeException.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Class OutOfScopeException.
 *
 * @author l.xue.nong
 */
public final class OutOfScopeException extends RuntimeException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8175088809011346667L;

	
	/**
	 * Instantiates a new out of scope exception.
	 *
	 * @param message the message
	 */
	public OutOfScopeException(String message) {
		super(message);
	}

	
	/**
	 * Instantiates a new out of scope exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public OutOfScopeException(String message, Throwable cause) {
		super(message, cause);
	}

	
	/**
	 * Instantiates a new out of scope exception.
	 *
	 * @param cause the cause
	 */
	public OutOfScopeException(Throwable cause) {
		super(cause);
	}
}
