/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons State.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.MatcherAndConverter;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


/**
 * The Interface State.
 *
 * @author l.xue.nong
 */
interface State {

    
    /** The Constant NONE. */
    static final State NONE = new State() {
        public State parent() {
            throw new UnsupportedOperationException();
        }

        public <T> BindingImpl<T> getExplicitBinding(Key<T> key) {
            return null;
        }

        public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
            throw new UnsupportedOperationException();
        }

        public void putBinding(Key<?> key, BindingImpl<?> binding) {
            throw new UnsupportedOperationException();
        }

        public Scope getScope(Class<? extends Annotation> scopingAnnotation) {
            return null;
        }

        public void putAnnotation(Class<? extends Annotation> annotationType, Scope scope) {
            throw new UnsupportedOperationException();
        }

        public void addConverter(MatcherAndConverter matcherAndConverter) {
            throw new UnsupportedOperationException();
        }

        public MatcherAndConverter getConverter(String stringValue, TypeLiteral<?> type, Errors errors,
                                                Object source) {
            throw new UnsupportedOperationException();
        }

        public Iterable<MatcherAndConverter> getConvertersThisLevel() {
            return ImmutableSet.of();
        }

        public void addTypeListener(TypeListenerBinding typeListenerBinding) {
            throw new UnsupportedOperationException();
        }

        public List<TypeListenerBinding> getTypeListenerBindings() {
            return ImmutableList.of();
        }

        public void blacklist(Key<?> key) {
        }

        public boolean isBlacklisted(Key<?> key) {
            return true;
        }

        @Override
        public void clearBlacklisted() {
        }

        public Object lock() {
            throw new UnsupportedOperationException();
        }
    };

    
    /**
     * Parent.
     *
     * @return the state
     */
    State parent();

    
    /**
     * Gets the explicit binding.
     *
     * @param <T> the generic type
     * @param key the key
     * @return the explicit binding
     */
    <T> BindingImpl<T> getExplicitBinding(Key<T> key);

    
    /**
     * Gets the explicit bindings this level.
     *
     * @return the explicit bindings this level
     */
    Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel();

    
    /**
     * Put binding.
     *
     * @param key the key
     * @param binding the binding
     */
    void putBinding(Key<?> key, BindingImpl<?> binding);

    
    /**
     * Gets the scope.
     *
     * @param scopingAnnotation the scoping annotation
     * @return the scope
     */
    Scope getScope(Class<? extends Annotation> scopingAnnotation);

    
    /**
     * Put annotation.
     *
     * @param annotationType the annotation type
     * @param scope the scope
     */
    void putAnnotation(Class<? extends Annotation> annotationType, Scope scope);

    
    /**
     * Adds the converter.
     *
     * @param matcherAndConverter the matcher and converter
     */
    void addConverter(MatcherAndConverter matcherAndConverter);

    
    /**
     * Gets the converter.
     *
     * @param stringValue the string value
     * @param type the type
     * @param errors the errors
     * @param source the source
     * @return the converter
     */
    MatcherAndConverter getConverter(
            String stringValue, TypeLiteral<?> type, Errors errors, Object source);

    
    /**
     * Gets the converters this level.
     *
     * @return the converters this level
     */
    Iterable<MatcherAndConverter> getConvertersThisLevel();

    
    /**
     * Adds the type listener.
     *
     * @param typeListenerBinding the type listener binding
     */
    void addTypeListener(TypeListenerBinding typeListenerBinding);

    
    /**
     * Gets the type listener bindings.
     *
     * @return the type listener bindings
     */
    List<TypeListenerBinding> getTypeListenerBindings();

    
    /**
     * Blacklist.
     *
     * @param key the key
     */
    void blacklist(Key<?> key);

    
    /**
     * Checks if is blacklisted.
     *
     * @param key the key
     * @return true, if is blacklisted
     */
    boolean isBlacklisted(Key<?> key);

    
    /**
     * Lock.
     *
     * @return the object
     */
    Object lock();

    
    
    /**
     * Clear blacklisted.
     */
    void clearBlacklisted();
}
