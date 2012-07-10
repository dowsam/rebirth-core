/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core MemcachedSimulator.java 2012-2-3 10:24:35 l.xue.nong$$
 */
package cn.com.rebirth.core.cache.memcached;

import net.spy.memcached.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.Key;
import com.thimbleware.jmemcached.LocalCacheElement;
import com.thimbleware.jmemcached.MemCacheDaemon;
import com.thimbleware.jmemcached.storage.CacheStorage;
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap;

/**
 * JMemcached的封装, 主要用于功能测试.
 *
 * @author l.xue.nong
 */
public class MemcachedSimulator implements InitializingBean, DisposableBean {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(MemcachedSimulator.class);

	/** The jmemcached. */
	private MemCacheDaemon<LocalCacheElement> jmemcached;

	/** The server url. */
	private String serverUrl = "localhost:11211";

	/** The max items. */
	private int maxItems = 1024 * 100;

	/** The max bytes. */
	private long maxBytes = 1024 * 100 * 2048;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		logger.info("Initializing JMemcached Server");

		jmemcached = new MemCacheDaemon<LocalCacheElement>();

		CacheStorage<Key, LocalCacheElement> storage = ConcurrentLinkedHashMap.create(
				ConcurrentLinkedHashMap.EvictionPolicy.FIFO, maxItems, maxBytes);
		jmemcached.setCache(new CacheImpl(storage));

		jmemcached.setAddr(AddrUtil.getAddresses(serverUrl).get(0));

		jmemcached.start();

		logger.info("Initialized JMemcached Server");
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		logger.info("Shutdowning Jmemcached Server");
		jmemcached.stop();
		logger.info("Shutdowned Jmemcached Server");
	}

	/**
	 * Sets the server url.
	 *
	 * @param serverUrl the new server url
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Sets the max items.
	 *
	 * @param maxItems the new max items
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	/**
	 * Sets the max bytes.
	 *
	 * @param maxBytes the new max bytes
	 */
	public void setMaxBytes(long maxBytes) {
		this.maxBytes = maxBytes;
	}
}
