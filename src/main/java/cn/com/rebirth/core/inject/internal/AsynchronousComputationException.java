/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AsynchronousComputationException.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;


/**
 * The Class AsynchronousComputationException.
 *
 * @author l.xue.nong
 */
public class AsynchronousComputationException extends ComputationException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4511195185616179160L;

	
	/**
	 * Instantiates a new asynchronous computation exception.
	 *
	 * @param cause the cause
	 */
	public AsynchronousComputationException(Throwable cause) {
		super(cause);
	}
}
