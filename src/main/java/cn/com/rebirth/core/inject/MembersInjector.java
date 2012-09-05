/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MembersInjector.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface MembersInjector.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface MembersInjector<T> {

    
    /**
     * Inject members.
     *
     * @param instance the instance
     */
    void injectMembers(T instance);
}
