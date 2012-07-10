/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core HibernateBeanValidatorHolder.java 2012-2-11 16:17:53 l.xue.nong$$
 */
package cn.com.rebirth.core.holder;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * The Class HibernateBeanValidatorHolder.
 *
 * @author l.xue.nong
 */
@Component
public class HibernateBeanValidatorHolder implements InitializingBean {
	/** The validator. */
	private static Validator validator;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (validator == null)
			throw new IllegalStateException("not found JSR303(HibernateValidator) 'validator' for BeanValidatorHolder ");
	}

	/**
	 * Sets the validator.
	 *
	 * @param v the new validator
	 */
	public void setValidator(Validator v) {
		if (validator != null) {
			throw new IllegalStateException("BeanValidatorHolder already holded 'validator'");
		}
		validator = v;
	}

	/**
	 * Gets the validator.
	 *
	 * @return the validator
	 */
	public static Validator getValidator() {
		if (validator == null)
			throw new IllegalStateException("'validator' property is null,BeanValidatorHolder not yet init.");
		return validator;
	}

	/**
	 * Validate.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @param groups the groups
	 * @return the sets the
	 */
	public static final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
		return getValidator().validate(object, groups);
	}

	/**
	 * Validate.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @throws ConstraintViolationException the constraint violation exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final <T> void validate(T object) throws ConstraintViolationException {
		Set constraintViolations = getValidator().validate(object);
		String msg = "validate failure on object:" + object.getClass().getSimpleName();
		throw new ConstraintViolationException(msg, constraintViolations);
	}

	/**
	 * Validate property.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @param propertyName the property name
	 * @param groups the groups
	 * @return the sets the
	 */
	public static final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName,
			Class<?>... groups) {
		return getValidator().validateProperty(object, propertyName, groups);
	}

	/**
	 * Validate property.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @param propertyName the property name
	 * @throws ConstraintViolationException the constraint violation exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final <T> void validateProperty(T object, String propertyName) throws ConstraintViolationException {
		Set constraintViolations = getValidator().validateProperty(object, propertyName);
		String msg = "validate property failure on object:" + object.getClass().getSimpleName() + "." + propertyName
				+ "";
		throw new ConstraintViolationException(msg, constraintViolations);
	}

	/**
	 * Validate value.
	 *
	 * @param <T> the generic type
	 * @param beanType the bean type
	 * @param propertyName the property name
	 * @param value the value
	 * @param groups the groups
	 * @return the sets the
	 */
	public static final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName,
			Object value, Class<?>... groups) {
		return getValidator().validateValue(beanType, propertyName, value, groups);
	}

	/**
	 * Validate value.
	 *
	 * @param <T> the generic type
	 * @param beanType the bean type
	 * @param propertyName the property name
	 * @param value the value
	 * @throws ConstraintViolationException the constraint violation exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final <T> void validateValue(Class<T> beanType, String propertyName, Object value)
			throws ConstraintViolationException {
		Set constraintViolations = getValidator().validateValue(beanType, propertyName, value);
		String msg = "validate value failure on object:" + beanType.getSimpleName() + "." + propertyName + " value:"
				+ value;
		throw new ConstraintViolationException(msg, constraintViolations);
	}

	/**
	 * Gets the constraints for class.
	 *
	 * @param clazz the clazz
	 * @return the constraints for class
	 */
	public static final BeanDescriptor getConstraintsForClass(Class<?> clazz) {
		return getValidator().getConstraintsForClass(clazz);
	}

	/**
	 * Unwrap.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the t
	 */
	public static final <T> T unwrap(Class<T> type) {
		return getValidator().unwrap(type);
	}

	/**
	 * Clean holder.
	 */
	public static void cleanHolder() {
		validator = null;
	}
}
