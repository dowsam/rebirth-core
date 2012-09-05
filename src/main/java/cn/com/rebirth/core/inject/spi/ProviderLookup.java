/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderLookup.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;


/**
 * The Class ProviderLookup.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class ProviderLookup<T> implements Element {

	
	/** The source. */
	private final Object source;

	
	/** The key. */
	private final Key<T> key;

	
	/** The delegate. */
	private Provider<T> delegate;

	
	/**
	 * Instantiates a new provider lookup.
	 *
	 * @param source the source
	 * @param key the key
	 */
	public ProviderLookup(Object source, Key<T> key) {
		this.source = checkNotNull(source, "source");
		this.key = checkNotNull(key, "key");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public Object getSource() {
		return source;
	}

	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Key<T> getKey() {
		return key;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#acceptVisitor(cn.com.rebirth.search.commons.inject.spi.ElementVisitor)
	 */
	@SuppressWarnings("hiding")
	public <T> T acceptVisitor(ElementVisitor<T> visitor) {
		return visitor.visit(this);
	}

	
	/**
	 * Initialize delegate.
	 *
	 * @param delegate the delegate
	 */
	public void initializeDelegate(Provider<T> delegate) {
		checkState(this.delegate == null, "delegate already initialized");
		this.delegate = checkNotNull(delegate, "delegate");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		initializeDelegate(binder.withSource(getSource()).getProvider(key));
	}

	
	/**
	 * Gets the delegate.
	 *
	 * @return the delegate
	 */
	public Provider<T> getDelegate() {
		return delegate;
	}

	
	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public Provider<T> getProvider() {
		return new Provider<T>() {
			public T get() {
				checkState(delegate != null, "This Provider cannot be used until the Injector has been created.");
				return delegate.get();
			}

			@Override
			public String toString() {
				return "Provider<" + key.getTypeLiteral() + ">";
			}
		};
	}
}
