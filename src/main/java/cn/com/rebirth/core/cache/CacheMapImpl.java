/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core CacheMapImpl.java 2012-2-3 10:29:34 l.xue.nong$$
 */
package cn.com.rebirth.core.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

/**
 * 基于Google Utils Cache实现，缓存对象存服务器中一天
 *
 * @author l.xue.nong
 */
public class CacheMapImpl {
	/** The cache. */
	private ConcurrentMap<Object, Object> cache;

	/**
	 * Instantiates a new cache map impl.
	 */
	private CacheMapImpl() {
		super();
		this.cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).softValues().build().asMap();
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The cache map impl. */
		static CacheMapImpl cacheMapImpl = new CacheMapImpl();
	}

	/**
	 * Gets the single instance of CacheMapImpl.
	 *
	 * @return single instance of CacheMapImpl
	 */
	public static CacheMapImpl getInstance() {
		return SingletonHolder.cacheMapImpl;
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		return (T) cache.get(key);
	}

	/**
	 * Put.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	/**
	 * Removes the.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T remove(Object key) {
		return (T) cache.remove(key);
	}
}
