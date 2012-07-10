/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core BlockingConsumer.java 2012-2-3 11:26:46 l.xue.nong$$
 */
package cn.com.rebirth.core.queue;

/**
 * 采用即时阻塞读取Queue中消息策略的Consumer.
 *
 * @author l.xue.nong
 */
public abstract class BlockingConsumer extends QueueConsumer {

	/**
	 * 线程执行函数,阻塞获取消息并调用processMessage()进行处理.
	 */
	public void run() {
		// 循环阻塞获取消息直到线程被中断.
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Object message = queue.take();
				processMessage(message);
			}
		} catch (InterruptedException e) {
			logger.debug("消费线程阻塞被中断");
		} finally {
			// 退出线程前调用清理函数.
			clean();
		}
	}

	/**
	 * 消息处理函数.
	 *
	 * @param message the message
	 */
	protected abstract void processMessage(Object message);

	/**
	 * 退出清理函数.
	 */
	protected abstract void clean();
}
