/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Providers.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.util;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Class Providers.
 *
 * @author l.xue.nong
 */
public final class Providers {

    
    /**
     * Instantiates a new providers.
     */
    private Providers() {
    }

    
    /**
     * Of.
     *
     * @param <T> the generic type
     * @param instance the instance
     * @return the provider
     */
    public static <T> Provider<T> of(final T instance) {
        return new Provider<T>() {
            public T get() {
                return instance;
            }

            @Override
            public String toString() {
                return "of(" + instance + ")";
            }
        };
    }
}
