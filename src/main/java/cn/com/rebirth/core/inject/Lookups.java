/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Lookups.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface Lookups.
 *
 * @author l.xue.nong
 */
interface Lookups {

    
    /**
     * Gets the provider.
     *
     * @param <T> the generic type
     * @param key the key
     * @return the provider
     */
    <T> Provider<T> getProvider(Key<T> key);

    
    /**
     * Gets the members injector.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the members injector
     */
    <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type);
}
