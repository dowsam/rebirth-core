/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Names.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.name;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Key;


/**
 * The Class Names.
 *
 * @author l.xue.nong
 */
public class Names {

	
	/**
	 * Instantiates a new names.
	 */
	private Names() {
	}

	
	/**
	 * Named.
	 *
	 * @param name the name
	 * @return the named
	 */
	public static Named named(String name) {
		return new NamedImpl(name);
	}

	
	/**
	 * Bind properties.
	 *
	 * @param binder the binder
	 * @param properties the properties
	 */
	public static void bindProperties(Binder binder, Map<String, String> properties) {
		binder = binder.skipSources(Names.class);
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			binder.bind(Key.get(String.class, new NamedImpl(key))).toInstance(value);
		}
	}

	
	/**
	 * Bind properties.
	 *
	 * @param binder the binder
	 * @param properties the properties
	 */
	public static void bindProperties(Binder binder, Properties properties) {
		binder = binder.skipSources(Names.class);

		
		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
			String propertyName = (String) e.nextElement();
			String value = properties.getProperty(propertyName);
			binder.bind(Key.get(String.class, new NamedImpl(propertyName))).toInstance(value);
		}
	}
}
