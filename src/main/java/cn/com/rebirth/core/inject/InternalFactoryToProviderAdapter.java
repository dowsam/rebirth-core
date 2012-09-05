/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InternalFactoryToProviderAdapter.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import static com.google.common.base.Preconditions.checkNotNull;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.SourceProvider;
import cn.com.rebirth.core.inject.spi.Dependency;


/**
 * The Class InternalFactoryToProviderAdapter.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class InternalFactoryToProviderAdapter<T> implements InternalFactory<T> {

    
    /** The initializable. */
    private final Initializable<Provider<? extends T>> initializable;
    
    
    /** The source. */
    private final Object source;

    
    /**
     * Instantiates a new internal factory to provider adapter.
     *
     * @param initializable the initializable
     */
    public InternalFactoryToProviderAdapter(Initializable<Provider<? extends T>> initializable) {
        this(initializable, SourceProvider.UNKNOWN_SOURCE);
    }

    
    /**
     * Instantiates a new internal factory to provider adapter.
     *
     * @param initializable the initializable
     * @param source the source
     */
    public InternalFactoryToProviderAdapter(
            Initializable<Provider<? extends T>> initializable, Object source) {
        this.initializable = checkNotNull(initializable, "provider");
        this.source = checkNotNull(source, "source");
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
     */
    public T get(Errors errors, InternalContext context, Dependency<?> dependency)
            throws ErrorsException {
        try {
            return errors.checkForNull(initializable.get(errors).get(), source, dependency);
        } catch (RuntimeException userException) {
            throw errors.withSource(source).errorInProvider(userException).toException();
        }
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return initializable.toString();
    }
}
