/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectionListener.java 2012-7-6 10:23:44 l.xue.nong$$
 */



package cn.com.rebirth.core.inject.spi;


/**
 * The listener interface for receiving injection events.
 * The class that is interested in processing a injection
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addInjectionListener<code> method. When
 * the injection event occurs, that object's appropriate
 * method is invoked.
 *
 * @param <I> the generic type
 * @see InjectionEvent
 */
public interface InjectionListener<I> {

    
    /**
     * After injection.
     *
     * @param injectee the injectee
     */
    void afterInjection(I injectee);
}
