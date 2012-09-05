/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons NullOutputException.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;


/**
 * The Class NullOutputException.
 *
 * @author l.xue.nong
 */
class NullOutputException extends NullPointerException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2032226431980380996L;

	
	/**
	 * Instantiates a new null output exception.
	 *
	 * @param s the s
	 */
	public NullOutputException(String s) {
		super(s);
	}
}
