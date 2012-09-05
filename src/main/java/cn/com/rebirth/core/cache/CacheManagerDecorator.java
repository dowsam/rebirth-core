/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core CacheManagerDecorator.java 2012-7-20 11:36:26 l.xue.nong$$
 */
package cn.com.rebirth.core.cache;

/**
 * The Class CacheManagerDecorator.
 *
 * @author l.xue.nong
 */
public class CacheManagerDecorator implements CacheManager {

	/** The cache manager. */
	private final CacheManager cacheManager;

	/**
	 * Gets the cache manager.
	 *
	 * @return the cache manager
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * Instantiates a new cache manager decorator.
	 *
	 * @param cacheManager the cache manager
	 */
	public CacheManagerDecorator(CacheManager cacheManager) {
		super();
		this.cacheManager = cacheManager;
	}

	/* (non-Javadoc)
	 * @see com.infolion.platform.dpframework.core.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public Cache getCache(String cacheName) {
		return getCacheManager().getCache(cacheName);
	}

	/* (non-Javadoc)
	 * @see com.infolion.platform.dpframework.core.cache.CacheManager#cleanAll()
	 */
	@Override
	public void cleanAll() {
		getCacheManager().cleanAll();
	}

}
