/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons HasDependencies.java 2012-7-6 10:23:47 l.xue.nong$$
 */



package cn.com.rebirth.core.inject.spi;
import java.util.Set;


/**
 * The Interface HasDependencies.
 *
 * @author l.xue.nong
 */
public interface HasDependencies {

    
    /**
     * Gets the dependencies.
     *
     * @return the dependencies
     */
    Set<Dependency<?>> getDependencies();
}
