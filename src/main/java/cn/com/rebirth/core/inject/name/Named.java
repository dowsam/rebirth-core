/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Named.java 2012-7-6 10:23:40 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.name;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cn.com.rebirth.core.inject.BindingAnnotation;


/**
 * The Interface Named.
 *
 * @author l.xue.nong
 */
@Retention(RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@BindingAnnotation
public @interface Named {
	
	
	/**
	 * Value.
	 *
	 * @return the string
	 */
	String value();
}
