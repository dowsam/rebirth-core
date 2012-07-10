/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server Notifier.java 2012-3-16 22:33:00 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.util.ArrayList;
import java.util.List;

import cn.com.rebirth.core.nio.event.ServerListener;

/**
 * The Class Notifier.
 *
 * @author l.xue.nong
 */
public class Notifier {

	/** The listeners. */
	private static List<ServerListener> listeners = null;

	/** The instance. */
	private static Notifier instance = null;

	/**
	 * Instantiates a new notifier.
	 */
	private Notifier() {
		listeners = new ArrayList<ServerListener>();
	}

	/**
	 * Gets the notifier.
	 *
	 * @return the notifier
	 */
	public static synchronized Notifier getNotifier() {
		if (instance == null) {
			instance = new Notifier();
			return instance;
		} else
			return instance;
	}

	/**
	 * Adds the listener.
	 *
	 * @param l the l
	 */
	public void addListener(ServerListener l) {
		synchronized (listeners) {
			if (!listeners.contains(l))
				listeners.add(l);
		}
	}

	/**
	 * Fire on accept.
	 *
	 * @throws Exception the exception
	 */
	public void fireOnAccept() throws Exception {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onAccept();
	}

	/**
	 * Fire on accepted.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void fireOnAccepted(Request request) throws Exception {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onAccepted(request);
	}

	/**
	 * Fire on read.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	void fireOnRead(Request request) throws Exception {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onRead(request);

	}

	/**
	 * Fire on write.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	void fireOnWrite(Request request, Response response) throws Exception {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onWrite(request, response);

	}

	/**
	 * Fire on closed.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void fireOnClosed(Request request) throws Exception {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onClosed(request);
	}

	/**
	 * Fire on error.
	 *
	 * @param error the error
	 */
	public void fireOnError(String error) {
		for (int i = listeners.size() - 1; i >= 0; i--)
			(listeners.get(i)).onError(error);
	}
}
