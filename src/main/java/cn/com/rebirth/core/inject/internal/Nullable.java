/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Nullable.java 2012-7-6 10:23:42 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.*;


/**
 * The Interface Nullable.
 *
 * @author l.xue.nong
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Nullable {
}
