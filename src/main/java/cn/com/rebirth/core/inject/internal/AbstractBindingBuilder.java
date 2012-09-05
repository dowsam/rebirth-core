/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AbstractBindingBuilder.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.List;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Scope;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.InstanceBinding;


/**
 * The Class AbstractBindingBuilder.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public abstract class AbstractBindingBuilder<T> {

	
	/** The Constant IMPLEMENTATION_ALREADY_SET. */
	public static final String IMPLEMENTATION_ALREADY_SET = "Implementation is set more than once.";

	
	/** The Constant SINGLE_INSTANCE_AND_SCOPE. */
	public static final String SINGLE_INSTANCE_AND_SCOPE = "Setting the scope is not permitted when binding to a single instance.";

	
	/** The Constant SCOPE_ALREADY_SET. */
	public static final String SCOPE_ALREADY_SET = "Scope is set more than once.";

	
	/** The Constant BINDING_TO_NULL. */
	public static final String BINDING_TO_NULL = "Binding to null instances is not allowed. "
			+ "Use toProvider(Providers.of(null)) if this is your intended behaviour.";

	
	/** The Constant CONSTANT_VALUE_ALREADY_SET. */
	public static final String CONSTANT_VALUE_ALREADY_SET = "Constant value is set more than once.";

	
	/** The Constant ANNOTATION_ALREADY_SPECIFIED. */
	public static final String ANNOTATION_ALREADY_SPECIFIED = "More than one annotation is specified for this binding.";

	
	/** The Constant NULL_KEY. */
	protected static final Key<?> NULL_KEY = Key.get(Void.class);

	
	/** The elements. */
	protected List<Element> elements;

	
	/** The position. */
	protected int position;

	
	/** The binder. */
	protected final Binder binder;

	
	/** The binding. */
	private BindingImpl<T> binding;

	
	/**
	 * Instantiates a new abstract binding builder.
	 *
	 * @param binder the binder
	 * @param elements the elements
	 * @param source the source
	 * @param key the key
	 */
	public AbstractBindingBuilder(Binder binder, List<Element> elements, Object source, Key<T> key) {
		this.binder = binder;
		this.elements = elements;
		this.position = elements.size();
		this.binding = new UntargettedBindingImpl<T>(source, key, Scoping.UNSCOPED);
		elements.add(position, this.binding);
	}

	
	/**
	 * Gets the binding.
	 *
	 * @return the binding
	 */
	protected BindingImpl<T> getBinding() {
		return binding;
	}

	
	/**
	 * Sets the binding.
	 *
	 * @param binding the binding
	 * @return the binding impl
	 */
	protected BindingImpl<T> setBinding(BindingImpl<T> binding) {
		this.binding = binding;
		elements.set(position, binding);
		return binding;
	}

	
	/**
	 * Annotated with internal.
	 *
	 * @param annotationType the annotation type
	 * @return the binding impl
	 */
	protected BindingImpl<T> annotatedWithInternal(Class<? extends Annotation> annotationType) {
		checkNotNull(annotationType, "annotationType");
		checkNotAnnotated();
		return setBinding(binding.withKey(Key.get(this.binding.getKey().getTypeLiteral(), annotationType)));
	}

	
	/**
	 * Annotated with internal.
	 *
	 * @param annotation the annotation
	 * @return the binding impl
	 */
	protected BindingImpl<T> annotatedWithInternal(Annotation annotation) {
		checkNotNull(annotation, "annotation");
		checkNotAnnotated();
		return setBinding(binding.withKey(Key.get(this.binding.getKey().getTypeLiteral(), annotation)));
	}

	
	/**
	 * In.
	 *
	 * @param scopeAnnotation the scope annotation
	 */
	public void in(final Class<? extends Annotation> scopeAnnotation) {
		checkNotNull(scopeAnnotation, "scopeAnnotation");
		checkNotScoped();
		setBinding(getBinding().withScoping(Scoping.forAnnotation(scopeAnnotation)));
	}

	
	/**
	 * In.
	 *
	 * @param scope the scope
	 */
	public void in(final Scope scope) {
		checkNotNull(scope, "scope");
		checkNotScoped();
		setBinding(getBinding().withScoping(Scoping.forInstance(scope)));
	}

	
	/**
	 * As eager singleton.
	 */
	public void asEagerSingleton() {
		checkNotScoped();
		setBinding(getBinding().withScoping(Scoping.EAGER_SINGLETON));
	}

	
	/**
	 * Key type is set.
	 *
	 * @return true, if successful
	 */
	protected boolean keyTypeIsSet() {
		return !Void.class.equals(binding.getKey().getTypeLiteral().getType());
	}

	
	/**
	 * Check not targetted.
	 */
	protected void checkNotTargetted() {
		if (!(binding instanceof UntargettedBindingImpl)) {
			binder.addError(IMPLEMENTATION_ALREADY_SET);
		}
	}

	
	/**
	 * Check not annotated.
	 */
	protected void checkNotAnnotated() {
		if (binding.getKey().getAnnotationType() != null) {
			binder.addError(ANNOTATION_ALREADY_SPECIFIED);
		}
	}

	
	/**
	 * Check not scoped.
	 */
	protected void checkNotScoped() {
		
		if (binding instanceof InstanceBinding) {
			binder.addError(SINGLE_INSTANCE_AND_SCOPE);
			return;
		}

		if (binding.getScoping().isExplicitlyScoped()) {
			binder.addError(SCOPE_ALREADY_SET);
		}
	}
}