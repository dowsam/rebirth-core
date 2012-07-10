/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core IResponder.java 2012-7-10 14:01:14 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

/**
 * The Interface IResponder.
 *
 * @author l.xue.nong
 */
public interface Responder {

	/**
	 * On result.
	 *
	 * @param <T> the generic type
	 * @param result the result
	 */
	public <T> void onResult(T result);

	/**
	 * On fault.
	 *
	 * @param fault the fault
	 */
	public void onFault(Exception fault);
}
