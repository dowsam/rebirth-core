/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ToStringBuilder.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The Class ToStringBuilder.
 *
 * @author l.xue.nong
 */
public class ToStringBuilder {

    
    
    /** The map. */
    final Map<String, Object> map = new LinkedHashMap<String, Object>();

    
    /** The name. */
    final String name;

    
    /**
     * Instantiates a new to string builder.
     *
     * @param name the name
     */
    public ToStringBuilder(String name) {
        this.name = name;
    }

    
    /**
     * Instantiates a new to string builder.
     *
     * @param type the type
     */
    public ToStringBuilder(Class<?> type) {
        this.name = type.getSimpleName();
    }

    
    /**
     * Adds the.
     *
     * @param name the name
     * @param value the value
     * @return the to string builder
     */
    public ToStringBuilder add(String name, Object value) {
        if (map.put(name, value) != null) {
            throw new RuntimeException("Duplicate names: " + name);
        }
        return this;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name + map.toString().replace('{', '[').replace('}', ']');
    }
}
