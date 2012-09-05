/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons DefaultBindingScopingVisitor.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.Scope;


/**
 * The Class DefaultBindingScopingVisitor.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public class DefaultBindingScopingVisitor<V> implements BindingScopingVisitor<V> {

	
	/**
	 * Visit other.
	 *
	 * @return the v
	 */
	protected V visitOther() {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingScopingVisitor#visitEagerSingleton()
	 */
	public V visitEagerSingleton() {
		return visitOther();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingScopingVisitor#visitScope(cn.com.rebirth.search.commons.inject.Scope)
	 */
	public V visitScope(Scope scope) {
		return visitOther();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingScopingVisitor#visitScopeAnnotation(java.lang.Class)
	 */
	public V visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
		return visitOther();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingScopingVisitor#visitNoScoping()
	 */
	public V visitNoScoping() {
		return visitOther();
	}
}
