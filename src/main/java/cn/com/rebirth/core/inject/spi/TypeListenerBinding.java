/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeListenerBinding.java 2012-7-6 10:23:54 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.matcher.Matcher;


/**
 * The Class TypeListenerBinding.
 *
 * @author l.xue.nong
 */
public final class TypeListenerBinding implements Element {

	
	/** The source. */
	private final Object source;

	
	/** The type matcher. */
	private final Matcher<? super TypeLiteral<?>> typeMatcher;

	
	/** The listener. */
	private final TypeListener listener;

	
	/**
	 * Instantiates a new type listener binding.
	 *
	 * @param source the source
	 * @param listener the listener
	 * @param typeMatcher the type matcher
	 */
	TypeListenerBinding(Object source, TypeListener listener, Matcher<? super TypeLiteral<?>> typeMatcher) {
		this.source = source;
		this.listener = listener;
		this.typeMatcher = typeMatcher;
	}

	
	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public TypeListener getListener() {
		return listener;
	}

	
	/**
	 * Gets the type matcher.
	 *
	 * @return the type matcher
	 */
	public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
		return typeMatcher;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public Object getSource() {
		return source;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#acceptVisitor(cn.com.rebirth.search.commons.inject.spi.ElementVisitor)
	 */
	public <T> T acceptVisitor(ElementVisitor<T> visitor) {
		return visitor.visit(this);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		binder.withSource(getSource()).bindListener(typeMatcher, listener);
	}
}
