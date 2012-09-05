/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeConverterBinding.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import static com.google.common.base.Preconditions.checkNotNull;
import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.matcher.Matcher;


/**
 * The Class TypeConverterBinding.
 *
 * @author l.xue.nong
 */
public final class TypeConverterBinding implements Element {

	
	/** The source. */
	private final Object source;

	
	/** The type matcher. */
	private final Matcher<? super TypeLiteral<?>> typeMatcher;

	
	/** The type converter. */
	private final TypeConverter typeConverter;

	
	/**
	 * Instantiates a new type converter binding.
	 *
	 * @param source the source
	 * @param typeMatcher the type matcher
	 * @param typeConverter the type converter
	 */
	TypeConverterBinding(Object source, Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter typeConverter) {
		this.source = checkNotNull(source, "source");
		this.typeMatcher = checkNotNull(typeMatcher, "typeMatcher");
		this.typeConverter = checkNotNull(typeConverter, "typeConverter");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public Object getSource() {
		return source;
	}

	
	/**
	 * Gets the type matcher.
	 *
	 * @return the type matcher
	 */
	public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
		return typeMatcher;
	}

	
	/**
	 * Gets the type converter.
	 *
	 * @return the type converter
	 */
	public TypeConverter getTypeConverter() {
		return typeConverter;
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
		binder.withSource(getSource()).convertToTypes(typeMatcher, typeConverter);
	}
}
