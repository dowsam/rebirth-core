/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons WeakKeySet.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import com.google.common.collect.Sets;

import java.util.Set;


/**
 * The Class WeakKeySet.
 *
 * @author l.xue.nong
 */
final class WeakKeySet {

    
    /** The backing set. */
    private Set<String> backingSet = Sets.newHashSet();

    
    /**
     * Adds the.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean add(Key<?> key) {
        return backingSet.add(key.toString());
    }

    
    /**
     * Contains.
     *
     * @param o the o
     * @return true, if successful
     */
    public boolean contains(Object o) {
        return o instanceof Key && backingSet.contains(o.toString());
    }
}
