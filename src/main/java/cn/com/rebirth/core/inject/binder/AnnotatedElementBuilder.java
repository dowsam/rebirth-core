/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AnnotatedElementBuilder.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.binder;

import java.lang.annotation.Annotation;


/**
 * The Interface AnnotatedElementBuilder.
 *
 * @author l.xue.nong
 */
public interface AnnotatedElementBuilder {

    
    /**
     * Annotated with.
     *
     * @param annotationType the annotation type
     */
    void annotatedWith(Class<? extends Annotation> annotationType);

    
    /**
     * Annotated with.
     *
     * @param annotation the annotation
     */
    void annotatedWith(Annotation annotation);
}
