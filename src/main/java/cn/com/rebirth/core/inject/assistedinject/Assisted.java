/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Assisted.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cn.com.rebirth.core.inject.BindingAnnotation;


/**
 * The Interface Assisted.
 *
 * @author l.xue.nong
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface Assisted {

	
	/**
	 * Value.
	 *
	 * @return the string
	 */
	String value() default "";
}