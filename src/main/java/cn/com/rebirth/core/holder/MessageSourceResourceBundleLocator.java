/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core MessageSourceResourceBundleLocator.java 2012-2-11 16:20:03 l.xue.nong$$
 */
package cn.com.rebirth.core.holder;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;

/**
 * 将ResourceBundleLocator代理为spring的MessageSource
 * 
 * <pre>
 * 用法:
 *    &lt;!-- 用于启用Hibernate Validator -->
 * 	&lt;bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" >
 * 		&lt;property name="messageInterpolator" ref="messageInterpolator"/>
 * 	&lt;/bean>
 * 
 * 	&lt;bean id="messageInterpolator" class="org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator" >
 * 		&lt;constructor-arg ref="resourceBundleLocator"></constructor-arg>
 * 	&lt;/bean>
 * </pre>
 * 
 * <pre>
 * spring配置:
 * &lt;bean id="resourceBundleLocator" class="cn.com.summall.core.holder.MessageSourceResourceBundleLocator">
 * &lt;property name="messageSource"> 
 * &lt;bean id="validationMessagesMessageSource" class ="org.springframework.context.support.ReloadableResourceBundleMessageSource">
 * &lt;property name="basename" value="classpath:ValidationMessages"/> 
 * &lt;property name="defaultEncoding" value="UTF-8"/> 
 * &lt;property name="cacheSeconds" value="600"/> 
 * &lt;/bean> 
 * &lt;/property> 
 * &lt;/bean>
 * </pre>
 * 
 * @author l.xue.nong
 * 
 */
public class MessageSourceResourceBundleLocator implements
		org.hibernate.validator.spi.resourceloading.ResourceBundleLocator {

	/** The message source. */
	private MessageSource messageSource;

	/**
	 * Instantiates a new message source resource bundle locator.
	 */
	public MessageSourceResourceBundleLocator() {
	}

	/**
	 * Instantiates a new message source resource bundle locator.
	 *
	 * @param messageSource the message source
	 */
	public MessageSourceResourceBundleLocator(MessageSource messageSource) {
		setMessageSource(messageSource);
	}

	/**
	 * Sets the message source.
	 *
	 * @param messageSource the new message source
	 */
	public void setMessageSource(MessageSource messageSource) {
		if (messageSource == null)
			throw new IllegalArgumentException("'messageSource' must be not null");
		this.messageSource = messageSource;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.validator.resourceloading.ResourceBundleLocator#getResourceBundle(java.util.Locale)
	 */
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MessageSourceResourceBundle(messageSource, locale);
	}

}
