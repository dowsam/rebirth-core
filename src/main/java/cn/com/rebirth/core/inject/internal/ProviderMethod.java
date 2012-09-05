/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderMethod.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Exposed;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.PrivateBinder;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.ProviderWithDependencies;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ProviderMethod.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class ProviderMethod<T> implements ProviderWithDependencies<T> {

	
	/** The key. */
	private final Key<T> key;

	
	/** The scope annotation. */
	private final Class<? extends Annotation> scopeAnnotation;

	
	/** The instance. */
	private final Object instance;

	
	/** The method. */
	private final Method method;

	
	/** The dependencies. */
	private final ImmutableSet<Dependency<?>> dependencies;

	
	/** The parameter providers. */
	private final List<Provider<?>> parameterProviders;

	
	/** The exposed. */
	private final boolean exposed;

	
	/**
	 * Instantiates a new provider method.
	 *
	 * @param key the key
	 * @param method the method
	 * @param instance the instance
	 * @param dependencies the dependencies
	 * @param parameterProviders the parameter providers
	 * @param scopeAnnotation the scope annotation
	 */
	ProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies,
			List<Provider<?>> parameterProviders, Class<? extends Annotation> scopeAnnotation) {
		this.key = key;
		this.scopeAnnotation = scopeAnnotation;
		this.instance = instance;
		this.dependencies = dependencies;
		this.method = method;
		this.parameterProviders = parameterProviders;
		this.exposed = method.isAnnotationPresent(Exposed.class);

		method.setAccessible(true);
	}

	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Key<T> getKey() {
		return key;
	}

	
	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	
	
	/**
	 * Gets the single instance of ProviderMethod.
	 *
	 * @return single instance of ProviderMethod
	 */
	public Object getInstance() {
		return instance;
	}

	
	/**
	 * Configure.
	 *
	 * @param binder the binder
	 */
	public void configure(Binder binder) {
		binder = binder.withSource(method);

		if (scopeAnnotation != null) {
			binder.bind(key).toProvider(this).in(scopeAnnotation);
		} else {
			binder.bind(key).toProvider(this);
		}

		if (exposed) {
			
			
			((PrivateBinder) binder).expose(key);
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Provider#get()
	 */
	public T get() {
		Object[] parameters = new Object[parameterProviders.size()];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = parameterProviders.get(i).get();
		}

		try {
			
			@SuppressWarnings({ "unchecked" })
			T result = (T) method.invoke(instance, parameters);
			return result;
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.HasDependencies#getDependencies()
	 */
	public Set<Dependency<?>> getDependencies() {
		return dependencies;
	}
}
