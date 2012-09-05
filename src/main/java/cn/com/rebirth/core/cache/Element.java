/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core Element.java 2012-7-20 11:34:57 l.xue.nong$$
 */
package cn.com.rebirth.core.cache;

import java.io.Serializable;

/**
 * The Class Element.
 *
 * @author l.xue.nong
 */
public class Element implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7608961352066321383L;

	/** The key. */
	private final String key;

	/** The value. */
	private final Object value;

	/**
	 * Instantiates a new element.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public Element(String key, Object value) {
		super();
		this.key = key;
		if (value instanceof net.sf.ehcache.Element) {
			net.sf.ehcache.Element element = (net.sf.ehcache.Element) value;
			value = element.getObjectValue();
		}
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @param <T> the generic type
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.value;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * To ehcache.
	 *
	 * @return the net.sf.ehcache. element
	 */
	public net.sf.ehcache.Element toEhcache() {
		return new net.sf.ehcache.Element(key, value);
	}

}
