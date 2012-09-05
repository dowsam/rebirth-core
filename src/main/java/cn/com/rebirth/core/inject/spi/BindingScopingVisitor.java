/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BindingScopingVisitor.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.Scope;


/**
 * The Interface BindingScopingVisitor.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public interface BindingScopingVisitor<V> {

	
	/**
	 * Visit eager singleton.
	 *
	 * @return the v
	 */
	V visitEagerSingleton();

	
	/**
	 * Visit scope.
	 *
	 * @param scope the scope
	 * @return the v
	 */
	V visitScope(Scope scope);

	
	/**
	 * Visit scope annotation.
	 *
	 * @param scopeAnnotation the scope annotation
	 * @return the v
	 */
	V visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation);

	
	/**
	 * Visit no scoping.
	 *
	 * @return the v
	 */
	V visitNoScoping();
}
