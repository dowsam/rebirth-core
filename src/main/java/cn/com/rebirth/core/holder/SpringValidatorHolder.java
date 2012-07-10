/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core SpringValidatorHolder.java 2012-2-11 16:04:12 l.xue.nong$$
 */
package cn.com.rebirth.core.holder;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The Class SpringValidatorHolder.
 *
 * @author l.xue.nong
 */
@Component
public class SpringValidatorHolder implements InitializingBean {

	/** The validator. */
	private static Validator validator;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (validator == null)
			throw new BeanCreationException("not found spring 'validator' for SpringValidatorHolder ");
	}

	/**
	 * Sets the validator.
	 *
	 * @param v the new validator
	 */
	@SuppressWarnings("all")
	public void setValidator(Validator v) {
		if (validator != null) {
			throw new IllegalStateException("SpringValidatorHolder already holded 'validator'");
		}
		validator = v;
	}

	/**
	 * Gets the required validator.
	 *
	 * @return the required validator
	 */
	private static Validator getRequiredValidator() {
		if (validator == null)
			throw new IllegalStateException("'validator' property is null,SpringValidatorHolder not yet init.");
		return validator;
	}

	/**
	 * Gets the validator.
	 *
	 * @return the validator
	 */
	public static Validator getValidator() {
		return getRequiredValidator();
	}

	/**
	 * Supports.
	 *
	 * @param type the type
	 * @return true, if successful
	 */
	public static boolean supports(Class<?> type) {
		return getRequiredValidator().supports(type);
	}

	/**
	 * Validate.
	 *
	 * @param object the object
	 * @param errors the errors
	 */
	public static void validate(Object object, Errors errors) {
		getRequiredValidator().validate(object, errors);
	}

	/**
	 * Validate.
	 *
	 * @param object the object
	 * @throws BindException the bind exception
	 */
	public static void validate(Object object) throws BindException {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(object, object.getClass().getSimpleName());
		getRequiredValidator().validate(object, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
	}

	/**
	 * Clean holder.
	 */
	public static void cleanHolder() {
		validator = null;
	}
}
