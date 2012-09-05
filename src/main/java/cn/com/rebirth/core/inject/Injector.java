/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Injector.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.util.List;
import java.util.Map;


/**
 * The Interface Injector.
 *
 * @author l.xue.nong
 */
public interface Injector {

    
    /**
     * Inject members.
     *
     * @param instance the instance
     */
    void injectMembers(Object instance);

    
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
     * Gets the bindings.
     *
     * @return the bindings
     */
    Map<Key<?>, Binding<?>> getBindings();

    
    /**
     * Gets the binding.
     *
     * @param <T> the generic type
     * @param key the key
     * @return the binding
     */
    <T> Binding<T> getBinding(Key<T> key);

    
    /**
     * Gets the binding.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the binding
     */
    <T> Binding<T> getBinding(Class<T> type);

    
    /**
     * Find bindings by type.
     *
     * @param <T> the generic type
     * @param type the type
     * @return the list
     */
    <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type);

    
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
     * Gets the single instance of Injector.
     *
     * @param <T> the generic type
     * @param key the key
     * @return single instance of Injector
     */
    <T> T getInstance(Key<T> key);

    
    /**
     * Gets the single instance of Injector.
     *
     * @param <T> the generic type
     * @param type the type
     * @return single instance of Injector
     */
    <T> T getInstance(Class<T> type);

    
    /**
     * Gets the parent.
     *
     * @return the parent
     */
    Injector getParent();

    
    /**
     * Creates the child injector.
     *
     * @param modules the modules
     * @return the injector
     */
    Injector createChildInjector(Iterable<? extends Module> modules);

    
    /**
     * Creates the child injector.
     *
     * @param modules the modules
     * @return the injector
     */
    Injector createChildInjector(Module... modules);
}
