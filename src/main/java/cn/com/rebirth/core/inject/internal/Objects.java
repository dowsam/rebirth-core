/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Objects.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.util.Arrays;


/**
 * The Class Objects.
 *
 * @author l.xue.nong
 */
public final class Objects {
    
    
    /**
     * Instantiates a new objects.
     */
    private Objects() {
    }

    
    /**
     * Equal.
     *
     * @param a the a
     * @param b the b
     * @return true, if successful
     */
    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    
    /**
     * Hash code.
     *
     * @param objects the objects
     * @return the int
     */
    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}
