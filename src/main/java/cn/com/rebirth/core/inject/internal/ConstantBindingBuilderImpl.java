/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConstantBindingBuilderImpl.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.annotation.Annotation;
import java.util.List;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.binder.AnnotatedConstantBindingBuilder;
import cn.com.rebirth.core.inject.binder.ConstantBindingBuilder;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.InjectionPoint;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ConstantBindingBuilderImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public final class ConstantBindingBuilderImpl<T> extends AbstractBindingBuilder<T> implements
		AnnotatedConstantBindingBuilder, ConstantBindingBuilder {

	
	/**
	 * Instantiates a new constant binding builder impl.
	 *
	 * @param binder the binder
	 * @param elements the elements
	 * @param source the source
	 */
	@SuppressWarnings("unchecked")
	
	public ConstantBindingBuilderImpl(Binder binder, List<Element> elements, Object source) {
		super(binder, elements, source, (Key<T>) NULL_KEY);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.AnnotatedConstantBindingBuilder#annotatedWith(java.lang.Class)
	 */
	public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType) {
		annotatedWithInternal(annotationType);
		return this;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.AnnotatedConstantBindingBuilder#annotatedWith(java.lang.annotation.Annotation)
	 */
	public ConstantBindingBuilder annotatedWith(Annotation annotation) {
		annotatedWithInternal(annotation);
		return this;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(java.lang.String)
	 */
	public void to(final String value) {
		toConstant(String.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(int)
	 */
	public void to(final int value) {
		toConstant(Integer.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(long)
	 */
	public void to(final long value) {
		toConstant(Long.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(boolean)
	 */
	public void to(final boolean value) {
		toConstant(Boolean.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(double)
	 */
	public void to(final double value) {
		toConstant(Double.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(float)
	 */
	public void to(final float value) {
		toConstant(Float.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(short)
	 */
	public void to(final short value) {
		toConstant(Short.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(char)
	 */
	public void to(final char value) {
		toConstant(Character.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(java.lang.Class)
	 */
	public void to(final Class<?> value) {
		toConstant(Class.class, value);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.binder.ConstantBindingBuilder#to(java.lang.Enum)
	 */
	public <E extends Enum<E>> void to(final E value) {
		toConstant(value.getDeclaringClass(), value);
	}

	
	/**
	 * To constant.
	 *
	 * @param type the type
	 * @param instance the instance
	 */
	private void toConstant(Class<?> type, Object instance) {
		
		@SuppressWarnings("unchecked")
		Class<T> typeAsClassT = (Class<T>) type;
		@SuppressWarnings("unchecked")
		T instanceAsT = (T) instance;

		if (keyTypeIsSet()) {
			binder.addError(CONSTANT_VALUE_ALREADY_SET);
			return;
		}

		BindingImpl<T> base = getBinding();
		Key<T> key;
		if (base.getKey().getAnnotation() != null) {
			key = Key.get(typeAsClassT, base.getKey().getAnnotation());
		} else if (base.getKey().getAnnotationType() != null) {
			key = Key.get(typeAsClassT, base.getKey().getAnnotationType());
		} else {
			key = Key.get(typeAsClassT);
		}

		if (instanceAsT == null) {
			binder.addError(BINDING_TO_NULL);
		}

		setBinding(new InstanceBindingImpl<T>(base.getSource(), key, base.getScoping(),
				ImmutableSet.<InjectionPoint> of(), instanceAsT));
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConstantBindingBuilder";
	}
}