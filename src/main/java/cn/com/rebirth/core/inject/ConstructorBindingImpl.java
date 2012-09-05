/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstructorBindingImpl.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import static com.google.common.base.Preconditions.checkState;

import java.util.Set;

import cn.com.rebirth.core.inject.internal.BindingImpl;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.internal.InternalFactory;
import cn.com.rebirth.core.inject.internal.Scoping;
import cn.com.rebirth.core.inject.internal.ToStringBuilder;
import cn.com.rebirth.core.inject.spi.BindingTargetVisitor;
import cn.com.rebirth.core.inject.spi.ConstructorBinding;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.InjectionPoint;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ConstructorBindingImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class ConstructorBindingImpl<T> extends BindingImpl<T> implements ConstructorBinding<T> {

	
	/** The factory. */
	private final Factory<T> factory;

	
	/**
	 * Instantiates a new constructor binding impl.
	 *
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param scopedFactory the scoped factory
	 * @param scoping the scoping
	 * @param factory the factory
	 */
	private ConstructorBindingImpl(Injector injector, Key<T> key, Object source,
			InternalFactory<? extends T> scopedFactory, Scoping scoping, Factory<T> factory) {
		super(injector, key, source, scopedFactory, scoping);
		this.factory = factory;
	}

	
	/**
	 * Creates the.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param key the key
	 * @param source the source
	 * @param scoping the scoping
	 * @return the constructor binding impl
	 */
	static <T> ConstructorBindingImpl<T> create(InjectorImpl injector, Key<T> key, Object source, Scoping scoping) {
		Factory<T> factoryFactory = new Factory<T>();
		InternalFactory<? extends T> scopedFactory = Scopes.scope(key, injector, factoryFactory, scoping);
		return new ConstructorBindingImpl<T>(injector, key, source, scopedFactory, scoping, factoryFactory);
	}

	
	/**
	 * Initialize.
	 *
	 * @param injector the injector
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
		factory.constructorInjector = injector.constructors.get(getKey().getTypeLiteral(), errors);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.Binding#acceptTargetVisitor(cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor)
	 */
	public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
		checkState(factory.constructorInjector != null, "not initialized");
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ConstructorBinding#getConstructor()
	 */
	public InjectionPoint getConstructor() {
		checkState(factory.constructorInjector != null, "Binding is not ready");
		return factory.constructorInjector.getConstructionProxy().getInjectionPoint();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ConstructorBinding#getInjectableMembers()
	 */
	public Set<InjectionPoint> getInjectableMembers() {
		checkState(factory.constructorInjector != null, "Binding is not ready");
		return factory.constructorInjector.getInjectableMembers();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.HasDependencies#getDependencies()
	 */
	public Set<Dependency<?>> getDependencies() {
		return Dependency.forInjectionPoints(new ImmutableSet.Builder<InjectionPoint>().add(getConstructor())
				.addAll(getInjectableMembers()).build());
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		throw new UnsupportedOperationException("This element represents a synthetic binding.");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.internal.BindingImpl#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(ConstructorBinding.class).add("key", getKey()).add("source", getSource())
				.add("scope", getScoping()).toString();
	}

	
	/**
	 * The Class Factory.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static class Factory<T> implements InternalFactory<T> {

		
		/** The constructor injector. */
		private ConstructorInjector<T> constructorInjector;

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.InternalFactory#get(cn.com.rebirth.search.commons.inject.internal.Errors, cn.com.rebirth.search.commons.inject.internal.InternalContext, cn.com.rebirth.search.commons.inject.spi.Dependency)
		 */
		@SuppressWarnings("unchecked")
		public T get(Errors errors, InternalContext context, Dependency<?> dependency) throws ErrorsException {
			checkState(constructorInjector != null, "Constructor not ready");

			
			
			return (T) constructorInjector.construct(errors, context, dependency.getKey().getRawType());
		}
	}
}
