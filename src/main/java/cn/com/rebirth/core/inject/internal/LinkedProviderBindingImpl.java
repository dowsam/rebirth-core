/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LinkedProviderBindingImpl.java 2012-7-6 10:23:50 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.ProviderKeyBinding;


/**
 * The Class LinkedProviderBindingImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class LinkedProviderBindingImpl<T> extends BindingImpl<T> implements ProviderKeyBinding<T> {

	
	/** The provider key. */
	final Key<? extends Provider<? extends T>> providerKey;

	
	/**
	 * Instantiates a new linked provider binding impl.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param internalFactory the internal factory
	 * @param scoping the scoping
	 * @param providerKey the provider key
	 */
	public LinkedProviderBindingImpl(Injector injector, Key<T> key, Object source,
			InternalFactory<? extends T> internalFactory, Scoping scoping,
			Key<? extends Provider<? extends T>> providerKey) {
		super(injector, key, source, internalFactory, scoping);
		this.providerKey = providerKey;
	}

	
	/**
	 * Instantiates a new linked provider binding impl.
	 *
	 * @param source the source
	 * @param key the key
	 * @param scoping the scoping
	 * @param providerKey the provider key
	 */
	LinkedProviderBindingImpl(Object source, Key<T> key, Scoping scoping,
			Key<? extends Provider<? extends T>> providerKey) {
		super(source, key, scoping);
		this.providerKey = providerKey;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
	 */
	public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ProviderKeyBinding#getProviderKey()
	 */
	public Key<? extends Provider<? extends T>> getProviderKey() {
		return providerKey;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withScoping(cn.com.rebirth.search.commons.inject.internal.Scoping)
	 */
	public BindingImpl<T> withScoping(Scoping scoping) {
		return new LinkedProviderBindingImpl<T>(getSource(), getKey(), scoping, providerKey);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withKey(cn.com.rebirth.search.commons.inject.Key)
	 */
	public BindingImpl<T> withKey(Key<T> key) {
		return new LinkedProviderBindingImpl<T>(getSource(), key, getScoping(), providerKey);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		getScoping().applyTo(binder.withSource(getSource()).bind(getKey()).toProvider(getProviderKey()));
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(ProviderKeyBinding.class).add("key", getKey()).add("source", getSource())
				.add("scope", getScoping()).add("provider", providerKey).toString();
	}
}
