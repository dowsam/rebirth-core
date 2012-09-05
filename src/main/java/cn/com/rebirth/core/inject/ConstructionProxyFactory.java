/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructionProxyFactory.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * A factory for creating ConstructionProxy objects.
 *
 * @param <T> the generic type
 */
interface ConstructionProxyFactory<T> {

    
    /**
     * Creates the.
     *
     * @return the construction proxy
     */
    ConstructionProxy<T> create();
}
