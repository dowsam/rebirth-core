/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BindingAnnotation.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * The Interface BindingAnnotation.
 *
 * @author l.xue.nong
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface BindingAnnotation {
}
