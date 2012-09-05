/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons DefaultBindingTargetVisitor.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binding;


/**
 * The Class DefaultBindingTargetVisitor.
 *
 * @param <T> the generic type
 * @param <V> the value type
 * @author l.xue.nong
 */
public abstract class DefaultBindingTargetVisitor<T, V> implements BindingTargetVisitor<T, V> {

	
	/**
	 * Visit other.
	 *
	 * @param binding the binding
	 * @return the v
	 */
	protected V visitOther(Binding<? extends T> binding) {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.InstanceBinding)
	 */
	public V visit(InstanceBinding<? extends T> instanceBinding) {
		return visitOther(instanceBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ProviderInstanceBinding)
	 */
	public V visit(ProviderInstanceBinding<? extends T> providerInstanceBinding) {
		return visitOther(providerInstanceBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ProviderKeyBinding)
	 */
	public V visit(ProviderKeyBinding<? extends T> providerKeyBinding) {
		return visitOther(providerKeyBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.LinkedKeyBinding)
	 */
	public V visit(LinkedKeyBinding<? extends T> linkedKeyBinding) {
		return visitOther(linkedKeyBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ExposedBinding)
	 */
	public V visit(ExposedBinding<? extends T> exposedBinding) {
		return visitOther(exposedBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.UntargettedBinding)
	 */
	public V visit(UntargettedBinding<? extends T> untargettedBinding) {
		return visitOther(untargettedBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ConstructorBinding)
	 */
	public V visit(ConstructorBinding<? extends T> constructorBinding) {
		return visitOther(constructorBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ConvertedConstantBinding)
	 */
	public V visit(ConvertedConstantBinding<? extends T> convertedConstantBinding) {
		return visitOther(convertedConstantBinding);
	}

	
	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.BindingTargetVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ProviderBinding)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public V visit(ProviderBinding<? extends T> providerBinding) {
		return visitOther((Binding) providerBinding);
	}
}
