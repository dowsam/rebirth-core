/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core Cache.java 2012-7-20 11:34:24 l.xue.nong$$
 */
package cn.com.rebirth.core.cache;

import java.util.List;

/**
 * The Interface Cache.
 *
 * @author l.xue.nong
 */
public interface Cache {

	/**
	 * Removes the all.
	 */
	public void removeAll();

	/**
	 * Gets the keys.
	 *
	 * @param <T> the generic type
	 * @return the keys
	 */
	public <T> List<T> getKeys();

	/**
	 * Removes the.
	 *
	 * @param key the key
	 */
	public void remove(String key);

	/**
	 * Put.
	 *
	 * @param element the element
	 */
	public void put(Element element);

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the element
	 */
	public Element get(String key);
}
