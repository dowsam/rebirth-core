/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProvidedBy.java 2012-7-6 10:23:43 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * The Interface ProvidedBy.
 *
 * @author l.xue.nong
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ProvidedBy {

    
    /**
     * Value.
     *
     * @return the class<? extends provider<?>>
     */
    Class<? extends Provider<?>> value();
}
