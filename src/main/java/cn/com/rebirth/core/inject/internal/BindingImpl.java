/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BindingImpl.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.spi.BindingScopingVisitor;
import cn.com.rebirth.core.inject.spi.ElementVisitor;
import cn.com.rebirth.core.inject.spi.InstanceBinding;


/**
 * The Class BindingImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public abstract class BindingImpl<T> implements Binding<T> {

	
	/** The injector. */
	private final Injector injector;

	
	/** The key. */
	private final Key<T> key;

	
	/** The source. */
	private final Object source;

	
	/** The scoping. */
	private final Scoping scoping;

	
	/** The internal factory. */
	private final InternalFactory<? extends T> internalFactory;

	
	/**
	 * Instantiates a new binding impl.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param internalFactory the internal factory
	 * @param scoping the scoping
	 */
	public BindingImpl(Injector injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory,
			Scoping scoping) {
		this.injector = injector;
		this.key = key;
		this.source = source;
		this.internalFactory = internalFactory;
		this.scoping = scoping;
	}

	
	/**
	 * Instantiates a new binding impl.
	 *
	 * @param source the source
	 * @param key the key
	 * @param scoping the scoping
	 */
	protected BindingImpl(Object source, Key<T> key, Scoping scoping) {
		this.internalFactory = null;
		this.injector = null;
		this.source = source;
		this.key = key;
		this.scoping = scoping;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#getKey()
	 */
	public Key<T> getKey() {
		return key;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public Object getSource() {
		return source;
	}

	
	/** The provider. */
	private volatile Provider<T> provider;

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#getProvider()
	 */
	public Provider<T> getProvider() {
		if (provider == null) {
			if (injector == null) {
				throw new UnsupportedOperationException("getProvider() not supported for module bindings");
			}

			provider = injector.getProvider(key);
		}
		return provider;
	}

	
	/**
	 * Gets the internal factory.
	 *
	 * @return the internal factory
	 */
	public InternalFactory<? extends T> getInternalFactory() {
		return internalFactory;
	}

	
	/**
	 * Gets the scoping.
	 *
	 * @return the scoping
	 */
	public Scoping getScoping() {
		return scoping;
	}

	
	/**
	 * Checks if is constant.
	 *
	 * @return true, if is constant
	 */
	public boolean isConstant() {
		return this instanceof InstanceBinding;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#acceptVisitor(cn.com.rebirth.search.commons.inject.spi.ElementVisitor)
	 */
	public <V> V acceptVisitor(ElementVisitor<V> visitor) {
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#acceptScopingVisitor(cn.com.rebirth.search.commons.inject.spi.BindingScopingVisitor)
	 */
	public <V> V acceptScopingVisitor(BindingScopingVisitor<V> visitor) {
		return scoping.acceptVisitor(visitor);
	}

	
	/**
	 * With scoping.
	 *
	 * @param scoping the scoping
	 * @return the binding impl
	 */
	protected BindingImpl<T> withScoping(Scoping scoping) {
		throw new AssertionError();
	}

	
	/**
	 * With key.
	 *
	 * @param key the key
	 * @return the binding impl
	 */
	protected BindingImpl<T> withKey(Key<T> key) {
		throw new AssertionError();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(Binding.class).add("key", key).add("scope", scoping).add("source", source)
				.toString();
	}

	
	/**
	 * Gets the injector.
	 *
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}
}