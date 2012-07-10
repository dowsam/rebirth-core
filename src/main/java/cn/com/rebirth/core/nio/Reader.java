/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server Reader.java 2012-3-16 22:47:45 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class Reader.
 *
 * @author l.xue.nong
 */
public class Reader extends Thread {

	/** The pool. */
	private static List<SelectionKey> pool = new LinkedList<SelectionKey>();

	/** The notifier. */
	private static Notifier notifier = Notifier.getNotifier();

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				SelectionKey key;
				synchronized (pool) {
					while (pool.isEmpty()) {
						pool.wait();
					}
					key = pool.remove(0);
				}

				// 读取数据
				read(key);
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * Read request.
	 *
	 * @param sc the sc
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] readRequest(SocketChannel sc) throws IOException {
		//		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		//		int off = 0;
		//		int r = 0;
		//		byte[] data = new byte[BUFFER_SIZE * 10];
		//
		//		while (true) {
		//			buffer.clear();
		//			r = sc.read(buffer);
		//			if (r == -1 || r == 0)
		//				break;
		//			if ((off + r) > data.length) {
		//				data = grow(data, BUFFER_SIZE * 10);
		//			}
		//			byte[] buf = buffer.array();
		//			System.arraycopy(buf, 0, data, off, r);
		//			off += r;
		//		}
		//		byte[] req = new byte[off];
		//		System.arraycopy(data, 0, req, 0, off);
		//		return req;
		ByteBuffer readBuffer = ByteBuffer.allocate(8192);
		int numRead = sc.read(readBuffer);
		byte[] dataCopy = new byte[numRead];
		System.arraycopy(readBuffer.array(), 0, dataCopy, 0, numRead);
		return dataCopy;
	}

	/**
	 * Read.
	 *
	 * @param key the key
	 */
	public void read(SelectionKey key) {
		try {
			// 读取客户端数据
			SocketChannel sc = (SocketChannel) key.channel();
			byte[] clientData = readRequest(sc);

			Request request = (Request) key.attachment();
			request.setDataInput(clientData);

			// 触发onRead
			notifier.fireOnRead(request);

			// 提交主控线程进行写处理
			NioServer.processWriteRequest(key);
		} catch (Exception e) {
			notifier.fireOnError("Error occured in Reader: " + e.getMessage());
		}
	}

	/**
	 * Process request.
	 *
	 * @param key the key
	 */
	public static void processRequest(SelectionKey key) {
		synchronized (pool) {
			pool.add(pool.size(), key);
			pool.notifyAll();
		}
	}

	/**
	 * Grow.
	 *
	 * @param src the src
	 * @param size the size
	 * @return the byte[]
	 */
	public static byte[] grow(byte[] src, int size) {
		byte[] tmp = new byte[src.length + size];
		System.arraycopy(src, 0, tmp, 0, src.length);
		return tmp;
	}

}
