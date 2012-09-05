/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AnnotatedConstantBindingBuilder.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.binder;

import java.lang.annotation.Annotation;


/**
 * The Interface AnnotatedConstantBindingBuilder.
 *
 * @author l.xue.nong
 */
public interface AnnotatedConstantBindingBuilder {

	
	/**
	 * Annotated with.
	 *
	 * @param annotationType the annotation type
	 * @return the constant binding builder
	 */
	ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType);

	
	/**
	 * Annotated with.
	 *
	 * @param annotation the annotation
	 * @return the constant binding builder
	 */
	ConstantBindingBuilder annotatedWith(Annotation annotation);
}
