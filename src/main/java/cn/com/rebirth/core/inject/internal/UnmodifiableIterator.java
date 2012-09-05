/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons UnmodifiableIterator.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.util.Iterator;


/**
 * The Class UnmodifiableIterator.
 *
 * @param <E> the element type
 * @author l.xue.nong
 */
public abstract class UnmodifiableIterator<E> implements Iterator<E> {

    
    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
