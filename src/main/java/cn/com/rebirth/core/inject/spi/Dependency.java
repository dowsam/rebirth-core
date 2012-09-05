/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Dependency.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.internal.Objects;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * The Class Dependency.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class Dependency<T> {

	
	/** The injection point. */
	private final InjectionPoint injectionPoint;

	
	/** The key. */
	private final Key<T> key;

	
	/** The nullable. */
	private final boolean nullable;

	
	/** The parameter index. */
	private final int parameterIndex;

	
	/**
	 * Instantiates a new dependency.
	 *
	 * @param injectionPoint the injection point
	 * @param key the key
	 * @param nullable the nullable
	 * @param parameterIndex the parameter index
	 */
	Dependency(InjectionPoint injectionPoint, Key<T> key, boolean nullable, int parameterIndex) {
		this.injectionPoint = injectionPoint;
		this.key = key;
		this.nullable = nullable;
		this.parameterIndex = parameterIndex;
	}

	
	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the dependency
	 */
	public static <T> Dependency<T> get(Key<T> key) {
		return new Dependency<T>(null, key, true, -1);
	}

	
	/**
	 * For injection points.
	 *
	 * @param injectionPoints the injection points
	 * @return the sets the
	 */
	public static Set<Dependency<?>> forInjectionPoints(Set<InjectionPoint> injectionPoints) {
		List<Dependency<?>> dependencies = Lists.newArrayList();
		for (InjectionPoint injectionPoint : injectionPoints) {
			dependencies.addAll(injectionPoint.getDependencies());
		}
		return ImmutableSet.copyOf(dependencies);
	}

	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Key<T> getKey() {
		return this.key;
	}

	
	/**
	 * Checks if is nullable.
	 *
	 * @return true, if is nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	
	/**
	 * Gets the injection point.
	 *
	 * @return the injection point
	 */
	public InjectionPoint getInjectionPoint() {
		return injectionPoint;
	}

	
	/**
	 * Gets the parameter index.
	 *
	 * @return the parameter index
	 */
	public int getParameterIndex() {
		return parameterIndex;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(injectionPoint, parameterIndex, key);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (o instanceof Dependency) {
			Dependency dependency = (Dependency) o;
			return Objects.equal(injectionPoint, dependency.injectionPoint)
					&& Objects.equal(parameterIndex, dependency.parameterIndex) && Objects.equal(key, dependency.key);
		} else {
			return false;
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(key);
		if (injectionPoint != null) {
			builder.append("@").append(injectionPoint);
			if (parameterIndex != -1) {
				builder.append("[").append(parameterIndex).append("]");
			}
		}
		return builder.toString();
	}
}
