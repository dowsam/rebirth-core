/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core DefaultAsyncTokenFactory.java 2012-2-2 17:29:36 l.xue.nong$$
 */
package cn.com.rebirth.core.concurrent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * 默认工厂类,主要生产异步监听器,.
 *
 * @author L.xue.nong
 */
public class DefaultAsyncFactory implements AsyncFactory {

	/** The token group. */
	private String tokenGroup = Token.DEFAULT_TOKEN_GROUP;

	/** The token name. */
	private String tokenName;

	/** The responders. */
	private List<Responder> responders = new ArrayList<Responder>();

	/** The uncaught exception handler. */
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	/**
	 * Gets the token group.
	 *
	 * @return the token group
	 */
	public String getTokenGroup() {
		return tokenGroup;
	}

	/**
	 * Sets the token group.
	 *
	 * @param tokenGroup the new token group
	 */
	public void setTokenGroup(String tokenGroup) {
		this.tokenGroup = tokenGroup;
	}

	/**
	 * Gets the token name.
	 *
	 * @return the token name
	 */
	public String getTokenName() {
		return tokenName;
	}

	/**
	 * Sets the token name.
	 *
	 * @param tokenName the new token name
	 */
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	/**
	 * Gets the responders.
	 *
	 * @return the responders
	 */
	public List<Responder> getResponders() {
		return responders;
	}

	/**
	 * Sets the responders.
	 *
	 * @param responders the new responders
	 */
	public void setResponders(List<Responder> responders) {
		Assert.notNull(responders, "responders must be not null");
		this.responders = responders;
	}

	/**
	 * Adds the responder.
	 *
	 * @param r the r
	 */
	public void addResponder(Responder r) {
		this.responders.add(r);
	}

	/**
	 * Gets the uncaught exception handler.
	 *
	 * @return the uncaught exception handler
	 */
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	/**
	 * Sets the uncaught exception handler.
	 *
	 * @param uncaughtExceptionHandler the new uncaught exception handler
	 */
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.concurrent.AsyncTokenFactory#newToken()
	 */
	public <T> Token<T> newToken() {
		Token<T> t = new Token<T>();

		t.setTokenGroup(tokenGroup);
		t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		t.setTokenName(tokenName);

		for (Responder r : responders) {
			t.addResponder(r);
		}

		return t;
	}

}
