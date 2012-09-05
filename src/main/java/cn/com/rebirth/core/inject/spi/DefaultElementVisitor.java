/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons DefaultElementVisitor.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binding;


/**
 * The Class DefaultElementVisitor.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public abstract class DefaultElementVisitor<V> implements ElementVisitor<V> {

	
	/**
	 * Visit other.
	 *
	 * @param element the element
	 * @return the v
	 */
	protected V visitOther(Element element) {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.Message)
	 */
	public V visit(Message message) {
		return visitOther(message);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.Binding)
	 */
	public <T> V visit(Binding<T> binding) {
		return visitOther(binding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ScopeBinding)
	 */
	public V visit(ScopeBinding scopeBinding) {
		return visitOther(scopeBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.TypeConverterBinding)
	 */
	public V visit(TypeConverterBinding typeConverterBinding) {
		return visitOther(typeConverterBinding);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.ProviderLookup)
	 */
	public <T> V visit(ProviderLookup<T> providerLookup) {
		return visitOther(providerLookup);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.InjectionRequest)
	 */
	@SuppressWarnings("rawtypes")
	public V visit(InjectionRequest injectionRequest) {
		return visitOther(injectionRequest);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.StaticInjectionRequest)
	 */
	public V visit(StaticInjectionRequest staticInjectionRequest) {
		return visitOther(staticInjectionRequest);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.PrivateElements)
	 */
	public V visit(PrivateElements privateElements) {
		return visitOther(privateElements);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.MembersInjectorLookup)
	 */
	public <T> V visit(MembersInjectorLookup<T> lookup) {
		return visitOther(lookup);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.ElementVisitor#visit(cn.com.rebirth.search.commons.inject.spi.TypeListenerBinding)
	 */
	public V visit(TypeListenerBinding binding) {
		return visitOther(binding);
	}
}
