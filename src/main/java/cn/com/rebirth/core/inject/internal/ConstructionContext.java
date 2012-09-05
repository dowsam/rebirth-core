/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructionContext.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class ConstructionContext.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class ConstructionContext<T> {

    
    /** The current reference. */
    T currentReference;
    
    
    /** The constructing. */
    boolean constructing;

    
    /** The invocation handlers. */
    List<DelegatingInvocationHandler<T>> invocationHandlers;

    
    /**
     * Gets the current reference.
     *
     * @return the current reference
     */
    public T getCurrentReference() {
        return currentReference;
    }

    
    /**
     * Removes the current reference.
     */
    public void removeCurrentReference() {
        this.currentReference = null;
    }

    
    /**
     * Sets the current reference.
     *
     * @param currentReference the new current reference
     */
    public void setCurrentReference(T currentReference) {
        this.currentReference = currentReference;
    }

    
    /**
     * Checks if is constructing.
     *
     * @return true, if is constructing
     */
    public boolean isConstructing() {
        return constructing;
    }

    
    /**
     * Start construction.
     */
    public void startConstruction() {
        this.constructing = true;
    }

    
    /**
     * Finish construction.
     */
    public void finishConstruction() {
        this.constructing = false;
        invocationHandlers = null;
    }

    
    /**
     * Creates the proxy.
     *
     * @param errors the errors
     * @param expectedType the expected type
     * @return the object
     * @throws ErrorsException the errors exception
     */
    public Object createProxy(Errors errors, Class<?> expectedType) throws ErrorsException {
        
        
        

        if (!expectedType.isInterface()) {
            throw errors.cannotSatisfyCircularDependency(expectedType).toException();
        }

        if (invocationHandlers == null) {
            invocationHandlers = new ArrayList<DelegatingInvocationHandler<T>>();
        }

        DelegatingInvocationHandler<T> invocationHandler
                = new DelegatingInvocationHandler<T>();
        invocationHandlers.add(invocationHandler);

        
        
        ClassLoader classLoader = expectedType.getClassLoader() == null ? ClassLoader.getSystemClassLoader() : expectedType.getClassLoader();
        return expectedType.cast(Proxy.newProxyInstance(classLoader,
                new Class[]{expectedType}, invocationHandler));
    }

    
    /**
     * Sets the proxy delegates.
     *
     * @param delegate the new proxy delegates
     */
    public void setProxyDelegates(T delegate) {
        if (invocationHandlers != null) {
            for (DelegatingInvocationHandler<T> handler : invocationHandlers) {
                handler.setDelegate(delegate);
            }
        }
    }

    
    /**
     * The Class DelegatingInvocationHandler.
     *
     * @param <T> the generic type
     * @author l.xue.nong
     */
    static class DelegatingInvocationHandler<T> implements InvocationHandler {

        
        /** The delegate. */
        T delegate;

        
        /* (non-Javadoc)
         * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
         */
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if (delegate == null) {
                throw new IllegalStateException("This is a proxy used to support"
                        + " circular references involving constructors. The object we're"
                        + " proxying is not constructed yet. Please wait until after"
                        + " injection has completed to use this object.");
            }

            try {
                
                return method.invoke(delegate, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        
        /**
         * Sets the delegate.
         *
         * @param delegate the new delegate
         */
        void setDelegate(T delegate) {
            this.delegate = delegate;
        }
    }
}
