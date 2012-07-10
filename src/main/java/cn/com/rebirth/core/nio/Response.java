/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server Response.java 2012-3-16 22:29:05 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.com.rebirth.commons.utils.ObjectToByteUtils;

/**
 * The Class Response.
 *
 * @author l.xue.nong
 */
public class Response {

	/** The sc. */
	private SocketChannel sc;

	/**
	 * Instantiates a new response.
	 *
	 * @param sc the sc
	 */
	public Response(SocketChannel sc) {
		this.sc = sc;
	}

	/**
	 * Send.
	 *
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void send(byte[] data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data, 0, data.length);
		buffer.flip();
		sc.write(buffer);
	}

	/**
	 * Send.
	 *
	 * @param object the object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void send(Object object) throws IOException {
		sc.write(ObjectToByteUtils.getByteBuffer(object));
	}
}
