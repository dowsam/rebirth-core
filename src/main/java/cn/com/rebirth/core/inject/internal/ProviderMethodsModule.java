/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderMethodsModule.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Module;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.Provides;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.Message;
import cn.com.rebirth.core.inject.util.Modules;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * The Class ProviderMethodsModule.
 *
 * @author l.xue.nong
 */
public final class ProviderMethodsModule implements Module {

	
	/** The delegate. */
	private final Object delegate;

	
	/** The type literal. */
	private final TypeLiteral<?> typeLiteral;

	
	/**
	 * Instantiates a new provider methods module.
	 *
	 * @param delegate the delegate
	 */
	private ProviderMethodsModule(Object delegate) {
		this.delegate = checkNotNull(delegate, "delegate");
		this.typeLiteral = TypeLiteral.get(this.delegate.getClass());
	}

	
	/**
	 * For module.
	 *
	 * @param module the module
	 * @return the module
	 */
	public static Module forModule(Module module) {
		return forObject(module);
	}

	
	/**
	 * For object.
	 *
	 * @param object the object
	 * @return the module
	 */
	public static Module forObject(Object object) {
		
		if (object instanceof ProviderMethodsModule) {
			return Modules.EMPTY_MODULE;
		}

		return new ProviderMethodsModule(object);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Module#configure(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public synchronized void configure(Binder binder) {
		for (ProviderMethod<?> providerMethod : getProviderMethods(binder)) {
			providerMethod.configure(binder);
		}
	}

	
	/**
	 * Gets the provider methods.
	 *
	 * @param binder the binder
	 * @return the provider methods
	 */
	public List<ProviderMethod<?>> getProviderMethods(Binder binder) {
		List<ProviderMethod<?>> result = Lists.newArrayList();
		for (Class<?> c = delegate.getClass(); c != Object.class; c = c.getSuperclass()) {
			for (Method method : c.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Provides.class)) {
					result.add(createProviderMethod(binder, method));
				}
			}
		}
		return result;
	}

	
	/**
	 * Creates the provider method.
	 *
	 * @param <T> the generic type
	 * @param binder the binder
	 * @param method the method
	 * @return the provider method
	 */
	<T> ProviderMethod<T> createProviderMethod(Binder binder, final Method method) {
		binder = binder.withSource(method);
		Errors errors = new Errors(method);

		
		List<Dependency<?>> dependencies = Lists.newArrayList();
		List<Provider<?>> parameterProviders = Lists.newArrayList();
		List<TypeLiteral<?>> parameterTypes = typeLiteral.getParameterTypes(method);
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.size(); i++) {
			Key<?> key = getKey(errors, parameterTypes.get(i), method, parameterAnnotations[i]);
			dependencies.add(Dependency.get(key));
			parameterProviders.add(binder.getProvider(key));
		}

		@SuppressWarnings("unchecked")
		
		TypeLiteral<T> returnType = (TypeLiteral<T>) typeLiteral.getReturnType(method);

		Key<T> key = getKey(errors, returnType, method, method.getAnnotations());
		Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, method.getAnnotations());

		for (Message message : errors.getMessages()) {
			binder.addError(message);
		}

		return new ProviderMethod<T>(key, method, delegate, ImmutableSet.copyOf(dependencies), parameterProviders,
				scopeAnnotation);
	}

	
	/**
	 * Gets the key.
	 *
	 * @param <T> the generic type
	 * @param errors the errors
	 * @param type the type
	 * @param member the member
	 * @param annotations the annotations
	 * @return the key
	 */
	<T> Key<T> getKey(Errors errors, TypeLiteral<T> type, Member member, Annotation[] annotations) {
		Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors, member, annotations);
		return bindingAnnotation == null ? Key.get(type) : Key.get(type, bindingAnnotation);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof ProviderMethodsModule && ((ProviderMethodsModule) o).delegate == delegate;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}
}
