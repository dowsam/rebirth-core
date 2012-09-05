/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Annotations.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Member;

import cn.com.rebirth.core.inject.BindingAnnotation;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.ScopeAnnotation;
import cn.com.rebirth.core.inject.TypeLiteral;


/**
 * The Class Annotations.
 *
 * @author l.xue.nong
 */
public class Annotations {

	
	/**
	 * Checks if is retained at runtime.
	 *
	 * @param annotationType the annotation type
	 * @return true, if is retained at runtime
	 */
	public static boolean isRetainedAtRuntime(Class<? extends Annotation> annotationType) {
		Retention retention = annotationType.getAnnotation(Retention.class);
		return retention != null && retention.value() == RetentionPolicy.RUNTIME;
	}

	
	/**
	 * Find scope annotation.
	 *
	 * @param errors the errors
	 * @param implementation the implementation
	 * @return the class<? extends annotation>
	 */
	public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Class<?> implementation) {
		return findScopeAnnotation(errors, implementation.getAnnotations());
	}

	
	/**
	 * Find scope annotation.
	 *
	 * @param errors the errors
	 * @param annotations the annotations
	 * @return the class<? extends annotation>
	 */
	public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Annotation[] annotations) {
		Class<? extends Annotation> found = null;

		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(ScopeAnnotation.class)) {
				if (found != null) {
					errors.duplicateScopeAnnotations(found, annotation.annotationType());
				} else {
					found = annotation.annotationType();
				}
			}
		}

		return found;
	}

	
	/**
	 * Checks if is scope annotation.
	 *
	 * @param annotationType the annotation type
	 * @return true, if is scope annotation
	 */
	public static boolean isScopeAnnotation(Class<? extends Annotation> annotationType) {
		return annotationType.isAnnotationPresent(ScopeAnnotation.class);
	}

	
	/**
	 * Check for misplaced scope annotations.
	 *
	 * @param type the type
	 * @param source the source
	 * @param errors the errors
	 */
	public static void checkForMisplacedScopeAnnotations(Class<?> type, Object source, Errors errors) {
		if (Classes.isConcrete(type)) {
			return;
		}

		Class<? extends Annotation> scopeAnnotation = findScopeAnnotation(errors, type);
		if (scopeAnnotation != null) {
			errors.withSource(type).scopeAnnotationOnAbstractType(scopeAnnotation, type, source);
		}
	}

	
	/**
	 * Gets the key.
	 *
	 * @param type the type
	 * @param member the member
	 * @param annotations the annotations
	 * @param errors the errors
	 * @return the key
	 * @throws ErrorsException the errors exception
	 */
	public static Key<?> getKey(TypeLiteral<?> type, Member member, Annotation[] annotations, Errors errors)
			throws ErrorsException {
		int numErrorsBefore = errors.size();
		Annotation found = findBindingAnnotation(errors, member, annotations);
		errors.throwIfNewErrors(numErrorsBefore);
		return found == null ? Key.get(type) : Key.get(type, found);
	}

	
	/**
	 * Find binding annotation.
	 *
	 * @param errors the errors
	 * @param member the member
	 * @param annotations the annotations
	 * @return the annotation
	 */
	public static Annotation findBindingAnnotation(Errors errors, Member member, Annotation[] annotations) {
		Annotation found = null;

		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class)) {
				if (found != null) {
					errors.duplicateBindingAnnotations(member, found.annotationType(), annotation.annotationType());
				} else {
					found = annotation;
				}
			}
		}

		return found;
	}
}
