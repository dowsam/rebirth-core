/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core NioServer.java 2012-3-17 16:02:40 l.xue.nong$$
 */
package cn.com.rebirth.core.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.core.nio.event.ServerListener;

/**
 * The Class NioServer.
 *
 * @author l.xue.nong
 */
public class NioServer implements Runnable {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());
	/** The wpool. */
	private static List<SelectionKey> wpool = new LinkedList<SelectionKey>(); // 回应池

	/** The selector. */
	private static Selector selector;

	/** The sschannel. */
	private ServerSocketChannel sschannel;

	/** The address. */
	private InetSocketAddress address;

	/** The notifier. */
	protected Notifier notifier;

	/** The MA x_ threads. */
	private static int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

	/** The atomic integer. */
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	/** The port. */
	private int port;

	/**
	 * Instantiates a new nio server.
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public NioServer(int port) throws IOException {
		super();
		// 获取事件触发器
		notifier = Notifier.getNotifier();
		//register Listener
		registerListener();
		// 创建读写线程池
		for (int i = 0; i < MAX_THREADS; i++) {
			Thread r = new Reader();
			Thread w = new Writer();
			r.setName(Reader.class.getName() + "-" + atomicInteger.incrementAndGet());
			r.start();
			w.setName(Writer.class.getName() + "-" + atomicInteger.incrementAndGet());
			w.start();
		}

		// 创建无阻塞网络套接
		selector = Selector.open();
		sschannel = ServerSocketChannel.open();
		sschannel.configureBlocking(false);
		address = new InetSocketAddress(port);
		ServerSocket ss = sschannel.socket();
		ss.bind(address);
		sschannel.register(selector, SelectionKey.OP_ACCEPT);
		this.port = port;
	}

	/**
	 * Register listener.
	 */
	private void registerListener() {
		List<ServerListener> objects = ClassResolverUtils.findImpl(ServerListener.class);
		if (objects != null) {
			for (Object object : objects) {
				notifier.addListener((ServerListener) object);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.info("JavaNio Server is Listing on:= " + port);
		// 监听
		while (true) {
			try {
				int num = 0;
				num = selector.select();
				if (num > 0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedKeys.iterator();
					while (it.hasNext()) {
						SelectionKey key = (SelectionKey) it.next();
						it.remove();
						// 处理IO事件
						if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
							// Accept the new connection
							ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
							notifier.fireOnAccept();

							SocketChannel sc = ssc.accept();
							sc.configureBlocking(false);

							// 触发接受连接事件
							Request request = new Request(sc);
							notifier.fireOnAccepted(request);

							// 注册读操作,以进行下一步的读操作
							sc.register(selector, SelectionKey.OP_READ, request);
						} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
							Reader.processRequest(key); // 提交读服务线程读取客户端数据
							key.cancel();
						} else if ((key.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
							Writer.processRequest(key); // 提交写服务线程向客户端发送回应数据
							key.cancel();
						}
					}
				} else {
					addRegister(); // 在Selector中注册新的写通道
				}
			} catch (Exception e) {
				notifier.fireOnError("Error occured in Server: " + e.getMessage());
				continue;
			}
		}
	}

	/**
	 * Process write request.
	 *
	 * @param key the key
	 */
	public static void processWriteRequest(SelectionKey key) {
		synchronized (wpool) {
			wpool.add(wpool.size(), key);
			wpool.notifyAll();
		}
		selector.wakeup(); // 解除selector的阻塞状态，以便注册新的通道
	}

	/**
	 * Adds the register.
	 */
	private void addRegister() {
		synchronized (wpool) {
			while (!wpool.isEmpty()) {
				SelectionKey key = wpool.remove(0);
				SocketChannel schannel = (SocketChannel) key.channel();
				try {
					schannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
				} catch (Exception e) {
					try {
						schannel.finishConnect();
						schannel.close();
						schannel.socket().close();
						notifier.fireOnClosed((Request) key.attachment());
					} catch (Exception e1) {
					}
					notifier.fireOnError("Error occured in addRegister: " + e.getMessage());
				}
			}
		}
	}
}
