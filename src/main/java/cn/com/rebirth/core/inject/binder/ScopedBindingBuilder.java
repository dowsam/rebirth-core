/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ScopedBindingBuilder.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.binder;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.Scope;


/**
 * The Interface ScopedBindingBuilder.
 *
 * @author l.xue.nong
 */
public interface ScopedBindingBuilder {

	
	/**
	 * In.
	 *
	 * @param scopeAnnotation the scope annotation
	 */
	void in(Class<? extends Annotation> scopeAnnotation);

	
	/**
	 * In.
	 *
	 * @param scope the scope
	 */
	void in(Scope scope);

	
	/**
	 * As eager singleton.
	 */
	void asEagerSingleton();
}
