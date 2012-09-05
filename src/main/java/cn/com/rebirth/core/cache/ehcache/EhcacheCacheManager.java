/*
 * Copyright (c) 2005-2011 www.ffcs.cn All rights reserved
 * Info:xmdp_sapddl EhcacheCacheManager.java 2011-12-10 12:00:24 l.xue.nong$$
 */
package cn.com.rebirth.core.cache.ehcache;

import java.util.List;

import cn.com.rebirth.core.cache.Cache;
import cn.com.rebirth.core.cache.CacheManager;
import cn.com.rebirth.core.cache.Element;

/**
 * The Class EhcacheCacheManager.
 *
 * @author l.xue.nong
 */
public class EhcacheCacheManager implements CacheManager {

	/** The cache manager. */
	protected net.sf.ehcache.CacheManager cacheManager;

	public net.sf.ehcache.CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/* (non-Javadoc)
	 * @see com.infolion.platform.dpframework.core.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public Cache getCache(String cacheName) {
		return new EhcacheCache(this.cacheManager.getCache(cacheName));
	}

	/**
	 * The Class EhcacheCache.
	 *
	 * @author l.xue.nong
	 */
	class EhcacheCache implements Cache {

		/** The cache. */
		private final net.sf.ehcache.Cache cache;

		/**
		 * Instantiates a new ehcache cache.
		 *
		 * @param cache the cache
		 */
		public EhcacheCache(net.sf.ehcache.Cache cache) {
			super();
			this.cache = cache;
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#removeAll()
		 */
		@Override
		public void removeAll() {
			this.cache.removeAll();
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#getKeys()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <T> List<T> getKeys() {
			return this.cache.getKeys();
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#remove(java.lang.String)
		 */
		@Override
		public void remove(String key) {
			this.cache.remove(key);
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#put(com.infolion.platform.dpframework.core.cache.Element)
		 */
		@Override
		public void put(Element element) {
			this.cache.put(element.toEhcache());
		}

		/* (non-Javadoc)
		 * @see com.infolion.platform.dpframework.core.cache.Cache#get(java.lang.String)
		 */
		@Override
		public Element get(String key) {
			Object value = this.cache.get(key);
			return value == null ? null : new Element(key, value);
		}

	}

	/* (non-Javadoc)
	 * @see com.infolion.platform.dpframework.core.cache.CacheManager#cleanAll()
	 */
	@Override
	public void cleanAll() {
		this.cacheManager.clearAll();
	}

}
