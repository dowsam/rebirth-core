/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server ServerHandler.java 2012-3-16 22:39:13 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.core.nio.event.EventAdapter;

/**
 * The Class ServerHandler.
 *
 * @author l.xue.nong
 */
public class ServerHandler extends EventAdapter {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onError(java.lang.String)
	 */
	@Override
	public void onError(String error) {
		logger.error(error);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onAccept()
	 */
	@Override
	public void onAccept() throws Exception {
		logger.info("begin Connected:=onAccept()");
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onAccepted(cn.com.summall.search.server.rpc.nio.Request)
	 */
	@Override
	public void onAccepted(Request request) throws Exception {
		logger.info("begin Connected:=onAccept(),add:=" + request.getAddress() + ",port:=" + request.getPort());
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onRead(cn.com.summall.search.server.rpc.nio.Request)
	 */
	@Override
	public void onRead(Request request) throws Exception {
		logger.info("#onRead()");
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onWrite(cn.com.summall.search.server.rpc.nio.Request, cn.com.summall.search.server.rpc.nio.Response)
	 */
	@Override
	public void onWrite(Request request, Response response) throws Exception {
		logger.info("#onWrite()");
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.server.rpc.nio.event.EventAdapter#onClosed(cn.com.summall.search.server.rpc.nio.Request)
	 */
	@Override
	public void onClosed(Request request) throws Exception {
		logger.info("#onClosed()");
	}

}
