/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Parameter.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.com.rebirth.core.inject.BindingAnnotation;
import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Class Parameter.
 *
 * @author l.xue.nong
 */
class Parameter {

	
	/** The type. */
	private final Type type;

	
	/** The is assisted. */
	private final boolean isAssisted;

	
	/** The binding annotation. */
	private final Annotation bindingAnnotation;

	
	/** The is provider. */
	private final boolean isProvider;

	
	/**
	 * Instantiates a new parameter.
	 *
	 * @param type the type
	 * @param annotations the annotations
	 */
	public Parameter(Type type, Annotation[] annotations) {
		this.type = type;
		this.bindingAnnotation = getBindingAnnotation(annotations);
		this.isAssisted = hasAssistedAnnotation(annotations);
		this.isProvider = isProvider(type);
	}

	
	/**
	 * Checks if is provided by factory.
	 *
	 * @return true, if is provided by factory
	 */
	public boolean isProvidedByFactory() {
		return isAssisted;
	}

	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (isAssisted) {
			result.append("@Assisted");
			result.append(" ");
		}
		if (bindingAnnotation != null) {
			result.append(bindingAnnotation.toString());
			result.append(" ");
		}
		result.append(type.toString());
		return result.toString();
	}

	
	/**
	 * Checks for assisted annotation.
	 *
	 * @param annotations the annotations
	 * @return true, if successful
	 */
	private boolean hasAssistedAnnotation(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(Assisted.class)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Gets the value.
	 *
	 * @param injector the injector
	 * @return the value
	 */
	public Object getValue(Injector injector) {
		return isProvider ? injector.getProvider(getBindingForType(getProvidedType(type))) : injector
				.getInstance(getPrimaryBindingKey());
	}

	
	/**
	 * Checks if is bound.
	 *
	 * @param injector the injector
	 * @return true, if is bound
	 */
	public boolean isBound(Injector injector) {
		return isBound(injector, getPrimaryBindingKey()) || isBound(injector, fixAnnotations(getPrimaryBindingKey()));
	}

	
	/**
	 * Checks if is bound.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @return true, if is bound
	 */
	private boolean isBound(Injector injector, Key<?> key) {
		
		
		try {
			return injector.getBinding(key) != null;
		} catch (ConfigurationException e) {
			return false;
		}
	}

	
	/**
	 * Fix annotations.
	 *
	 * @param key the key
	 * @return the key
	 */
	public Key<?> fixAnnotations(Key<?> key) {
		return key.getAnnotation() == null ? key : Key.get(key.getTypeLiteral(), key.getAnnotation().annotationType());
	}

	
	/**
	 * Gets the primary binding key.
	 *
	 * @return the primary binding key
	 */
	Key<?> getPrimaryBindingKey() {
		return isProvider ? getBindingForType(getProvidedType(type)) : getBindingForType(type);
	}

	
	/**
	 * Gets the provided type.
	 *
	 * @param type the type
	 * @return the provided type
	 */
	private Type getProvidedType(Type type) {
		return ((ParameterizedType) type).getActualTypeArguments()[0];
	}

	
	/**
	 * Checks if is provider.
	 *
	 * @param type the type
	 * @return true, if is provider
	 */
	private boolean isProvider(Type type) {
		return type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Provider.class;
	}

	
	/**
	 * Gets the binding for type.
	 *
	 * @param type the type
	 * @return the binding for type
	 */
	private Key<?> getBindingForType(Type type) {
		return bindingAnnotation != null ? Key.get(type, bindingAnnotation) : Key.get(type);
	}

	
	/**
	 * Gets the binding annotation.
	 *
	 * @param annotations the annotations
	 * @return the binding annotation
	 */
	private Annotation getBindingAnnotation(Annotation[] annotations) {
		Annotation bindingAnnotation = null;
		for (Annotation a : annotations) {
			if (a.annotationType().getAnnotation(BindingAnnotation.class) != null) {
				checkArgument(bindingAnnotation == null, "Parameter has multiple binding annotations: %s and %s",
						bindingAnnotation, a);
				bindingAnnotation = a;
			}
		}
		return bindingAnnotation;
	}
}
