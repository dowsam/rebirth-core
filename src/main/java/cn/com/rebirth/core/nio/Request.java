/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server Request.java 2012-3-16 22:28:17 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.nio.channels.SocketChannel;

/**
 * The Class Request.
 *
 * @author l.xue.nong
 */
public class Request {

	/** The sc. */
	private SocketChannel sc;

	/** The data input. */
	private byte[] dataInput = null;;

	/** The obj. */
	Object obj;

	/**
	 * Instantiates a new request.
	 *
	 * @param sc the sc
	 */
	public Request(SocketChannel sc) {
		this.sc = sc;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public java.net.InetAddress getAddress() {
		return sc.socket().getInetAddress();
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return sc.socket().getPort();
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return sc.isConnected();
	}

	/**
	 * Checks if is blocking.
	 *
	 * @return true, if is blocking
	 */
	public boolean isBlocking() {
		return sc.isBlocking();
	}

	/**
	 * Checks if is connection pending.
	 *
	 * @return true, if is connection pending
	 */
	public boolean isConnectionPending() {
		return sc.isConnectionPending();
	}

	/**
	 * Gets the keep alive.
	 *
	 * @return the keep alive
	 * @throws SocketException the socket exception
	 */
	public boolean getKeepAlive() throws java.net.SocketException {
		return sc.socket().getKeepAlive();
	}

	/**
	 * Gets the so timeout.
	 *
	 * @return the so timeout
	 * @throws SocketException the socket exception
	 */
	public int getSoTimeout() throws java.net.SocketException {
		return sc.socket().getSoTimeout();
	}

	/**
	 * Gets the tcp no delay.
	 *
	 * @return the tcp no delay
	 * @throws SocketException the socket exception
	 */
	public boolean getTcpNoDelay() throws java.net.SocketException {
		return sc.socket().getTcpNoDelay();
	}

	/**
	 * Checks if is closed.
	 *
	 * @return true, if is closed
	 */
	public boolean isClosed() {
		return sc.socket().isClosed();
	}

	/**
	 * Attach.
	 *
	 * @param obj the obj
	 */
	public void attach(Object obj) {
		this.obj = obj;
	}

	/**
	 * Attachment.
	 *
	 * @return the object
	 */
	public Object attachment() {
		return obj;
	}

	/**
	 * Gets the data input.
	 *
	 * @return the data input
	 */
	public byte[] getDataInput() {
		return dataInput;
	}

	/**
	 * Sets the data input.
	 *
	 * @param dataInput the new data input
	 */
	public void setDataInput(byte[] dataInput) {
		this.dataInput = dataInput;
	}
}
