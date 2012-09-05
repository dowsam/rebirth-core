/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Matchers.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.matcher;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The Class Matchers.
 *
 * @author l.xue.nong
 */
public class Matchers {

	
	/**
	 * Instantiates a new matchers.
	 */
	private Matchers() {
	}

	
	/**
	 * Any.
	 *
	 * @return the matcher
	 */
	public static Matcher<Object> any() {
		return ANY;
	}

	
	/** The Constant ANY. */
	private static final Matcher<Object> ANY = new Any();

	
	/**
	 * The Class Any.
	 *
	 * @author l.xue.nong
	 */
	private static class Any extends AbstractMatcher<Object> implements Serializable {

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Object o) {
			return true;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "any()";
		}

		
		/**
		 * Read resolve.
		 *
		 * @return the object
		 */
		public Object readResolve() {
			return any();
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Not.
	 *
	 * @param <T> the generic type
	 * @param p the p
	 * @return the matcher
	 */
	public static <T> Matcher<T> not(final Matcher<? super T> p) {
		return new Not<T>(p);
	}

	
	/**
	 * The Class Not.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static class Not<T> extends AbstractMatcher<T> implements Serializable {

		
		/** The delegate. */
		final Matcher<? super T> delegate;

		
		/**
		 * Instantiates a new not.
		 *
		 * @param delegate the delegate
		 */
		private Not(Matcher<? super T> delegate) {
			this.delegate = checkNotNull(delegate, "delegate");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(T t) {
			return !delegate.matches(t);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object other) {
			return other instanceof Not && ((Not) other).delegate.equals(delegate);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return -delegate.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "not(" + delegate + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Check for runtime retention.
	 *
	 * @param annotationType the annotation type
	 */
	private static void checkForRuntimeRetention(Class<? extends Annotation> annotationType) {
		Retention retention = annotationType.getAnnotation(Retention.class);
		checkArgument(retention != null && retention.value() == RetentionPolicy.RUNTIME,
				"Annotation " + annotationType.getSimpleName() + " is missing RUNTIME retention");
	}

	
	/**
	 * Annotated with.
	 *
	 * @param annotationType the annotation type
	 * @return the matcher
	 */
	public static Matcher<AnnotatedElement> annotatedWith(final Class<? extends Annotation> annotationType) {
		return new AnnotatedWithType(annotationType);
	}

	
	/**
	 * The Class AnnotatedWithType.
	 *
	 * @author l.xue.nong
	 */
	private static class AnnotatedWithType extends AbstractMatcher<AnnotatedElement> implements Serializable {

		
		/** The annotation type. */
		private final Class<? extends Annotation> annotationType;

		
		/**
		 * Instantiates a new annotated with type.
		 *
		 * @param annotationType the annotation type
		 */
		public AnnotatedWithType(Class<? extends Annotation> annotationType) {
			this.annotationType = checkNotNull(annotationType, "annotation type");
			checkForRuntimeRetention(annotationType);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(AnnotatedElement element) {
			return element.getAnnotation(annotationType) != null;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof AnnotatedWithType
					&& ((AnnotatedWithType) other).annotationType.equals(annotationType);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * annotationType.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "annotatedWith(" + annotationType.getSimpleName() + ".class)";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Annotated with.
	 *
	 * @param annotation the annotation
	 * @return the matcher
	 */
	public static Matcher<AnnotatedElement> annotatedWith(final Annotation annotation) {
		return new AnnotatedWith(annotation);
	}

	
	/**
	 * The Class AnnotatedWith.
	 *
	 * @author l.xue.nong
	 */
	private static class AnnotatedWith extends AbstractMatcher<AnnotatedElement> implements Serializable {

		
		/** The annotation. */
		private final Annotation annotation;

		
		/**
		 * Instantiates a new annotated with.
		 *
		 * @param annotation the annotation
		 */
		public AnnotatedWith(Annotation annotation) {
			this.annotation = checkNotNull(annotation, "annotation");
			checkForRuntimeRetention(annotation.annotationType());
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(AnnotatedElement element) {
			Annotation fromElement = element.getAnnotation(annotation.annotationType());
			return fromElement != null && annotation.equals(fromElement);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof AnnotatedWith && ((AnnotatedWith) other).annotation.equals(annotation);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * annotation.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "annotatedWith(" + annotation + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Subclasses of.
	 *
	 * @param superclass the superclass
	 * @return the matcher
	 */
	@SuppressWarnings("rawtypes")
	public static Matcher<Class> subclassesOf(final Class<?> superclass) {
		return new SubclassesOf(superclass);
	}

	
	/**
	 * The Class SubclassesOf.
	 *
	 * @author l.xue.nong
	 */
	@SuppressWarnings("rawtypes")
	private static class SubclassesOf extends AbstractMatcher<Class> implements Serializable {

		
		/** The superclass. */
		private final Class<?> superclass;

		
		/**
		 * Instantiates a new subclasses of.
		 *
		 * @param superclass the superclass
		 */
		public SubclassesOf(Class<?> superclass) {
			this.superclass = checkNotNull(superclass, "superclass");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Class subclass) {
			return superclass.isAssignableFrom(subclass);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof SubclassesOf && ((SubclassesOf) other).superclass.equals(superclass);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * superclass.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "subclassesOf(" + superclass.getSimpleName() + ".class)";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Only.
	 *
	 * @param value the value
	 * @return the matcher
	 */
	public static Matcher<Object> only(Object value) {
		return new Only(value);
	}

	
	/**
	 * The Class Only.
	 *
	 * @author l.xue.nong
	 */
	private static class Only extends AbstractMatcher<Object> implements Serializable {

		
		/** The value. */
		private final Object value;

		
		/**
		 * Instantiates a new only.
		 *
		 * @param value the value
		 */
		public Only(Object value) {
			this.value = checkNotNull(value, "value");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Object other) {
			return value.equals(other);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof Only && ((Only) other).value.equals(value);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * value.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "only(" + value + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Identical to.
	 *
	 * @param value the value
	 * @return the matcher
	 */
	public static Matcher<Object> identicalTo(final Object value) {
		return new IdenticalTo(value);
	}

	
	/**
	 * The Class IdenticalTo.
	 *
	 * @author l.xue.nong
	 */
	private static class IdenticalTo extends AbstractMatcher<Object> implements Serializable {

		
		/** The value. */
		private final Object value;

		
		/**
		 * Instantiates a new identical to.
		 *
		 * @param value the value
		 */
		public IdenticalTo(Object value) {
			this.value = checkNotNull(value, "value");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Object other) {
			return value == other;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof IdenticalTo && ((IdenticalTo) other).value == value;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * System.identityHashCode(value);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "identicalTo(" + value + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * In package.
	 *
	 * @param targetPackage the target package
	 * @return the matcher
	 */
	@SuppressWarnings("rawtypes")
	public static Matcher<Class> inPackage(final Package targetPackage) {
		return new InPackage(targetPackage);
	}

	
	/**
	 * The Class InPackage.
	 *
	 * @author l.xue.nong
	 */
	@SuppressWarnings("rawtypes")
	private static class InPackage extends AbstractMatcher<Class> implements Serializable {

		
		/** The target package. */
		private final transient Package targetPackage;

		
		/** The package name. */
		private final String packageName;

		
		/**
		 * Instantiates a new in package.
		 *
		 * @param targetPackage the target package
		 */
		public InPackage(Package targetPackage) {
			this.targetPackage = checkNotNull(targetPackage, "package");
			this.packageName = targetPackage.getName();
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Class c) {
			return c.getPackage().equals(targetPackage);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof InPackage && ((InPackage) other).targetPackage.equals(targetPackage);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * targetPackage.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "inPackage(" + targetPackage.getName() + ")";
		}

		
		/**
		 * Read resolve.
		 *
		 * @return the object
		 */
		public Object readResolve() {
			return inPackage(Package.getPackage(packageName));
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * In subpackage.
	 *
	 * @param targetPackageName the target package name
	 * @return the matcher
	 */
	@SuppressWarnings("rawtypes")
	public static Matcher<Class> inSubpackage(final String targetPackageName) {
		return new InSubpackage(targetPackageName);
	}

	
	/**
	 * The Class InSubpackage.
	 *
	 * @author l.xue.nong
	 */
	@SuppressWarnings("rawtypes")
	private static class InSubpackage extends AbstractMatcher<Class> implements Serializable {

		
		/** The target package name. */
		private final String targetPackageName;

		
		/**
		 * Instantiates a new in subpackage.
		 *
		 * @param targetPackageName the target package name
		 */
		public InSubpackage(String targetPackageName) {
			this.targetPackageName = targetPackageName;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Class c) {
			String classPackageName = c.getPackage().getName();
			return classPackageName.equals(targetPackageName) || classPackageName.startsWith(targetPackageName + ".");
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof InSubpackage && ((InSubpackage) other).targetPackageName.equals(targetPackageName);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * targetPackageName.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "inSubpackage(" + targetPackageName + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Returns.
	 *
	 * @param returnType the return type
	 * @return the matcher
	 */
	public static Matcher<Method> returns(final Matcher<? super Class<?>> returnType) {
		return new Returns(returnType);
	}

	
	/**
	 * The Class Returns.
	 *
	 * @author l.xue.nong
	 */
	private static class Returns extends AbstractMatcher<Method> implements Serializable {

		
		/** The return type. */
		private final Matcher<? super Class<?>> returnType;

		
		/**
		 * Instantiates a new returns.
		 *
		 * @param returnType the return type
		 */
		public Returns(Matcher<? super Class<?>> returnType) {
			this.returnType = checkNotNull(returnType, "return type matcher");
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(Method m) {
			return returnType.matches(m.getReturnType());
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof Returns && ((Returns) other).returnType.equals(returnType);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * returnType.hashCode();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "returns(" + returnType + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}
}
