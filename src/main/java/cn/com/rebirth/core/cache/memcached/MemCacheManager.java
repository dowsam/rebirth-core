/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core MemCacheManager.java 2012-7-20 11:47:07 l.xue.nong$$
 */
package cn.com.rebirth.core.cache.memcached;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.StringUtils;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.CglibProxyUtils;
import cn.com.rebirth.commons.utils.SpringContextHolder;
import cn.com.rebirth.core.cache.Cache;
import cn.com.rebirth.core.cache.CacheManager;
import cn.com.rebirth.core.cache.Element;

/**
 * The Class MemCacheManager.
 *
 * @author l.xue.nong
 */
public class MemCacheManager implements CacheManager {

	/** The client. */
	protected SpyMemcachedClient client;

	/**
	 * Instantiates a new mem cache manager.
	 */
	public MemCacheManager() {
		super();
	}

	/**
	 * Instantiates a new mem cache manager.
	 *
	 * @param client the client
	 */
	public MemCacheManager(SpyMemcachedClient client) {
		super();
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public Cache getCache(String cacheName) {
		MemCache memCache = new MemCache(client, cacheName);
		return CglibProxyUtils.getProxyInstance(MemCache.class, Cache.class, new MemCacheMethodInterceptor(memCache));
	}

	/**
	 * The Class MemCacheMethodInterceptor.
	 *
	 * @author l.xue.nong
	 */
	class MemCacheMethodInterceptor implements MethodInterceptor {

		/** The mem cache. */
		private MemCache memCache;

		/**
		 * Instantiates a new mem cache method interceptor.
		 *
		 * @param memCache the mem cache
		 */
		public MemCacheMethodInterceptor(MemCache memCache) {
			super();
			this.memCache = memCache;
		}

		/* (non-Javadoc)
		 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (memCache.spyMemcachedClient == null) {
				memCache.spyMemcachedClient = SpringContextHolder.getBean(StringUtils
						.uncapitalize(SpyMemcachedClient.class.getSimpleName()));
			}
			if (memCache.spyMemcachedClient == null)
				throw new RebirthException("Memcached Client Not null");
			return method.invoke(memCache, args);
		}

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.cache.CacheManager#cleanAll()
	 */
	@Override
	public void cleanAll() {
		this.client.getMemcachedClient().flush();
	}

	/**
	 * The Class MemCache.
	 *
	 * @author l.xue.nong
	 */
	static class MemCache implements Cache {

		/** The spy memcached client. */
		private SpyMemcachedClient spyMemcachedClient;

		/** The cache name. */
		private String cacheName;

		/** The Constant TOP_FIX. */
		private final static String TOP_FIX = "_";

		/** The Constant TOP_FIX_KEYS. */
		private final static String TOP_FIX_KEYS = "_KEYS";

		/**
		 * Instantiates a new mem cache.
		 */
		public MemCache() {
			super();
		}

		/**
		 * Instantiates a new mem cache.
		 *
		 * @param spyMemcachedClient the spy memcached client
		 * @param cacheName the cache name
		 */
		public MemCache(SpyMemcachedClient spyMemcachedClient, String cacheName) {
			super();
			this.spyMemcachedClient = spyMemcachedClient;
			this.cacheName = cacheName;
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#removeAll()
		 */
		@Override
		public void removeAll() {
			List<String> keys = this.spyMemcachedClient.get(cacheName + TOP_FIX_KEYS);
			if (keys == null || keys.isEmpty())
				return;
			for (String key : keys) {
				remove(key);
			}
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#getKeys()
		 */
		@Override
		public <T> List<T> getKeys() {
			return this.spyMemcachedClient.get(cacheName + TOP_FIX_KEYS);
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#remove(java.lang.String)
		 */
		@Override
		public void remove(String key) {
			this.spyMemcachedClient.delete(cacheName + TOP_FIX + key);
			List<String> keys = this.spyMemcachedClient.get(cacheName + TOP_FIX_KEYS);
			if (keys == null)
				keys = new ArrayList<String>();
			keys.remove(key);
			this.spyMemcachedClient.set(cacheName + TOP_FIX_KEYS, 0, keys);
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#put(com.infolion.platform.dpframework.core.cache.Element)
		 */
		@Override
		public void put(Element element) {
			this.spyMemcachedClient.set(cacheName + TOP_FIX + element.getKey(), 0, element.getValue());
			List<String> keys = this.spyMemcachedClient.get(cacheName + TOP_FIX_KEYS);
			if (keys == null)
				keys = new ArrayList<String>();
			keys.add(element.getKey());
			this.spyMemcachedClient.set(cacheName + TOP_FIX_KEYS, 0, keys);
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#get(java.lang.String)
		 */
		@Override
		public Element get(String key) {
			Object value = this.spyMemcachedClient.get(cacheName + TOP_FIX + key);
			return value == null ? null : new Element(key, value);
		}

	}
}
