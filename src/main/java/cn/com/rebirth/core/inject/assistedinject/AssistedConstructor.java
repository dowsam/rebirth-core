/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AssistedConstructor.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.TypeLiteral;

import com.google.common.collect.Lists;


/**
 * The Class AssistedConstructor.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class AssistedConstructor<T> {

	
	/** The constructor. */
	private final Constructor<T> constructor;

	
	/** The assisted parameters. */
	private final ParameterListKey assistedParameters;

	
	/** The all parameters. */
	private final List<Parameter> allParameters;

	
	/**
	 * Instantiates a new assisted constructor.
	 *
	 * @param constructor the constructor
	 * @param parameterTypes the parameter types
	 */
	public AssistedConstructor(Constructor<T> constructor, List<TypeLiteral<?>> parameterTypes) {
		this.constructor = constructor;

		Annotation[][] annotations = constructor.getParameterAnnotations();

		List<Type> typeList = Lists.newArrayList();
		allParameters = new ArrayList<Parameter>();

		
		for (int i = 0; i < parameterTypes.size(); i++) {
			Parameter parameter = new Parameter(parameterTypes.get(i).getType(), annotations[i]);
			allParameters.add(parameter);
			if (parameter.isProvidedByFactory()) {
				typeList.add(parameter.getType());
			}
		}
		this.assistedParameters = new ParameterListKey(typeList);
	}

	
	/**
	 * Gets the assisted parameters.
	 *
	 * @return the assisted parameters
	 */
	public ParameterListKey getAssistedParameters() {
		return assistedParameters;
	}

	
	/**
	 * Gets the all parameters.
	 *
	 * @return the all parameters
	 */
	public List<Parameter> getAllParameters() {
		return allParameters;
	}

	
	/**
	 * Gets the declared exceptions.
	 *
	 * @return the declared exceptions
	 */
	public Set<Class<?>> getDeclaredExceptions() {
		return new HashSet<Class<?>>(Arrays.asList(constructor.getExceptionTypes()));
	}

	
	/**
	 * New instance.
	 *
	 * @param args the args
	 * @return the t
	 * @throws Throwable the throwable
	 */
	public T newInstance(Object[] args) throws Throwable {
		constructor.setAccessible(true);
		try {
			return constructor.newInstance(args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return constructor.toString();
	}
}
