/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InternalContext.java 2012-7-6 10:23:44 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.util.Map;

import cn.com.rebirth.core.inject.spi.Dependency;

import com.google.common.collect.Maps;


/**
 * The Class InternalContext.
 *
 * @author l.xue.nong
 */
public final class InternalContext {

	
	/** The construction contexts. */
	private Map<Object, ConstructionContext<?>> constructionContexts = Maps.newHashMap();

	
	/** The dependency. */
	@SuppressWarnings("rawtypes")
	private Dependency dependency;

	
	/**
	 * Gets the construction context.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the construction context
	 */
	@SuppressWarnings("unchecked")
	public <T> ConstructionContext<T> getConstructionContext(Object key) {
		ConstructionContext<T> constructionContext = (ConstructionContext<T>) constructionContexts.get(key);
		if (constructionContext == null) {
			constructionContext = new ConstructionContext<T>();
			constructionContexts.put(key, constructionContext);
		}
		return constructionContext;
	}

	
	/**
	 * Gets the dependency.
	 *
	 * @return the dependency
	 */
	@SuppressWarnings("rawtypes")
	public Dependency getDependency() {
		return dependency;
	}

	
	/**
	 * Sets the dependency.
	 *
	 * @param dependency the new dependency
	 */
	@SuppressWarnings("rawtypes")
	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}
}
