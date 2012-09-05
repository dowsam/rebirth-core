/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MatcherAndConverter.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.matcher.Matcher;
import cn.com.rebirth.core.inject.spi.TypeConverter;


/**
 * The Class MatcherAndConverter.
 *
 * @author l.xue.nong
 */
public final class MatcherAndConverter {

    
    /** The type matcher. */
    private final Matcher<? super TypeLiteral<?>> typeMatcher;
    
    
    /** The type converter. */
    private final TypeConverter typeConverter;
    
    
    /** The source. */
    private final Object source;

    
    /**
     * Instantiates a new matcher and converter.
     *
     * @param typeMatcher the type matcher
     * @param typeConverter the type converter
     * @param source the source
     */
    public MatcherAndConverter(Matcher<? super TypeLiteral<?>> typeMatcher,
                               TypeConverter typeConverter, Object source) {
        this.typeMatcher = checkNotNull(typeMatcher, "type matcher");
        this.typeConverter = checkNotNull(typeConverter, "converter");
        this.source = source;
    }

    
    /**
     * Gets the type converter.
     *
     * @return the type converter
     */
    public TypeConverter getTypeConverter() {
        return typeConverter;
    }

    
    /**
     * Gets the type matcher.
     *
     * @return the type matcher
     */
    public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
        return typeMatcher;
    }

    
    /**
     * Gets the source.
     *
     * @return the source
     */
    public Object getSource() {
        return source;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return typeConverter + " which matches " + typeMatcher
                + " (bound at " + source + ")";
    }
}
