/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Scope.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface Scope.
 *
 * @author l.xue.nong
 */
public interface Scope {

    
    /**
     * Scope.
     *
     * @param <T> the generic type
     * @param key the key
     * @param unscoped the unscoped
     * @return the provider
     */
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped);

    
    /**
     * To string.
     *
     * @return the string
     */
    String toString();
}
