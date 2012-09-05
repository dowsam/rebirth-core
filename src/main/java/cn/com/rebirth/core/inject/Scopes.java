/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Scopes.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.lang.annotation.Annotation;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.Scoping;


/**
 * The Class Scopes.
 *
 * @author l.xue.nong
 */
public class Scopes {

    
    /**
     * Instantiates a new scopes.
     */
    private Scopes() {
    }

    
    /** The Constant SINGLETON. */
    public static final Scope SINGLETON = new Scope() {
        public <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
            return new Provider<T>() {

                private volatile T instance;

                
                public T get() {
                    if (instance == null) {
                        
                        synchronized (InjectorImpl.class) {
                            if (instance == null) {
                                instance = creator.get();
                            }
                        }
                    }
                    return instance;
                }

                public String toString() {
                    return String.format("%s[%s]", creator, SINGLETON);
                }
            };
        }

        @Override
        public String toString() {
            return "Scopes.SINGLETON";
        }
    };

    
    /** The Constant NO_SCOPE. */
    public static final Scope NO_SCOPE = new Scope() {
        public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
            return unscoped;
        }

        @Override
        public String toString() {
            return "Scopes.NO_SCOPE";
        }
    };

    
    /**
     * Scope.
     *
     * @param <T> the generic type
     * @param key the key
     * @param injector the injector
     * @param creator the creator
     * @param scoping the scoping
     * @return the internal factory<? extends t>
     */
    static <T> InternalFactory<? extends T> scope(Key<T> key, InjectorImpl injector,
                                                  InternalFactory<? extends T> creator, Scoping scoping) {

        if (scoping.isNoScope()) {
            return creator;
        }

        Scope scope = scoping.getScopeInstance();

        Provider<T> scoped
                = scope.scope(key, new ProviderToInternalFactoryAdapter<T>(injector, creator));
        return new InternalFactoryToProviderAdapter<T>(
                Initializables.<Provider<? extends T>>of(scoped));
    }

    
    /**
     * Make injectable.
     *
     * @param scoping the scoping
     * @param injector the injector
     * @param errors the errors
     * @return the scoping
     */
    static Scoping makeInjectable(Scoping scoping, InjectorImpl injector, Errors errors) {
        Class<? extends Annotation> scopeAnnotation = scoping.getScopeAnnotation();
        if (scopeAnnotation == null) {
            return scoping;
        }

        Scope scope = injector.state.getScope(scopeAnnotation);
        if (scope != null) {
            return Scoping.forInstance(scope);
        }

        errors.scopeNotFound(scopeAnnotation);
        return Scoping.UNSCOPED;
    }
}
