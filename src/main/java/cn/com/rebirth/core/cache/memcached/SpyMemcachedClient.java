/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core SpyMemcachedClient.java 2012-2-3 10:24:31 l.xue.nong$$
 */
package cn.com.rebirth.core.cache.memcached;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * 对SpyMemcached Client的二次封装,提供常用的Get/GetBulk/Set/Delete/Incr/Decr函数的封装.
 * 
 * 未提供封装的函数可直接调用getClient()取出Spy的原版MemcachedClient来使用.
 *
 * @author l.xue.nong
 */
public class SpyMemcachedClient implements DisposableBean {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SpyMemcachedClient.class);

	/** The memcached client. */
	private MemcachedClient memcachedClient;

	/** The shutdown timeout. */
	private long shutdownTimeout = 1000;

	/**
	 * Get方法, 转换结果类型并屏蔽异常, 仅返回Null.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		try {
			return (T) memcachedClient.get(key);
		} catch (RuntimeException e) {
			handleException(e, key);
			return null;
		}
	}

	/**
	 * GetBulk方法, 转换结果类型并屏蔽异常.
	 *
	 * @param <T> the generic type
	 * @param keys the keys
	 * @return the bulk
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getBulk(Collection<String> keys) {
		try {
			return (Map<String, T>) memcachedClient.getBulk(keys);
		} catch (RuntimeException e) {
			handleException(e, StringUtils.join(keys, ","));
			return null;
		}
	}

	/**
	 * 异步Set方法, 不考虑执行结果.
	 *
	 * @param key the key
	 * @param expiredTime the expired time
	 * @param value the value
	 */
	public void set(String key, int expiredTime, Object value) {
		memcachedClient.set(key, expiredTime, value);
	}

	/**
	 * 安全的Set方法,在1秒内返回结果, 否则返回false并取消操作.
	 *
	 * @param key the key
	 * @param value the value
	 * @param expiration the expiration
	 * @return true, if successful
	 */
	public boolean safeSet(String key, Object value, int expiration) {
		Future<Boolean> future = memcachedClient.set(key, expiration, value);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * 异步 Delete方法, 不考虑执行结果.
	 *
	 * @param key the key
	 */
	public void delete(String key) {
		memcachedClient.delete(key);
	}

	/**
	 * 安全的Set方法,在1秒内返回结果, 否则返回false并取消操作.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean safeDelete(String key) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * Incr方法.
	 *
	 * @param key the key
	 * @param by the by
	 * @param defaultValue the default value
	 * @return the long
	 */
	public long incr(String key, int by, long defaultValue) {
		return memcachedClient.incr(key, by, defaultValue);
	}

	/**
	 * Decr方法.
	 *
	 * @param key the key
	 * @param by the by
	 * @param defaultValue the default value
	 * @return the long
	 */
	public long decr(String key, int by, long defaultValue) {
		return memcachedClient.decr(key, by, defaultValue);
	}

	/**
	 * 异步Incr方法, 不支持默认值, 若key不存在返回-1.
	 *
	 * @param key the key
	 * @param by the by
	 * @return the future
	 */
	public Future<Long> asyncIncr(String key, int by) {
		return memcachedClient.asyncIncr(key, by);
	}

	/**
	 * 异步Decr方法, 不支持默认值, 若key不存在返回-1.
	 *
	 * @param key the key
	 * @param by the by
	 * @return the future
	 */
	public Future<Long> asyncDecr(String key, int by) {
		return memcachedClient.asyncDecr(key, by);
	}

	/**
	 * Handle exception.
	 *
	 * @param e the e
	 * @param key the key
	 * @return the runtime exception
	 */
	private RuntimeException handleException(Exception e, String key) {
		logger.warn("spymemcached client receive an exception with key:" + key, e);
		return new RuntimeException(e);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		if (memcachedClient != null) {
			memcachedClient.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Gets the memcached client.
	 *
	 * @return the memcached client
	 */
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	/**
	 * Sets the memcached client.
	 *
	 * @param memcachedClient the new memcached client
	 */
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	/**
	 * Sets the shutdown timeout.
	 *
	 * @param shutdownTimeout the new shutdown timeout
	 */
	public void setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}
}