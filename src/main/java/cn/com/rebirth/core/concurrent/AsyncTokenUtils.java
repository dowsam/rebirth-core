/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AsyncTokenUtils.java 2012-2-2 17:29:16 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * 提供工具类,主要产生AsyncToken.
 *
 * @author Administrator
 */
public class AsyncTokenUtils {

	/**
	 * 通过executor执行Callable,将callable的执行结果设置进token.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param token the token
	 * @param task the task
	 */
	public static <T> void execute(Executor executor, Token<T> token, final Callable<T> task) {
		executor.execute(new TokenRunnable<T>(token, task));
	}

	/**
	 * 通过executor执行Runnable.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param token the token
	 * @param task the task
	 */
	public static <T> void execute(Executor executor, Token<T> token, final Runnable task) {
		executor.execute(new TokenRunnable<T>(token, task));
	}

	/**
	 * 通过executor执行Callable,将callable的执行结果设置进token.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param factory the factory
	 * @param task the task
	 * @return the async token
	 */
	public static <T> Token<T> execute(Executor executor, AsyncFactory factory, final Callable<T> task) {
		Token<T> token = factory.newToken();
		executor.execute(new TokenRunnable<T>(token, task));
		return token;
	}

	/**
	 * 通过executor执行Runnable.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param factory the factory
	 * @param task the task
	 * @return the async token
	 */
	public static <T> Token<T> execute(Executor executor, AsyncFactory factory, final Runnable task) {
		Token<T> token = factory.newToken();
		executor.execute(new TokenRunnable<T>(token, task));
		return token;
	}
}
