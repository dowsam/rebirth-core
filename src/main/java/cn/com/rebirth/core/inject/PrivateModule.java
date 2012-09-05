/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PrivateModule.java 2012-7-6 10:23:43 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import static com.google.common.base.Preconditions.checkState;

import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.binder.AnnotatedBindingBuilder;
import cn.com.rebirth.core.inject.binder.AnnotatedConstantBindingBuilder;
import cn.com.rebirth.core.inject.binder.AnnotatedElementBuilder;
import cn.com.rebirth.core.inject.binder.LinkedBindingBuilder;
import cn.com.rebirth.core.inject.matcher.Matcher;
import cn.com.rebirth.core.inject.spi.Message;
import cn.com.rebirth.core.inject.spi.TypeConverter;
import cn.com.rebirth.core.inject.spi.TypeListener;


/**
 * The Class PrivateModule.
 *
 * @author l.xue.nong
 */
public abstract class PrivateModule implements Module {

    
    /** The binder. */
    private PrivateBinder binder;

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.inject.Module#configure(cn.com.rebirth.search.commons.inject.Binder)
     */
    public final synchronized void configure(Binder binder) {
        checkState(this.binder == null, "Re-entry is not allowed.");

        
        this.binder = (PrivateBinder) binder.skipSources(PrivateModule.class);
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    
    /**
     * Configure.
     */
    protected abstract void configure();

    
    /**
     * Expose.
     *
     * @param <T> the generic type
     * @param key the key
     */
    protected final <T> void expose(Key<T> key) {
        binder.expose(key);
    }

    
    /**
     * Expose.
     *
     * @param type the type
     * @return the annotated element builder
     */
    protected final AnnotatedElementBuilder expose(Class<?> type) {
        return binder.expose(type);
    }

    
    /**
     * Expose.
     *
     * @param type the type
     * @return the annotated element builder
     */
    protected final AnnotatedElementBuilder expose(TypeLiteral<?> type) {
        return binder.expose(type);
    }

    

    
    /**
     * Binder.
     *
     * @return the private binder
     */
    protected final PrivateBinder binder() {
        return binder;
    }

    
    /**
     * Bind scope.
     *
     * @param scopeAnnotation the scope annotation
     * @param scope the scope
     */
    protected final void bindScope(Class<? extends Annotation> scopeAnnotation, Scope scope) {
        binder.bindScope(scopeAnnotation, scope);
    }

    
    /**
     * Bind.
     *
     * @param <T> the generic type
     * @param key the key
     * @return the linked binding builder
     */
    protected final <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return binder.bind(key);
    }

    
    /**
     * Bind.
     *
     * @param <T> the generic type
     * @param typeLiteral the type literal
     * @return the annotated binding builder
     */
    protected final <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return binder.bind(typeLiteral);
    }

    
    /**
     * Bind.
     *
     * @param <T> the generic type
     * @param clazz the clazz
     * @return the annotated binding builder
     */
    protected final <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
        return binder.bind(clazz);
    }

    
    /**
     * Bind constant.
     *
     * @return the annotated constant binding builder
     */
    protected final AnnotatedConstantBindingBuilder bindConstant() {
        return binder.bindConstant();
    }

    
    /**
     * Install.
     *
     * @param module the module
     */
    protected final void install(Module module) {
        binder.install(module);
    }

    
    /**
     * Adds the error.
     *
     * @param message the message
     * @param arguments the arguments
     */
    protected final void addError(String message, Object... arguments) {
        binder.addError(message, arguments);
    }

    
    /**
     * Adds the error.
     *
     * @param t the t
     */
    protected final void addError(Throwable t) {
        binder.addError(t);
    }

    
    /**
     * Adds the error.
     *
     * @param message the message
     */
    protected final void addError(Message message) {
        binder.addError(message);
    }

    
    /**
     * Request injection.
     *
     * @param instance the instance
     */
    protected final void requestInjection(Object instance) {
        binder.requestInjection(instance);
    }

    
    /**
     * Request static injection.
     *
     * @param types the types
     */
    protected final void requestStaticInjection(Class<?>... types) {
        binder.requestStaticInjection(types);
    }

    
    /**
     * Require binding.
     *
     * @param key the key
     */
    protected final void requireBinding(Key<?> key) {
        binder.getProvider(key);
    }

    
    /**
     * Require binding.
     *
     * @param type the type
     */
    protected final void requireBinding(Class<?> type) {
        binder.getProvider(type);
    }

    
    /**
     * Gets the provider.
     *
     * @param <T> the generic type
     * @param key the key
     * @return the provider
     */
    protected final <T> Provider<T> getProvider(Key<T> key) {
        return binder.getProvider(key);
    }

    
    /**
     * Gets the provider.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the provider
     */
    protected final <T> Provider<T> getProvider(Class<T> type) {
        return binder.getProvider(type);
    }

    
    /**
     * Convert to types.
     *
     * @param typeMatcher the type matcher
     * @param converter the converter
     */
    protected final void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher,
                                        TypeConverter converter) {
        binder.convertToTypes(typeMatcher, converter);
    }

    
    /**
     * Current stage.
     *
     * @return the stage
     */
    protected final Stage currentStage() {
        return binder.currentStage();
    }

    
    /**
     * Gets the members injector.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the members injector
     */
    protected <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return binder.getMembersInjector(type);
    }

    
    /**
     * Gets the members injector.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the members injector
     */
    protected <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type) {
        return binder.getMembersInjector(type);
    }

    
    /**
     * Bind listener.
     *
     * @param typeMatcher the type matcher
     * @param listener the listener
     */
    protected void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher,
                                TypeListener listener) {
        binder.bindListener(typeMatcher, listener);
    }
}
