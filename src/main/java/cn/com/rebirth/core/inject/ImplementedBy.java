/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ImplementedBy.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * The Interface ImplementedBy.
 *
 * @author l.xue.nong
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ImplementedBy {

    
    /**
     * Value.
     *
     * @return the class
     */
    Class<?> value();
}
