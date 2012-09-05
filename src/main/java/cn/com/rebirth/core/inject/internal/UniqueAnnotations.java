/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons UniqueAnnotations.java 2012-7-6 10:23:54 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.util.concurrent.atomic.AtomicInteger;

import cn.com.rebirth.core.inject.BindingAnnotation;


/**
 * The Class UniqueAnnotations.
 *
 * @author l.xue.nong
 */
public class UniqueAnnotations {

	
	/**
	 * Instantiates a new unique annotations.
	 */
	private UniqueAnnotations() {
	}

	
	/** The Constant nextUniqueValue. */
	private static final AtomicInteger nextUniqueValue = new AtomicInteger(1);

	
	/**
	 * Creates the.
	 *
	 * @return the annotation
	 */
	public static Annotation create() {
		return create(nextUniqueValue.getAndIncrement());
	}

	
	/**
	 * Creates the.
	 *
	 * @param value the value
	 * @return the annotation
	 */
	static Annotation create(final int value) {
		return new Internal() {
			public int value() {
				return value;
			}

			public Class<? extends Annotation> annotationType() {
				return Internal.class;
			}

			@Override
			public String toString() {
				return "@" + Internal.class.getName() + "(value=" + value + ")";
			}

			@Override
			public boolean equals(Object o) {
				return o instanceof Internal && ((Internal) o).value() == value();
			}

			@Override
			public int hashCode() {
				return (127 * "value".hashCode()) ^ value;
			}
		};
	}

	
	/**
	 * The Interface Internal.
	 *
	 * @author l.xue.nong
	 */
	@Retention(RUNTIME)
	@BindingAnnotation
	@interface Internal {

		
		/**
		 * Value.
		 *
		 * @return the int
		 */
		int value();
	}
}
