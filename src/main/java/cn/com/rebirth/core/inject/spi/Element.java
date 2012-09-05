/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Element.java 2012-7-6 10:23:41 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.spi;


/**
 * The Interface Element.
 *
 * @author l.xue.nong
 */
public interface Element {

    
    /**
     * Gets the source.
     *
     * @return the source
     */
    Object getSource();

    
    /**
     * Accept visitor.
     *
     * @param <T> the generic type
     * @param visitor the visitor
     * @return the t
     */
    <T> T acceptVisitor(ElementVisitor<T> visitor);

    
    /**
     * Apply to.
     *
     * @param binder the binder
     */
    void applyTo(cn.com.rebirth.core.inject.Binder binder);

}
