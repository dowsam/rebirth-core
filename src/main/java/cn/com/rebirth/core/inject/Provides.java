/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Provides.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * The Interface Provides.
 *
 * @author l.xue.nong
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Provides {
}
