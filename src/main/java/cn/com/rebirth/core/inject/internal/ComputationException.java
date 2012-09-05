/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ComputationException.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;


/**
 * The Class ComputationException.
 *
 * @author l.xue.nong
 */
public class ComputationException extends RuntimeException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5940202996824774701L;

	
	/**
	 * Instantiates a new computation exception.
	 *
	 * @param cause the cause
	 */
	public ComputationException(Throwable cause) {
		super(cause);
	}
}
