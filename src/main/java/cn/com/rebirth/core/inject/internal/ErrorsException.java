/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ErrorsException.java 2012-7-6 10:23:54 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;


/**
 * The Class ErrorsException.
 *
 * @author l.xue.nong
 */
public class ErrorsException extends Exception {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3181514182676999879L;
	
	
	/** The errors. */
	private final Errors errors;

	
	/**
	 * Instantiates a new errors exception.
	 *
	 * @param errors the errors
	 */
	public ErrorsException(Errors errors) {
		this.errors = errors;
	}

	
	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public Errors getErrors() {
		return errors;
	}
}
