/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SourceProvider.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * The Class SourceProvider.
 *
 * @author l.xue.nong
 */
public class SourceProvider {

    
    /** The Constant UNKNOWN_SOURCE. */
    public static final Object UNKNOWN_SOURCE = "[unknown source]";

    
    /** The class names to skip. */
    private final ImmutableSet<String> classNamesToSkip;

    
    /**
     * Instantiates a new source provider.
     */
    public SourceProvider() {
        this.classNamesToSkip = ImmutableSet.of(SourceProvider.class.getName());
    }

    
    /** The Constant DEFAULT_INSTANCE. */
    public static final SourceProvider DEFAULT_INSTANCE
            = new SourceProvider(ImmutableSet.of(SourceProvider.class.getName()));

    
    /**
     * Instantiates a new source provider.
     *
     * @param classesToSkip the classes to skip
     */
    private SourceProvider(Iterable<String> classesToSkip) {
        this.classNamesToSkip = ImmutableSet.copyOf(classesToSkip);
    }

    
    /**
     * Plus skipped classes.
     *
     * @param moreClassesToSkip the more classes to skip
     * @return the source provider
     */
    public SourceProvider plusSkippedClasses(Class<?>... moreClassesToSkip) {
        return new SourceProvider(Iterables.concat(classNamesToSkip, asStrings(moreClassesToSkip)));
    }

    
    /**
     * As strings.
     *
     * @param classes the classes
     * @return the list
     */
    private static List<String> asStrings(Class<?>... classes) {
        List<String> strings = Lists.newArrayList();
        for (Class<?> c : classes) {
            strings.add(c.getName());
        }
        return strings;
    }

    
    /**
     * Gets the.
     *
     * @return the stack trace element
     */
    public StackTraceElement get() {
        for (final StackTraceElement element : new Throwable().getStackTrace()) {
            String className = element.getClassName();
            if (!classNamesToSkip.contains(className)) {
                return element;
            }
        }
        throw new AssertionError();
    }
}
