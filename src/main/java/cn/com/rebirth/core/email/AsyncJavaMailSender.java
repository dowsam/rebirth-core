/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AsyncJavaMailSender.java 2012-2-2 17:27:54 l.xue.nong$$
 */
package cn.com.rebirth.core.email;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import cn.com.rebirth.core.concurrent.AsyncFactory;
import cn.com.rebirth.core.concurrent.AsyncTokenUtils;
import cn.com.rebirth.core.concurrent.DefaultAsyncFactory;
import cn.com.rebirth.core.concurrent.Token;

/**
 * 提供异步发送邮件,启动监听模式.
 *
 * @author l.xue.nong
 */
public class AsyncJavaMailSender implements InitializingBean, DisposableBean, BeanNameAware {

	/** The Constant log. */
	protected static final Logger log = LoggerFactory.getLogger(AsyncJavaMailSender.class);

	/** The shutdown executor service. */
	protected boolean shutdownExecutorService = true;

	/** The bean name. */
	private String beanName;

	/** The send mail thread pool size. */
	protected int sendMailThreadPoolSize = 0;

	/** The executor service. */
	protected ExecutorService executorService; // 邮件发送的线程池

	/** The wait for tasks to complete on shutdown. */
	protected boolean waitForTasksToCompleteOnShutdown = true;

	/** The async token factory. */
	protected AsyncFactory asyncTokenFactory = new DefaultAsyncFactory();

	/** The mime mail service. */
	protected MimeMailService mimeMailService;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	/**
	 * Sets the bean name.
	 *
	 * @param name the new bean name
	 */
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	/**
	 * Destroy.
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void destroy() throws Exception {
		if (shutdownExecutorService) {
			shutdown();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	/**
	 * After properties set.
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (executorService == null && sendMailThreadPoolSize > 0) {
			executorService = Executors.newFixedThreadPool(sendMailThreadPoolSize, new CustomizableThreadFactory(
					getClass().getSimpleName() + "-"));
			log.info("create send mail executorService,sendMailThreadPoolSize:" + sendMailThreadPoolSize);
		}
		Assert.notNull(mimeMailService, "mimeMailService must be not null");
		Assert.notNull(executorService, "executorService must be not null");
		Assert.notNull(asyncTokenFactory, "asyncTokenFactory must be not null");
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		log.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		if (waitForTasksToCompleteOnShutdown)
			executorService.shutdown();
		else
			executorService.shutdownNow();
	}

	/**
	 * Checks if is wait for tasks to complete on shutdown.
	 *
	 * @return true, if is wait for tasks to complete on shutdown
	 */
	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	/**
	 * Sets the wait for tasks to complete on shutdown.
	 *
	 * @param waitForTasksToCompleteOnShutdown the new wait for tasks to complete on shutdown
	 */
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	/**
	 * Sets the send mail thread pool size.
	 *
	 * @param sendMailThreadPool the new send mail thread pool size
	 */
	public void setSendMailThreadPoolSize(int sendMailThreadPool) {
		this.sendMailThreadPoolSize = sendMailThreadPool;
	}

	/**
	 * Gets the executor service.
	 *
	 * @return the executor service
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * Gets the async token factory.
	 *
	 * @return the async token factory
	 */
	public AsyncFactory getAsyncTokenFactory() {
		return asyncTokenFactory;
	}

	/**
	 * Sets the async token factory.
	 *
	 * @param asyncTokenFactory the new async token factory
	 */
	public void setAsyncTokenFactory(AsyncFactory asyncTokenFactory) {
		this.asyncTokenFactory = asyncTokenFactory;
	}

	/**
	 * Sets the executor service.
	 *
	 * @param executorService the new executor service
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * Sets the shutdown executor service.
	 *
	 * @param shutdownExecutorService the new shutdown executor service
	 */
	public void setShutdownExecutorService(boolean shutdownExecutorService) {
		this.shutdownExecutorService = shutdownExecutorService;
	}

	/**
	 * Checks if is shutdown executor service.
	 *
	 * @return true, if is shutdown executor service
	 */
	public boolean isShutdownExecutorService() {
		return shutdownExecutorService;
	}

	/**
	 * Gets the mime mail service.
	 *
	 * @return the mime mail service
	 */
	public MimeMailService getMimeMailService() {
		return mimeMailService;
	}

	/**
	 * Sets the mime mail service.
	 *
	 * @param mimeMailService the new mime mail service
	 */
	public void setMimeMailService(MimeMailService mimeMailService) {
		this.mimeMailService = mimeMailService;
	}

	/**
	 * Send.
	 *
	 * @param <T> the generic type
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @return the async token
	 */
	public <T> Token<T> send(final String from, final String to, final String subject) {
		return AsyncTokenUtils.execute(executorService, asyncTokenFactory, new Runnable() {
			@Override
			public void run() {
				mimeMailService.send(from, to, subject);
			}
		});
	}

	/**
	 * Send.
	 *
	 * @param <T> the generic type
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @return the async token
	 */
	public <T> Token<T> send(final String from, final String to, final String subject, final String templateName,
			final Map<String, Object> map) {
		return AsyncTokenUtils.execute(executorService, asyncTokenFactory, new Runnable() {
			@Override
			public void run() {
				mimeMailService.send(from, to, subject, templateName, map);
			}
		});
	}

	/**
	 * Send.
	 *
	 * @param <T> the generic type
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @return the async token
	 */
	public <T> Token<T> send(final String from, final String[] to, final String subject, final String templateName,
			final Map<String, Object> map) {
		return AsyncTokenUtils.execute(executorService, asyncTokenFactory, new Runnable() {
			@Override
			public void run() {
				mimeMailService.send(from, to, subject, templateName, map);
			}
		});
	}

	/**
	 * Send.
	 *
	 * @param <T> the generic type
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @param fileName the file name
	 * @return the async token
	 */
	public <T> Token<T> send(final String from, final String[] to, final String subject, final String templateName,
			final Map<String, Object> map, final String fileName) {
		return AsyncTokenUtils.execute(executorService, asyncTokenFactory, new Runnable() {
			@Override
			public void run() {
				mimeMailService.send(from, to, subject, templateName, map, fileName);
			}
		});
	}

	/**
	 * Send.
	 *
	 * @param <T> the generic type
	 * @param from the from
	 * @param to the to
	 * @param subject the subject
	 * @param templateName the template name
	 * @param map the map
	 * @param fileName the file name
	 * @return the async token
	 */
	public <T> Token<T> send(final String from, final String to, final String subject, final String templateName,
			final Map<String, Object> map, final String fileName) {
		return AsyncTokenUtils.execute(executorService, asyncTokenFactory, new Runnable() {
			@Override
			public void run() {
				mimeMailService.send(from, to, subject, templateName, map, fileName);
			}
		});
	}
}