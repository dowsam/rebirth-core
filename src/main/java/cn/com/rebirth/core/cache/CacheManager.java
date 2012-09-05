/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core CacheManager.java 2012-7-20 11:35:03 l.xue.nong$$
 */
package cn.com.rebirth.core.cache;

/**
 * The Interface CacheManager.
 *
 * @author l.xue.nong
 */
public interface CacheManager {

	/**
	 * Gets the cache.
	 *
	 * @param cacheName the cache name
	 * @return the cache
	 */
	public Cache getCache(String cacheName);

	/**
	 * Clean all.
	 */
	public void cleanAll();
}
