package cn.com.rebirth.core.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import cn.com.rebirth.core.concurrent.AsyncFactory;
import cn.com.rebirth.core.concurrent.Token;
import cn.com.rebirth.core.concurrent.TokenRunnable;

/**
 * The Class TokenUtils.
 *
 * @author l.xue.nong
 */
public abstract class TokenUtils {

	/**
	 * Execute.
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
	 * Execute.
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
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param factory the factory
	 * @param task the task
	 * @return the token
	 */
	public static <T> Token<T> execute(Executor executor, AsyncFactory factory, final Callable<T> task) {
		Token<T> token = factory.newToken();
		executor.execute(new TokenRunnable<T>(token, task));
		return token;
	}

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param executor the executor
	 * @param factory the factory
	 * @param task the task
	 * @return the token
	 */
	public static <T> Token<T> execute(Executor executor, AsyncFactory factory, final Runnable task) {
		Token<T> token = factory.newToken();
		executor.execute(new TokenRunnable<T>(token, task));
		return token;
	}
}
