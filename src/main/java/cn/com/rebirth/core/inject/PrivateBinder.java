/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PrivateBinder.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.binder.AnnotatedElementBuilder;


/**
 * The Interface PrivateBinder.
 *
 * @author l.xue.nong
 */
public interface PrivateBinder extends Binder {

	
	/**
	 * Expose.
	 *
	 * @param key the key
	 */
	void expose(Key<?> key);

	
	/**
	 * Expose.
	 *
	 * @param type the type
	 * @return the annotated element builder
	 */
	AnnotatedElementBuilder expose(Class<?> type);

	
	/**
	 * Expose.
	 *
	 * @param type the type
	 * @return the annotated element builder
	 */
	AnnotatedElementBuilder expose(TypeLiteral<?> type);

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binder#withSource(java.lang.Object)
	 */
	PrivateBinder withSource(Object source);

	
	
	/**
	 * Skip sources.
	 *
	 * @param classesToSkip the classes to skip
	 * @return the private binder
	 */
	PrivateBinder skipSources(Class<?>... classesToSkip);
}
