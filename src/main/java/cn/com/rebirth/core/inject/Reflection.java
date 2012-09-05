/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Reflection.java 2012-7-6 10:23:50 l.xue.nong$$
 */



package cn.com.rebirth.core.inject;
import java.lang.reflect.Constructor;


/**
 * The Class Reflection.
 *
 * @author l.xue.nong
 */
class Reflection {

    
    /**
     * The Class InvalidConstructor.
     *
     * @author l.xue.nong
     */
    static class InvalidConstructor {
        
        
        /**
         * Instantiates a new invalid constructor.
         */
        InvalidConstructor() {
            throw new AssertionError();
        }
    }

    
    /**
     * Invalid constructor.
     *
     * @param <T> the generic type
     * @return the constructor
     */
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> invalidConstructor() {
        try {
            return (Constructor<T>) InvalidConstructor.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }
}
