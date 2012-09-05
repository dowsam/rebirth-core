/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Element.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.multibindings;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import cn.com.rebirth.core.inject.BindingAnnotation;


/**
 * The Interface Element.
 *
 * @author l.xue.nong
 */
@Retention(RUNTIME)
@BindingAnnotation
@interface Element {

	
	/**
	 * Sets the name.
	 *
	 * @return the string
	 */
	String setName();

	
	/**
	 * Unique id.
	 *
	 * @return the int
	 */
	int uniqueId();
}
