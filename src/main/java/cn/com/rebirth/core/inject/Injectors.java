/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Injectors.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.rebirth.core.inject.matcher.Matcher;
import cn.com.rebirth.core.inject.name.Names;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.Sets;


/**
 * The Class Injectors.
 *
 * @author l.xue.nong
 */
public class Injectors {

	
	/**
	 * Gets the first error failure.
	 *
	 * @param e the e
	 * @return the first error failure
	 */
	public static Throwable getFirstErrorFailure(CreationException e) {
		if (e.getErrorMessages().isEmpty()) {
			return e;
		}
		
		for (Message message : e.getErrorMessages()) {
			if (message.getCause() != null) {
				return message.getCause();
			}
		}
		return e;
	}

	
	/**
	 * Gets the single instance of Injectors.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param type the type
	 * @param name the name
	 * @return single instance of Injectors
	 */
	public static <T> T getInstance(Injector injector, java.lang.Class<T> type, String name) {
		return injector.getInstance(Key.get(type, Names.named(name)));
	}

	
	/**
	 * Gets the instances of.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param baseClass the base class
	 * @return the instances of
	 */
	public static <T> Set<T> getInstancesOf(Injector injector, Class<T> baseClass) {
		Set<T> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && baseClass.isAssignableFrom(keyType)) {
				Binding<?> binding = entry.getValue();
				Object value = binding.getProvider().get();
				if (value != null) {
					T castValue = baseClass.cast(value);
					answer.add(castValue);
				}
			}
		}
		return answer;
	}

	
	/**
	 * Gets the instances of.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param matcher the matcher
	 * @return the instances of
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> getInstancesOf(Injector injector, Matcher<Class<?>> matcher) {
		Set<T> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && matcher.matches(keyType)) {
				Binding<?> binding = entry.getValue();
				Object value = binding.getProvider().get();
				answer.add((T) value);
			}
		}
		return answer;
	}

	
	/**
	 * Gets the providers of.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param matcher the matcher
	 * @return the providers of
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<Provider<T>> getProvidersOf(Injector injector, Matcher<Class<?>> matcher) {
		Set<Provider<T>> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && matcher.matches(keyType)) {
				Binding<?> binding = entry.getValue();
				answer.add((Provider<T>) binding.getProvider());
			}
		}
		return answer;
	}

	
	/**
	 * Gets the providers of.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param baseClass the base class
	 * @return the providers of
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<Provider<T>> getProvidersOf(Injector injector, Class<T> baseClass) {
		Set<Provider<T>> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && baseClass.isAssignableFrom(keyType)) {
				Binding<?> binding = entry.getValue();
				answer.add((Provider<T>) binding.getProvider());
			}
		}
		return answer;
	}

	
	/**
	 * Checks for binding.
	 *
	 * @param injector the injector
	 * @param matcher the matcher
	 * @return true, if successful
	 */
	public static boolean hasBinding(Injector injector, Matcher<Class<?>> matcher) {
		return !getBindingsOf(injector, matcher).isEmpty();
	}

	
	/**
	 * Checks for binding.
	 *
	 * @param injector the injector
	 * @param baseClass the base class
	 * @return true, if successful
	 */
	public static boolean hasBinding(Injector injector, Class<?> baseClass) {
		return !getBindingsOf(injector, baseClass).isEmpty();
	}

	
	/**
	 * Checks for binding.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @return true, if successful
	 */
	public static boolean hasBinding(Injector injector, Key<?> key) {
		Binding<?> binding = getBinding(injector, key);
		return binding != null;
	}

	
	/**
	 * Gets the binding.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @return the binding
	 */
	public static Binding<?> getBinding(Injector injector, Key<?> key) {
		Map<Key<?>, Binding<?>> bindings = injector.getBindings();
		Binding<?> binding = bindings.get(key);
		return binding;
	}

	
	/**
	 * Gets the bindings of.
	 *
	 * @param injector the injector
	 * @param matcher the matcher
	 * @return the bindings of
	 */
	public static Set<Binding<?>> getBindingsOf(Injector injector, Matcher<Class<?>> matcher) {
		Set<Binding<?>> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && matcher.matches(keyType)) {
				answer.add(entry.getValue());
			}
		}
		return answer;
	}

	
	/**
	 * Gets the bindings of.
	 *
	 * @param injector the injector
	 * @param baseClass the base class
	 * @return the bindings of
	 */
	public static Set<Binding<?>> getBindingsOf(Injector injector, Class<?> baseClass) {
		Set<Binding<?>> answer = Sets.newHashSet();
		Set<Entry<Key<?>, Binding<?>>> entries = injector.getBindings().entrySet();
		for (Entry<Key<?>, Binding<?>> entry : entries) {
			Key<?> key = entry.getKey();
			Class<?> keyType = getKeyType(key);
			if (keyType != null && baseClass.isAssignableFrom(keyType)) {
				answer.add(entry.getValue());
			}
		}
		return answer;
	}

	
	/**
	 * Gets the key type.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the key type
	 */
	public static <T> Class<?> getKeyType(Key<?> key) {
		Class<?> keyType = null;
		TypeLiteral<?> typeLiteral = key.getTypeLiteral();
		Type type = typeLiteral.getType();
		if (type instanceof Class) {
			keyType = (Class<?>) type;
		}
		return keyType;
	}

	
	/**
	 * Close.
	 *
	 * @param injector the injector
	 */
	public static void close(Injector injector) {

	}

	
	/**
	 * Clean caches.
	 *
	 * @param injector the injector
	 */
	public static void cleanCaches(Injector injector) {
		((InjectorImpl) injector).clearCache();
		if (injector.getParent() != null) {
			cleanCaches(injector.getParent());
		}
	}
}
