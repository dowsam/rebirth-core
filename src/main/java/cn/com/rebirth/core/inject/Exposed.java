/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Exposed.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * The Interface Exposed.
 *
 * @author l.xue.nong
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Documented
public @interface Exposed {
}
