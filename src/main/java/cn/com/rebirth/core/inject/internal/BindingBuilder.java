/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BindingBuilder.java 2012-7-6 10:23:44 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.binder.AnnotatedBindingBuilder;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableSet;


/**
 * The Class BindingBuilder.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class BindingBuilder<T> extends AbstractBindingBuilder<T> implements AnnotatedBindingBuilder<T> {

	
	/**
	 * Instantiates a new binding builder.
	 *
	 * @param binder the binder
	 * @param elements the elements
	 * @param source the source
	 * @param key the key
	 */
	public BindingBuilder(Binder binder, List<Element> elements, Object source, Key<T> key) {
		super(binder, elements, source, key);
	}

	
	public BindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
		annotatedWithInternal(annotationType);
		return this;
	}

	
	public BindingBuilder<T> annotatedWith(Annotation annotation) {
		annotatedWithInternal(annotation);
		return this;
	}

	
	public BindingBuilder<T> to(Class<? extends T> implementation) {
		return to(Key.get(implementation));
	}

	
	public BindingBuilder<T> to(TypeLiteral<? extends T> implementation) {
		return to(Key.get(implementation));
	}

	
	public BindingBuilder<T> to(Key<? extends T> linkedKey) {
		checkNotNull(linkedKey, "linkedKey");
		checkNotTargetted();
		BindingImpl<T> base = getBinding();
		setBinding(new LinkedBindingImpl<T>(base.getSource(), base.getKey(), base.getScoping(), linkedKey));
		return this;
	}

	
	public void toInstance(T instance) {
		checkNotTargetted();

		
		Set<InjectionPoint> injectionPoints;
		if (instance != null) {
			try {
				injectionPoints = InjectionPoint.forInstanceMethodsAndFields(instance.getClass());
			} catch (ConfigurationException e) {
				for (Message message : e.getErrorMessages()) {
					binder.addError(message);
				}
				injectionPoints = e.getPartialValue();
			}
		} else {
			binder.addError(BINDING_TO_NULL);
			injectionPoints = ImmutableSet.of();
		}

		BindingImpl<T> base = getBinding();
		setBinding(new InstanceBindingImpl<T>(base.getSource(), base.getKey(), base.getScoping(), injectionPoints,
				instance));
	}

	
	public BindingBuilder<T> toProvider(Provider<? extends T> provider) {
		checkNotNull(provider, "provider");
		checkNotTargetted();

		
		Set<InjectionPoint> injectionPoints;
		try {
			injectionPoints = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
		} catch (ConfigurationException e) {
			for (Message message : e.getErrorMessages()) {
				binder.addError(message);
			}
			injectionPoints = e.getPartialValue();
		}

		BindingImpl<T> base = getBinding();
		setBinding(new ProviderInstanceBindingImpl<T>(base.getSource(), base.getKey(), base.getScoping(),
				injectionPoints, provider));
		return this;
	}

	
	public BindingBuilder<T> toProvider(Class<? extends Provider<? extends T>> providerType) {
		return toProvider(Key.get(providerType));
	}

	
	public BindingBuilder<T> toProvider(Key<? extends Provider<? extends T>> providerKey) {
		checkNotNull(providerKey, "providerKey");
		checkNotTargetted();

		BindingImpl<T> base = getBinding();
		setBinding(new LinkedProviderBindingImpl<T>(base.getSource(), base.getKey(), base.getScoping(), providerKey));
		return this;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BindingBuilder<" + getBinding().getKey().getTypeLiteral() + ">";
	}
}
