/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AssistedInject.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * The Interface AssistedInject.
 *
 * @author l.xue.nong
 */
@Target({ CONSTRUCTOR })
@Retention(RUNTIME)
public @interface AssistedInject {
}
