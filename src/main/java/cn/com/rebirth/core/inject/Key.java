/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Key.java 2012-7-6 10:23:43 l.xue.nong$$
 */

package cn.com.rebirth.core.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import cn.com.rebirth.commons.Preconditions;
import cn.com.rebirth.core.inject.internal.Annotations;
import cn.com.rebirth.core.inject.internal.MoreTypes;
import cn.com.rebirth.core.inject.internal.ToStringBuilder;

/**
 * The Class Key.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class Key<T> {

	/** The annotation strategy. */
	private final AnnotationStrategy annotationStrategy;

	/** The type literal. */
	private final TypeLiteral<T> typeLiteral;

	/** The hash code. */
	private final int hashCode;

	/**
	 * Instantiates a new key.
	 *
	 * @param annotationType the annotation type
	 */
	@SuppressWarnings("unchecked")
	protected Key(Class<? extends Annotation> annotationType) {
		this.annotationStrategy = strategyFor(annotationType);
		this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
		this.hashCode = computeHashCode();
	}

	/**
	 * Instantiates a new key.
	 *
	 * @param annotation the annotation
	 */
	@SuppressWarnings("unchecked")
	protected Key(Annotation annotation) {

		this.annotationStrategy = strategyFor(annotation);
		this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
		this.hashCode = computeHashCode();
	}

	/**
	 * Instantiates a new key.
	 */
	@SuppressWarnings("unchecked")
	protected Key() {
		this.annotationStrategy = NullAnnotationStrategy.INSTANCE;
		this.typeLiteral = (TypeLiteral<T>) TypeLiteral.fromSuperclassTypeParameter(getClass());
		this.hashCode = computeHashCode();
	}

	/**
	 * Instantiates a new key.
	 *
	 * @param type the type
	 * @param annotationStrategy the annotation strategy
	 */
	@SuppressWarnings("unchecked")
	private Key(Type type, AnnotationStrategy annotationStrategy) {
		this.annotationStrategy = annotationStrategy;
		this.typeLiteral = MoreTypes.makeKeySafe((TypeLiteral<T>) TypeLiteral.get(type));
		this.hashCode = computeHashCode();
	}

	/**
	 * Instantiates a new key.
	 *
	 * @param typeLiteral the type literal
	 * @param annotationStrategy the annotation strategy
	 */
	private Key(TypeLiteral<T> typeLiteral, AnnotationStrategy annotationStrategy) {
		this.annotationStrategy = annotationStrategy;
		this.typeLiteral = MoreTypes.makeKeySafe(typeLiteral);
		this.hashCode = computeHashCode();
	}

	/**
	 * Compute hash code.
	 *
	 * @return the int
	 */
	private int computeHashCode() {
		return typeLiteral.hashCode() * 31 + annotationStrategy.hashCode();
	}

	/**
	 * Gets the type literal.
	 *
	 * @return the type literal
	 */
	public final TypeLiteral<T> getTypeLiteral() {
		return typeLiteral;
	}

	/**
	 * Gets the annotation type.
	 *
	 * @return the annotation type
	 */
	public final Class<? extends Annotation> getAnnotationType() {
		return annotationStrategy.getAnnotationType();
	}

	/**
	 * Gets the annotation.
	 *
	 * @return the annotation
	 */
	public final Annotation getAnnotation() {
		return annotationStrategy.getAnnotation();
	}

	/**
	 * Checks for annotation type.
	 *
	 * @return true, if successful
	 */
	boolean hasAnnotationType() {
		return annotationStrategy.getAnnotationType() != null;
	}

	/**
	 * Gets the annotation name.
	 *
	 * @return the annotation name
	 */
	String getAnnotationName() {
		Annotation annotation = annotationStrategy.getAnnotation();
		if (annotation != null) {
			return annotation.toString();
		}

		return annotationStrategy.getAnnotationType().toString();
	}

	/**
	 * Gets the raw type.
	 *
	 * @return the raw type
	 */
	Class<? super T> getRawType() {
		return typeLiteral.getRawType();
	}

	/**
	 * Provider key.
	 *
	 * @return the key
	 */
	Key<Provider<T>> providerKey() {
		return ofType(typeLiteral.providerType());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Key<?>)) {
			return false;
		}
		Key<?> other = (Key<?>) o;
		return annotationStrategy.equals(other.annotationStrategy) && typeLiteral.equals(other.typeLiteral);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return new ToStringBuilder(Key.class).add("type", typeLiteral).add("annotation", annotationStrategy).toString();
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param annotationStrategy the annotation strategy
	 * @return the key
	 */
	static <T> Key<T> get(Class<T> type, AnnotationStrategy annotationStrategy) {
		return new Key<T>(type, annotationStrategy);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the key
	 */
	public static <T> Key<T> get(Class<T> type) {
		return new Key<T>(type, NullAnnotationStrategy.INSTANCE);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param annotationType the annotation type
	 * @return the key
	 */
	public static <T> Key<T> get(Class<T> type, Class<? extends Annotation> annotationType) {
		return new Key<T>(type, strategyFor(annotationType));
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param annotation the annotation
	 * @return the key
	 */
	public static <T> Key<T> get(Class<T> type, Annotation annotation) {
		return new Key<T>(type, strategyFor(annotation));
	}

	/**
	 * Gets the.
	 *
	 * @param type the type
	 * @return the key
	 */
	public static Key<?> get(Type type) {
		return new Key<Object>(type, NullAnnotationStrategy.INSTANCE);
	}

	/**
	 * Gets the.
	 *
	 * @param type the type
	 * @param annotationType the annotation type
	 * @return the key
	 */
	public static Key<?> get(Type type, Class<? extends Annotation> annotationType) {
		return new Key<Object>(type, strategyFor(annotationType));
	}

	/**
	 * Gets the.
	 *
	 * @param type the type
	 * @param annotation the annotation
	 * @return the key
	 */
	public static Key<?> get(Type type, Annotation annotation) {
		return new Key<Object>(type, strategyFor(annotation));
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param typeLiteral the type literal
	 * @return the key
	 */
	public static <T> Key<T> get(TypeLiteral<T> typeLiteral) {
		return new Key<T>(typeLiteral, NullAnnotationStrategy.INSTANCE);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param typeLiteral the type literal
	 * @param annotationType the annotation type
	 * @return the key
	 */
	public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Class<? extends Annotation> annotationType) {
		return new Key<T>(typeLiteral, strategyFor(annotationType));
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param typeLiteral the type literal
	 * @param annotation the annotation
	 * @return the key
	 */
	public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Annotation annotation) {
		return new Key<T>(typeLiteral, strategyFor(annotation));
	}

	/**
	 * Of type.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the key
	 */
	@SuppressWarnings("hiding")
	<T> Key<T> ofType(Class<T> type) {
		return new Key<T>(type, annotationStrategy);
	}

	/**
	 * Of type.
	 *
	 * @param type the type
	 * @return the key
	 */
	Key<?> ofType(Type type) {
		return new Key<Object>(type, annotationStrategy);
	}

	/**
	 * Of type.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the key
	 */
	@SuppressWarnings("hiding")
	<T> Key<T> ofType(TypeLiteral<T> type) {
		return new Key<T>(type, annotationStrategy);
	}

	/**
	 * Checks for attributes.
	 *
	 * @return true, if successful
	 */
	boolean hasAttributes() {
		return annotationStrategy.hasAttributes();
	}

	/**
	 * Without attributes.
	 *
	 * @return the key
	 */
	Key<T> withoutAttributes() {
		return new Key<T>(typeLiteral, annotationStrategy.withoutAttributes());
	}

	/**
	 * The Interface AnnotationStrategy.
	 *
	 * @author l.xue.nong
	 */
	interface AnnotationStrategy {

		/**
		 * Gets the annotation.
		 *
		 * @return the annotation
		 */
		Annotation getAnnotation();

		/**
		 * Gets the annotation type.
		 *
		 * @return the annotation type
		 */
		Class<? extends Annotation> getAnnotationType();

		/**
		 * Checks for attributes.
		 *
		 * @return true, if successful
		 */
		boolean hasAttributes();

		/**
		 * Without attributes.
		 *
		 * @return the annotation strategy
		 */
		AnnotationStrategy withoutAttributes();
	}

	/**
	 * Checks if is marker.
	 *
	 * @param annotationType the annotation type
	 * @return true, if is marker
	 */
	static boolean isMarker(Class<? extends Annotation> annotationType) {
		return annotationType.getDeclaredMethods().length == 0;
	}

	/**
	 * Strategy for.
	 *
	 * @param annotation the annotation
	 * @return the annotation strategy
	 */
	static AnnotationStrategy strategyFor(Annotation annotation) {
		Preconditions.checkNotNull(annotation, "annotation");
		Class<? extends Annotation> annotationType = annotation.annotationType();
		ensureRetainedAtRuntime(annotationType);
		ensureIsBindingAnnotation(annotationType);

		if (annotationType.getDeclaredMethods().length == 0) {
			return new AnnotationTypeStrategy(annotationType, annotation);
		}

		return new AnnotationInstanceStrategy(annotation);
	}

	/**
	 * Strategy for.
	 *
	 * @param annotationType the annotation type
	 * @return the annotation strategy
	 */
	static AnnotationStrategy strategyFor(Class<? extends Annotation> annotationType) {
		Preconditions.checkNotNull(annotationType, "annotation type");
		ensureRetainedAtRuntime(annotationType);
		ensureIsBindingAnnotation(annotationType);
		return new AnnotationTypeStrategy(annotationType, null);
	}

	/**
	 * Ensure retained at runtime.
	 *
	 * @param annotationType the annotation type
	 */
	private static void ensureRetainedAtRuntime(Class<? extends Annotation> annotationType) {
		Preconditions
				.checkArgument(Annotations.isRetainedAtRuntime(annotationType),
						"%s is not retained at runtime. Please annotate it with @Retention(RUNTIME).",
						annotationType.getName());
	}

	/**
	 * Ensure is binding annotation.
	 *
	 * @param annotationType the annotation type
	 */
	private static void ensureIsBindingAnnotation(Class<? extends Annotation> annotationType) {
		Preconditions
				.checkArgument(isBindingAnnotation(annotationType),
						"%s is not a binding annotation. Please annotate it with @BindingAnnotation.",
						annotationType.getName());
	}

	/**
	 * The Enum NullAnnotationStrategy.
	 *
	 * @author l.xue.nong
	 */
	static enum NullAnnotationStrategy implements AnnotationStrategy {

		/** The instance. */
		INSTANCE;

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#hasAttributes()
		 */
		public boolean hasAttributes() {
			return false;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#withoutAttributes()
		 */
		public AnnotationStrategy withoutAttributes() {
			throw new UnsupportedOperationException("Key already has no attributes.");
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotation()
		 */
		public Annotation getAnnotation() {
			return null;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotationType()
		 */
		public Class<? extends Annotation> getAnnotationType() {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return "[none]";
		}
	}

	/**
	 * The Class AnnotationInstanceStrategy.
	 *
	 * @author l.xue.nong
	 */
	static class AnnotationInstanceStrategy implements AnnotationStrategy {

		/** The annotation. */
		final Annotation annotation;

		/**
		 * Instantiates a new annotation instance strategy.
		 *
		 * @param annotation the annotation
		 */
		AnnotationInstanceStrategy(Annotation annotation) {
			this.annotation = Preconditions.checkNotNull(annotation, "annotation");
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#hasAttributes()
		 */
		public boolean hasAttributes() {
			return true;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#withoutAttributes()
		 */
		public AnnotationStrategy withoutAttributes() {
			return new AnnotationTypeStrategy(getAnnotationType(), annotation);
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotation()
		 */
		public Annotation getAnnotation() {
			return annotation;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotationType()
		 */
		public Class<? extends Annotation> getAnnotationType() {
			return annotation.annotationType();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof AnnotationInstanceStrategy)) {
				return false;
			}

			AnnotationInstanceStrategy other = (AnnotationInstanceStrategy) o;
			return annotation.equals(other.annotation);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return annotation.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return annotation.toString();
		}
	}

	/**
	 * The Class AnnotationTypeStrategy.
	 *
	 * @author l.xue.nong
	 */
	static class AnnotationTypeStrategy implements AnnotationStrategy {

		/** The annotation type. */
		final Class<? extends Annotation> annotationType;

		/** The annotation. */
		final Annotation annotation;

		/**
		 * Instantiates a new annotation type strategy.
		 *
		 * @param annotationType the annotation type
		 * @param annotation the annotation
		 */
		AnnotationTypeStrategy(Class<? extends Annotation> annotationType, Annotation annotation) {
			this.annotationType = Preconditions.checkNotNull(annotationType, "annotation type");
			this.annotation = annotation;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#hasAttributes()
		 */
		public boolean hasAttributes() {
			return false;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#withoutAttributes()
		 */
		public AnnotationStrategy withoutAttributes() {
			throw new UnsupportedOperationException("Key already has no attributes.");
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotation()
		 */
		public Annotation getAnnotation() {
			return annotation;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Key.AnnotationStrategy#getAnnotationType()
		 */
		public Class<? extends Annotation> getAnnotationType() {
			return annotationType;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof AnnotationTypeStrategy)) {
				return false;
			}

			AnnotationTypeStrategy other = (AnnotationTypeStrategy) o;
			return annotationType.equals(other.annotationType);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return annotationType.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "@" + annotationType.getName();
		}
	}

	/**
	 * Checks if is binding annotation.
	 *
	 * @param annotation the annotation
	 * @return true, if is binding annotation
	 */
	static boolean isBindingAnnotation(Annotation annotation) {
		return isBindingAnnotation(annotation.annotationType());
	}

	/**
	 * Checks if is binding annotation.
	 *
	 * @param annotationType the annotation type
	 * @return true, if is binding annotation
	 */
	static boolean isBindingAnnotation(Class<? extends Annotation> annotationType) {
		return annotationType.isAnnotationPresent(BindingAnnotation.class);
	}
}
