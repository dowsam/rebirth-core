/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Nullability.java 2012-7-6 10:23:53 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.Annotation;


/**
 * The Class Nullability.
 *
 * @author l.xue.nong
 */
public class Nullability {
    
    
    /**
     * Instantiates a new nullability.
     */
    private Nullability() {
    }

    
    /**
     * Allows null.
     *
     * @param annotations the annotations
     * @return true, if successful
     */
    public static boolean allowsNull(Annotation[] annotations) {
        for (Annotation a : annotations) {
            if ("Nullable".equals(a.annotationType().getSimpleName())) {
                return true;
            }
        }
        return false;
    }
}
