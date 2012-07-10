/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core UncaughtExceptionHandler.java 2012-2-2 17:30:03 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

/**
 * 异步回调接口,处理异常接口.
 *
 * @author Administrator
 */
public interface UncaughtExceptionHandler {

	/**
	 * 处理异常.
	 *
	 * @param responder the responder
	 * @param e the e
	 * @see cn.com.Responder.concurrent.IResponder
	 */
	void uncaughtException(Responder responder, Throwable e);
}
