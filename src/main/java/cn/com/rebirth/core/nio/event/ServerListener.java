/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server ServerListener.java 2012-3-16 22:28:36 l.xue.nong$$
 */
package cn.com.rebirth.core.nio.event;

import cn.com.rebirth.core.nio.Request;
import cn.com.rebirth.core.nio.Response;

/**
 * The listener interface for receiving server events.
 * The class that is interested in processing a server
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addServerListener<code> method. When
 * the server event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ServerEvent
 */
public interface ServerListener {

	/**
	 * On error.
	 *
	 * @param error the error
	 */
	public void onError(String error);

	/**
	 * On accept.
	 *
	 * @throws Exception the exception
	 */
	public void onAccept() throws Exception;

	/**
	 * On accepted.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void onAccepted(Request request) throws Exception;

	/**
	 * On read.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void onRead(Request request) throws Exception;

	/**
	 * On write.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	public void onWrite(Request request, Response response) throws Exception;

	/**
	 * On closed.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void onClosed(Request request) throws Exception;
}
