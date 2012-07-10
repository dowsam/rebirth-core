/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AsynServiceHandle.java 2012-3-16 12:08:37 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The Interface AsynServiceHandle.
 *
 * @author l.xue.nong
 */
public interface AsynServiceHandle {

	/**
	 * Submit.
	 *
	 * @param <T> the generic type
	 * @param callable the callable
	 * @return the future
	 */
	public abstract <T> Future<T> submit(Callable<T> callable);

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param token the token
	 * @param task the task
	 */
	public abstract <T> void execute(final Token<T> token, final Callable<T> task);

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param token the token
	 * @param task the task
	 */
	public abstract <T> void execute(final Token<T> token, final Runnable task);

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param task the task
	 * @return the token
	 */
	public abstract <T> Token<T> execute(final Callable<T> task);

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param task the task
	 * @return the token
	 */
	public abstract <T> Token<T> execute(final Runnable task);

}