/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server Writer.java 2012-3-16 22:37:30 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class Writer.
 *
 * @author l.xue.nong
 */
public class Writer extends Thread {

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
					key = (SelectionKey) pool.remove(0);
				}

				// 处理写事件
				write(key);
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * Write.
	 *
	 * @param key the key
	 */
	public void write(SelectionKey key) {
		try {
			SocketChannel sc = (SocketChannel) key.channel();
			Response response = new Response(sc);

			// 触发onWrite事件
			notifier.fireOnWrite((Request) key.attachment(), response);

			// 关闭
			sc.finishConnect();
			sc.socket().close();
			sc.close();

			// 触发onClosed事件
			notifier.fireOnClosed((Request) key.attachment());
		} catch (Exception e) {
			notifier.fireOnError("Error occured in Writer: " + e.getMessage());
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

}
