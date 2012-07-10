/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server EventAdapter.java 2012-3-16 22:30:53 l.xue.nong$$
 */
package cn.com.rebirth.core.nio.event;

import cn.com.rebirth.core.nio.Request;
import cn.com.rebirth.core.nio.Response;

/**
 * The Class EventAdapter.
 *
 * @author l.xue.nong
 */
public abstract class EventAdapter implements ServerListener {

	/**
	 * Instantiates a new event adapter.
	 */
	public EventAdapter() {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onError(java.lang.String)
	 */
	public void onError(String error) {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onAccept()
	 */
	public void onAccept() throws Exception {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onAccepted(cn.com.summall.search.server.rpc.nio.Request)
	 */
	public void onAccepted(Request request) throws Exception {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onRead(cn.com.summall.search.server.rpc.nio.Request)
	 */
	public void onRead(Request request) throws Exception {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onWrite(cn.com.summall.search.server.rpc.nio.Request, cn.com.summall.search.server.rpc.nio.Response)
	 */
	public void onWrite(Request request, Response response) throws Exception {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.ServerListener#onClosed(cn.com.summall.search.server.rpc.nio.Request)
	 */
	public void onClosed(Request request) throws Exception {
	}
}
