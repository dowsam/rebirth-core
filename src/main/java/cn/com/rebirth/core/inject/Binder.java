/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Binder.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.binder.AnnotatedBindingBuilder;
import cn.com.rebirth.core.inject.binder.AnnotatedConstantBindingBuilder;
import cn.com.rebirth.core.inject.binder.LinkedBindingBuilder;
import cn.com.rebirth.core.inject.matcher.Matcher;
import cn.com.rebirth.core.inject.spi.Message;
import cn.com.rebirth.core.inject.spi.TypeConverter;
import cn.com.rebirth.core.inject.spi.TypeListener;


/**
 * The Interface Binder.
 *
 * @author l.xue.nong
 */
public interface Binder {

	
	/**
	 * Bind scope.
	 *
	 * @param annotationType the annotation type
	 * @param scope the scope
	 */
	void bindScope(Class<? extends Annotation> annotationType, Scope scope);

	
	/**
	 * Bind.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the linked binding builder
	 */
	<T> LinkedBindingBuilder<T> bind(Key<T> key);

	
	/**
	 * Bind.
	 *
	 * @param <T> the generic type
	 * @param typeLiteral the type literal
	 * @return the annotated binding builder
	 */
	<T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral);

	
	/**
	 * Bind.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the annotated binding builder
	 */
	<T> AnnotatedBindingBuilder<T> bind(Class<T> type);

	
	/**
	 * Bind constant.
	 *
	 * @return the annotated constant binding builder
	 */
	AnnotatedConstantBindingBuilder bindConstant();

	
	/**
	 * Request injection.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param instance the instance
	 */
	<T> void requestInjection(TypeLiteral<T> type, T instance);

	
	/**
	 * Request injection.
	 *
	 * @param instance the instance
	 */
	void requestInjection(Object instance);

	
	/**
	 * Request static injection.
	 *
	 * @param types the types
	 */
	void requestStaticInjection(Class<?>... types);

	
	/**
	 * Install.
	 *
	 * @param module the module
	 */
	void install(Module module);

	
	/**
	 * Current stage.
	 *
	 * @return the stage
	 */
	Stage currentStage();

	
	/**
	 * Adds the error.
	 *
	 * @param message the message
	 * @param arguments the arguments
	 */
	void addError(String message, Object... arguments);

	
	/**
	 * Adds the error.
	 *
	 * @param t the t
	 */
	void addError(Throwable t);

	
	/**
	 * Adds the error.
	 *
	 * @param message the message
	 */
	void addError(Message message);

	
	/**
	 * Gets the provider.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the provider
	 */
	<T> Provider<T> getProvider(Key<T> key);

	
	/**
	 * Gets the provider.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the provider
	 */
	<T> Provider<T> getProvider(Class<T> type);

	
	/**
	 * Gets the members injector.
	 *
	 * @param <T> the generic type
	 * @param typeLiteral the type literal
	 * @return the members injector
	 */
	<T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral);

	
	/**
	 * Gets the members injector.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the members injector
	 */
	<T> MembersInjector<T> getMembersInjector(Class<T> type);

	
	/**
	 * Convert to types.
	 *
	 * @param typeMatcher the type matcher
	 * @param converter the converter
	 */
	void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter);

	
	/**
	 * Bind listener.
	 *
	 * @param typeMatcher the type matcher
	 * @param listener the listener
	 */
	void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener);

	
	/**
	 * With source.
	 *
	 * @param source the source
	 * @return the binder
	 */
	Binder withSource(Object source);

	
	/**
	 * Skip sources.
	 *
	 * @param classesToSkip the classes to skip
	 * @return the binder
	 */
	@SuppressWarnings("rawtypes")
	Binder skipSources(Class... classesToSkip);

	
	/**
	 * New private binder.
	 *
	 * @return the private binder
	 */
	PrivateBinder newPrivateBinder();
}
