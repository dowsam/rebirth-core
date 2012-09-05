/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LinkedBindingImpl.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.LinkedKeyBinding;


/**
 * The Class LinkedBindingImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class LinkedBindingImpl<T> extends BindingImpl<T> implements LinkedKeyBinding<T> {

	
	/** The target key. */
	final Key<? extends T> targetKey;

	
	/**
	 * Instantiates a new linked binding impl.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param internalFactory the internal factory
	 * @param scoping the scoping
	 * @param targetKey the target key
	 */
	public LinkedBindingImpl(Injector injector, Key<T> key, Object source,
			InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends T> targetKey) {
		super(injector, key, source, internalFactory, scoping);
		this.targetKey = targetKey;
	}

	
	/**
	 * Instantiates a new linked binding impl.
	 *
	 * @param source the source
	 * @param key the key
	 * @param scoping the scoping
	 * @param targetKey the target key
	 */
	public LinkedBindingImpl(Object source, Key<T> key, Scoping scoping, Key<? extends T> targetKey) {
		super(source, key, scoping);
		this.targetKey = targetKey;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
	 */
	public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.LinkedKeyBinding#getLinkedKey()
	 */
	public Key<? extends T> getLinkedKey() {
		return targetKey;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withScoping(cn.com.rebirth.search.commons.inject.internal.Scoping)
	 */
	public BindingImpl<T> withScoping(Scoping scoping) {
		return new LinkedBindingImpl<T>(getSource(), getKey(), scoping, targetKey);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withKey(cn.com.rebirth.search.commons.inject.Key)
	 */
	public BindingImpl<T> withKey(Key<T> key) {
		return new LinkedBindingImpl<T>(getSource(), key, getScoping(), targetKey);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		getScoping().applyTo(binder.withSource(getSource()).bind(getKey()).to(getLinkedKey()));
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(LinkedKeyBinding.class).add("key", getKey()).add("source", getSource())
				.add("scope", getScoping()).add("target", targetKey).toString();
	}
}
