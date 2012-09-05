/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PrivateElements.java 2012-7-6 10:23:40 l.xue.nong$$
 */



package cn.com.rebirth.core.inject.spi;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;


/**
 * The Interface PrivateElements.
 *
 * @author l.xue.nong
 */
public interface PrivateElements extends Element {

    
    /**
     * Gets the elements.
     *
     * @return the elements
     */
    List<Element> getElements();

    
    /**
     * Gets the injector.
     *
     * @return the injector
     */
    Injector getInjector();

    
    /**
     * Gets the exposed keys.
     *
     * @return the exposed keys
     */
    Set<Key<?>> getExposedKeys();

    
    /**
     * Gets the exposed source.
     *
     * @param key the key
     * @return the exposed source
     */
    Object getExposedSource(Key<?> key);
}
