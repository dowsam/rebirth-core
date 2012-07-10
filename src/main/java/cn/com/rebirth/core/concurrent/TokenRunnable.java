/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AsyncTokenRunnable.java 2012-2-2 17:29:08 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

import java.util.concurrent.Callable;

/**
 * 异步处理,线程类.
 *
 * @param <T> the generic type
 * @see java.lang.Runnable
 * @author Administrator
 */
public class TokenRunnable<T> implements Runnable {

	/** The async token. */
	Token<T> asyncToken;

	/** The target runnable. */
	Runnable targetRunnable;

	/** The target callable. */
	Callable<T> targetCallable;

	/**
	 * Instantiates a new async token runnable.
	 *
	 * @param asyncToken the async token
	 * @param target the target
	 */
	public TokenRunnable(Token<T> asyncToken, Runnable target) {
		if (asyncToken == null)
			throw new IllegalArgumentException("asyncToken must be not null");
		if (target == null)
			throw new IllegalArgumentException("target Runnable must be not null");
		this.asyncToken = asyncToken;
		this.targetRunnable = target;
	}

	/**
	 * Instantiates a new async token runnable.
	 *
	 * @param asyncToken the async token
	 * @param target the target
	 */
	public TokenRunnable(Token<T> asyncToken, Callable<T> target) {
		if (asyncToken == null)
			throw new IllegalArgumentException("asyncToken must be not null");
		if (target == null)
			throw new IllegalArgumentException("target Callable must be not null");
		this.asyncToken = asyncToken;
		this.targetCallable = target;
	}

	/**
	 * Gets the async token.
	 *
	 * @return the async token
	 */
	public Token<T> getAsyncToken() {
		return asyncToken;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			if (targetRunnable == null) {
				asyncToken.setComplete(targetCallable.call());
			} else {
				targetRunnable.run();
				asyncToken.setComplete();
			}
		} catch (Exception e) {
			asyncToken.setFault(e);
		}
	}
}
