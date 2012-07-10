/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AsyncTokenFactory.java 2012-2-2 17:28:59 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

/**
 * 异步监听,工厂类,具体生产监听器.
 *
 * @author l.xue.nong
 */
public interface AsyncFactory {

	/**
	 * 生产监听器.
	 *
	 * @param <T> the generic type
	 * @return the async token
	 */
	public <T> Token<T> newToken();
}
