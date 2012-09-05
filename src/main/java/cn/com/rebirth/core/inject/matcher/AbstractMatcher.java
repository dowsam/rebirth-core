/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AbstractMatcher.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.matcher;

import java.io.Serializable;


/**
 * The Class AbstractMatcher.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public abstract class AbstractMatcher<T> implements Matcher<T> {

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#and(cn.com.rebirth.search.commons.inject.matcher.Matcher)
	 */
	public Matcher<T> and(final Matcher<? super T> other) {
		return new AndMatcher<T>(this, other);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#or(cn.com.rebirth.search.commons.inject.matcher.Matcher)
	 */
	public Matcher<T> or(Matcher<? super T> other) {
		return new OrMatcher<T>(this, other);
	}

	
	/**
	 * The Class AndMatcher.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static class AndMatcher<T> extends AbstractMatcher<T> implements Serializable {

		
		/** The b. */
		private final Matcher<? super T> a, b;

		
		/**
		 * Instantiates a new and matcher.
		 *
		 * @param a the a
		 * @param b the b
		 */
		public AndMatcher(Matcher<? super T> a, Matcher<? super T> b) {
			this.a = a;
			this.b = b;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(T t) {
			return a.matches(t) && b.matches(t);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object other) {
			return other instanceof AndMatcher && ((AndMatcher) other).a.equals(a) && ((AndMatcher) other).b.equals(b);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 41 * (a.hashCode() ^ b.hashCode());
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "and(" + a + ", " + b + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * The Class OrMatcher.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static class OrMatcher<T> extends AbstractMatcher<T> implements Serializable {

		
		/** The b. */
		private final Matcher<? super T> a, b;

		
		/**
		 * Instantiates a new or matcher.
		 *
		 * @param a the a
		 * @param b the b
		 */
		public OrMatcher(Matcher<? super T> a, Matcher<? super T> b) {
			this.a = a;
			this.b = b;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.matcher.Matcher#matches(java.lang.Object)
		 */
		public boolean matches(T t) {
			return a.matches(t) || b.matches(t);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object other) {
			return other instanceof OrMatcher && ((OrMatcher) other).a.equals(a) && ((OrMatcher) other).b.equals(b);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 37 * (a.hashCode() ^ b.hashCode());
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "or(" + a + ", " + b + ")";
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}
}
