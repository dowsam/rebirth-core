/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core MockLog4jAppender.java 2012-2-3 10:13:33 l.xue.nong$$
 */
package cn.com.rebirth.core.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 在List中保存日志的Appender, 用于测试Log4j的日志输出.
 * 
 * 在测试开始前, 使用任意一种addToLogger()方法将此appender添加到需要侦听的logger中.
 *
 * @author l.xue.nong
 */
public class MockLog4jAppender extends AppenderSkeleton {

	/** The logs. */
	private List<LoggingEvent> logs = new ArrayList<LoggingEvent>();

	/**
	 * 返回之前append的第一个log.
	 *
	 * @return the first log
	 */
	public LoggingEvent getFirstLog() {
		if (logs.isEmpty()) {
			return null;
		}
		return logs.get(0);
	}

	/**
	 * 返回之前append的第一个log的信息.
	 *
	 * @return the first message
	 */
	public String getFirstMessage() {
		return getFirstLog().getMessage().toString();
	}

	/**
	 * 返回之前appender的第一个log的按layout pattern格式化的信息.
	 *
	 * @return the first rendered message
	 */
	public String getFirstRenderedMessage() {
		return getLayout().format(getFirstLog());
	}

	/**
	 * 返回之前append的最后一个log.
	 *
	 * @return the last log
	 */
	public LoggingEvent getLastLog() {
		if (logs.isEmpty()) {
			return null;
		}
		return logs.get(logs.size() - 1);
	}

	/**
	 * 返回之前append的最后一个log的信息.
	 *
	 * @return the last message
	 */
	public String getLastMessage() {
		return getLastLog().getMessage().toString();
	}

	/**
	 * 返回之前appender的最后一个log的按layout pattern格式化的信息.
	 *
	 * @return the last rendered message
	 */
	public String getLastRenderedMessage() {
		return getLayout().format(getLastLog());
	}

	/**
	 * 返回之前append的所有log.
	 *
	 * @return the all logs
	 */
	public List<LoggingEvent> getAllLogs() {
		return logs;
	}

	/**
	 * 判断是否有log.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return logs.isEmpty();
	}

	/**
	 * 清除之前append的所有log.
	 */
	public void clearLogs() {
		logs.clear();
	}

	/**
	 * 将此appender添加到logger中.
	 *
	 * @param loggerName the logger name
	 */
	public void addToLogger(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);
		logger.addAppender(this);
	}

	/**
	 * 将此appender添加到logger中.
	 *
	 * @param loggerClass the logger class
	 */
	public void addToLogger(Class<?> loggerClass) {
		Logger logger = Logger.getLogger(loggerClass);
		logger.addAppender(this);
	}

	/**
	 * 将此appender从logger中移除.
	 *
	 * @param loggerName the logger name
	 */
	public void removeFromLogger(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);
		logger.removeAppender(this);
	}

	/**
	 * 将此appender从logger中移除.
	 *
	 * @param loggerClass the logger class
	 */
	public void removeFromLogger(Class<?> loggerClass) {
		Logger logger = Logger.getLogger(loggerClass);
		logger.removeAppender(this);
	}

	/**
	 * 设置输出格式.
	 *
	 * @param pattern the new layout
	 */
	public void setLayout(String pattern) {
		super.setLayout(new PatternLayout(pattern));
	}

	/**
	 * 实现AppenderSkeleton的append函数, 将log加入到内部的List.
	 *
	 * @param event the event
	 */
	@Override
	protected void append(LoggingEvent event) {
		logs.add(event);
	}

	/**
	 * Close.
	 *
	 * @see AppenderSkeleton#close()
	 */
	@Override
	public void close() {
		logs.clear();
	}

	/**
	 * Requires layout.
	 *
	 * @return true, if successful
	 * @see AppenderSkeleton#requiresLayout()
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}
}
