/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderInstanceBindingImpl.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.util.Set;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.HasDependencies;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.ProviderInstanceBinding;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ProviderInstanceBindingImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class ProviderInstanceBindingImpl<T> extends BindingImpl<T> implements ProviderInstanceBinding<T> {

	
	/** The provider instance. */
	final Provider<? extends T> providerInstance;

	
	/** The injection points. */
	final ImmutableSet<InjectionPoint> injectionPoints;

	
	/**
	 * Instantiates a new provider instance binding impl.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param internalFactory the internal factory
	 * @param scoping the scoping
	 * @param providerInstance the provider instance
	 * @param injectionPoints the injection points
	 */
	public ProviderInstanceBindingImpl(Injector injector, Key<T> key, Object source,
			InternalFactory<? extends T> internalFactory, Scoping scoping, Provider<? extends T> providerInstance,
			Set<InjectionPoint> injectionPoints) {
		super(injector, key, source, internalFactory, scoping);
		this.providerInstance = providerInstance;
		this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
	}

	
	/**
	 * Instantiates a new provider instance binding impl.
	 *
	 * @param source the source
	 * @param key the key
	 * @param scoping the scoping
	 * @param injectionPoints the injection points
	 * @param providerInstance the provider instance
	 */
	public ProviderInstanceBindingImpl(Object source, Key<T> key, Scoping scoping, Set<InjectionPoint> injectionPoints,
			Provider<? extends T> providerInstance) {
		super(source, key, scoping);
		this.injectionPoints = ImmutableSet.copyOf(injectionPoints);
		this.providerInstance = providerInstance;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
	 */
	public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ProviderInstanceBinding#getProviderInstance()
	 */
	public Provider<? extends T> getProviderInstance() {
		return providerInstance;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ProviderInstanceBinding#getInjectionPoints()
	 */
	public Set<InjectionPoint> getInjectionPoints() {
		return injectionPoints;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.HasDependencies#getDependencies()
	 */
	public Set<Dependency<?>> getDependencies() {
		return providerInstance instanceof HasDependencies ? ImmutableSet.copyOf(((HasDependencies) providerInstance)
				.getDependencies()) : Dependency.forInjectionPoints(injectionPoints);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withScoping(cn.com.rebirth.search.commons.inject.internal.Scoping)
	 */
	public BindingImpl<T> withScoping(Scoping scoping) {
		return new ProviderInstanceBindingImpl<T>(getSource(), getKey(), scoping, injectionPoints, providerInstance);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#withKey(cn.com.rebirth.search.commons.inject.Key)
	 */
	public BindingImpl<T> withKey(Key<T> key) {
		return new ProviderInstanceBindingImpl<T>(getSource(), key, getScoping(), injectionPoints, providerInstance);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		getScoping().applyTo(binder.withSource(getSource()).bind(getKey()).toProvider(getProviderInstance()));
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(ProviderInstanceBinding.class).add("key", getKey()).add("source", getSource())
				.add("scope", getScoping()).add("provider", providerInstance).toString();
	}
}
