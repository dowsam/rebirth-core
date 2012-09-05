/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeEncounter.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.MembersInjector;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.TypeLiteral;


/**
 * The Interface TypeEncounter.
 *
 * @param <I> the generic type
 * @author l.xue.nong
 */
public interface TypeEncounter<I> {

	
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
	 * Register.
	 *
	 * @param membersInjector the members injector
	 */
	void register(MembersInjector<? super I> membersInjector);

	
	/**
	 * Register.
	 *
	 * @param listener the listener
	 */
	void register(InjectionListener<? super I> listener);
}
