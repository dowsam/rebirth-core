/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Scoping.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.Scope;
import cn.com.rebirth.core.inject.Scopes;
import cn.com.rebirth.core.inject.Singleton;
import cn.com.rebirth.core.inject.Stage;
import cn.com.rebirth.core.inject.binder.ScopedBindingBuilder;
import cn.com.rebirth.core.inject.spi.BindingScopingVisitor;


/**
 * The Class Scoping.
 *
 * @author l.xue.nong
 */
public abstract class Scoping {

	
	/** The Constant UNSCOPED. */
	public static final Scoping UNSCOPED = new Scoping() {
		public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
			return visitor.visitNoScoping();
		}

		@Override
		public Scope getScopeInstance() {
			return Scopes.NO_SCOPE;
		}

		@Override
		public String toString() {
			return Scopes.NO_SCOPE.toString();
		}

		public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
			
		}
	};

	
	/** The Constant SINGLETON_ANNOTATION. */
	public static final Scoping SINGLETON_ANNOTATION = new Scoping() {
		public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
			return visitor.visitScopeAnnotation(Singleton.class);
		}

		@Override
		public Class<? extends Annotation> getScopeAnnotation() {
			return Singleton.class;
		}

		@Override
		public String toString() {
			return Singleton.class.getName();
		}

		public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
			scopedBindingBuilder.in(Singleton.class);
		}
	};

	
	/** The Constant SINGLETON_INSTANCE. */
	public static final Scoping SINGLETON_INSTANCE = new Scoping() {
		public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
			return visitor.visitScope(Scopes.SINGLETON);
		}

		@Override
		public Scope getScopeInstance() {
			return Scopes.SINGLETON;
		}

		@Override
		public String toString() {
			return Scopes.SINGLETON.toString();
		}

		public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
			scopedBindingBuilder.in(Scopes.SINGLETON);
		}
	};

	
	/** The Constant EAGER_SINGLETON. */
	public static final Scoping EAGER_SINGLETON = new Scoping() {
		public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
			return visitor.visitEagerSingleton();
		}

		@Override
		public Scope getScopeInstance() {
			return Scopes.SINGLETON;
		}

		@Override
		public String toString() {
			return "eager singleton";
		}

		public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
			scopedBindingBuilder.asEagerSingleton();
		}
	};

	
	/**
	 * For annotation.
	 *
	 * @param scopingAnnotation the scoping annotation
	 * @return the scoping
	 */
	public static Scoping forAnnotation(final Class<? extends Annotation> scopingAnnotation) {
		if (scopingAnnotation == Singleton.class) {
			return SINGLETON_ANNOTATION;
		}

		return new Scoping() {
			public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
				return visitor.visitScopeAnnotation(scopingAnnotation);
			}

			@Override
			public Class<? extends Annotation> getScopeAnnotation() {
				return scopingAnnotation;
			}

			@Override
			public String toString() {
				return scopingAnnotation.getName();
			}

			public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
				scopedBindingBuilder.in(scopingAnnotation);
			}
		};
	}

	
	/**
	 * For instance.
	 *
	 * @param scope the scope
	 * @return the scoping
	 */
	public static Scoping forInstance(final Scope scope) {
		if (scope == Scopes.SINGLETON) {
			return SINGLETON_INSTANCE;
		}

		return new Scoping() {
			public <V> V acceptVisitor(BindingScopingVisitor<V> visitor) {
				return visitor.visitScope(scope);
			}

			@Override
			public Scope getScopeInstance() {
				return scope;
			}

			@Override
			public String toString() {
				return scope.toString();
			}

			public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
				scopedBindingBuilder.in(scope);
			}
		};
	}

	
	/**
	 * Checks if is explicitly scoped.
	 *
	 * @return true, if is explicitly scoped
	 */
	public boolean isExplicitlyScoped() {
		return this != UNSCOPED;
	}

	
	/**
	 * Checks if is no scope.
	 *
	 * @return true, if is no scope
	 */
	public boolean isNoScope() {
		return getScopeInstance() == Scopes.NO_SCOPE;
	}

	
	/**
	 * Checks if is eager singleton.
	 *
	 * @param stage the stage
	 * @return true, if is eager singleton
	 */
	public boolean isEagerSingleton(Stage stage) {
		if (this == EAGER_SINGLETON) {
			return true;
		}

		if (stage == Stage.PRODUCTION) {
			return this == SINGLETON_ANNOTATION || this == SINGLETON_INSTANCE;
		}

		return false;
	}

	
	/**
	 * Gets the scope instance.
	 *
	 * @return the scope instance
	 */
	public Scope getScopeInstance() {
		return null;
	}

	
	/**
	 * Gets the scope annotation.
	 *
	 * @return the scope annotation
	 */
	public Class<? extends Annotation> getScopeAnnotation() {
		return null;
	}

	
	/**
	 * Accept visitor.
	 *
	 * @param <V> the value type
	 * @param visitor the visitor
	 * @return the v
	 */
	public abstract <V> V acceptVisitor(BindingScopingVisitor<V> visitor);

	
	/**
	 * Apply to.
	 *
	 * @param scopedBindingBuilder the scoped binding builder
	 */
	public abstract void applyTo(ScopedBindingBuilder scopedBindingBuilder);

	
	/**
	 * Instantiates a new scoping.
	 */
	private Scoping() {
	}
}
