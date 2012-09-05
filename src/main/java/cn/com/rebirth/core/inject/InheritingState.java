/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InheritingState.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.MatcherAndConverter;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The Class InheritingState.
 *
 * @author l.xue.nong
 */
class InheritingState implements State {

	
	/** The parent. */
	private final State parent;

	
	
	/** The explicit bindings mutable. */
	private final Map<Key<?>, Binding<?>> explicitBindingsMutable = Maps.newLinkedHashMap();

	
	/** The explicit bindings. */
	private final Map<Key<?>, Binding<?>> explicitBindings = Collections.unmodifiableMap(explicitBindingsMutable);

	
	/** The scopes. */
	private final Map<Class<? extends Annotation>, Scope> scopes = Maps.newHashMap();

	
	/** The converters. */
	private final List<MatcherAndConverter> converters = Lists.newArrayList();

	
	/** The listener bindings. */
	private final List<TypeListenerBinding> listenerBindings = Lists.newArrayList();

	
	/** The blacklisted keys. */
	private WeakKeySet blacklistedKeys = new WeakKeySet();

	
	/** The lock. */
	private final Object lock;

	
	/**
	 * Instantiates a new inheriting state.
	 *
	 * @param parent the parent
	 */
	InheritingState(State parent) {
		this.parent = checkNotNull(parent, "parent");
		this.lock = (parent == State.NONE) ? this : parent.lock();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#parent()
	 */
	public State parent() {
		return parent;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getExplicitBinding(cn.com.rebirth.search.commons.inject.Key)
	 */
	@SuppressWarnings("unchecked")
	
	public <T> BindingImpl<T> getExplicitBinding(Key<T> key) {
		Binding<?> binding = explicitBindings.get(key);
		return binding != null ? (BindingImpl<T>) binding : parent.getExplicitBinding(key);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getExplicitBindingsThisLevel()
	 */
	public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
		return explicitBindings;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#putBinding(cn.com.rebirth.search.commons.inject.Key, cn.com.rebirth.search.commons.inject.internal.BindingImpl)
	 */
	public void putBinding(Key<?> key, BindingImpl<?> binding) {
		explicitBindingsMutable.put(key, binding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getScope(java.lang.Class)
	 */
	public Scope getScope(Class<? extends Annotation> annotationType) {
		Scope scope = scopes.get(annotationType);
		return scope != null ? scope : parent.getScope(annotationType);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#putAnnotation(java.lang.Class, cn.com.rebirth.search.commons.inject.Scope)
	 */
	public void putAnnotation(Class<? extends Annotation> annotationType, Scope scope) {
		scopes.put(annotationType, scope);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getConvertersThisLevel()
	 */
	public Iterable<MatcherAndConverter> getConvertersThisLevel() {
		return converters;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#addConverter(cn.com.rebirth.search.commons.inject.internal.MatcherAndConverter)
	 */
	public void addConverter(MatcherAndConverter matcherAndConverter) {
		converters.add(matcherAndConverter);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getConverter(java.lang.String, cn.com.rebirth.search.commons.inject.TypeLiteral, cn.com.rebirth.search.commons.inject.internal.Errors, java.lang.Object)
	 */
	public MatcherAndConverter getConverter(String stringValue, TypeLiteral<?> type, Errors errors, Object source) {
		MatcherAndConverter matchingConverter = null;
		for (State s = this; s != State.NONE; s = s.parent()) {
			for (MatcherAndConverter converter : s.getConvertersThisLevel()) {
				if (converter.getTypeMatcher().matches(type)) {
					if (matchingConverter != null) {
						errors.ambiguousTypeConversion(stringValue, source, type, matchingConverter, converter);
					}
					matchingConverter = converter;
				}
			}
		}
		return matchingConverter;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#addTypeListener(cn.com.rebirth.search.commons.inject.spi.TypeListenerBinding)
	 */
	public void addTypeListener(TypeListenerBinding listenerBinding) {
		listenerBindings.add(listenerBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#getTypeListenerBindings()
	 */
	public List<TypeListenerBinding> getTypeListenerBindings() {
		List<TypeListenerBinding> parentBindings = parent.getTypeListenerBindings();
		List<TypeListenerBinding> result = new ArrayList<TypeListenerBinding>(parentBindings.size() + 1);
		result.addAll(parentBindings);
		result.addAll(listenerBindings);
		return result;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#blacklist(cn.com.rebirth.search.commons.inject.Key)
	 */
	public void blacklist(Key<?> key) {
		parent.blacklist(key);
		blacklistedKeys.add(key);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#isBlacklisted(cn.com.rebirth.search.commons.inject.Key)
	 */
	public boolean isBlacklisted(Key<?> key) {
		return blacklistedKeys.contains(key);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#clearBlacklisted()
	 */
	@Override
	public void clearBlacklisted() {
		blacklistedKeys = new WeakKeySet();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.State#lock()
	 */
	public Object lock() {
		return lock;
	}
}
