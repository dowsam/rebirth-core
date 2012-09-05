/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SpawnModules.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;


/**
 * The Interface SpawnModules.
 *
 * @author l.xue.nong
 */
public interface SpawnModules {

    
    /**
     * Spawn modules.
     *
     * @return the iterable<? extends module>
     */
    Iterable<? extends Module> spawnModules();
}
