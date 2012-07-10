/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core WebExceptionFactory.java 2012-2-3 10:09:55 l.xue.nong$$
 */
package cn.com.rebirth.core.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

/**
 * A factory for creating WebException objects.
 */
public class WebExceptionFactory {

	/**
	 * Instantiates a new web exception factory.
	 */
	private WebExceptionFactory() {
	}

	/**
	 * 创建WebApplicationException并记打印日志, 使用标准状态码与自定义信息并记录错误信息.
	 *
	 * @param status the status
	 * @param message the message
	 * @param logger the logger
	 * @return the web application exception
	 */
	public static WebApplicationException buildException(Status status, String message, Logger logger) {
		logger.error(status.getStatusCode() + ":" + message);
		return new WebApplicationException(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build());
	}

	/**
	 * 创建WebApplicationException并打印日志, 使用自定义状态码与自定义信息并记录错误信息.
	 *
	 * @param status the status
	 * @param message the message
	 * @param logger the logger
	 * @return the web application exception
	 */
	public static WebApplicationException buildException(int status, String message, Logger logger) {
		logger.error(status + ":" + message);
		return new WebApplicationException(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build());
	}

	/**
	 * 创建状态码为500的默认WebApplicatonExcetpion, 并在日志中打印RuntimeExcetpion的信息.
	 * 如RuntimeException为WebApplicatonExcetpion则跳过不进行处理.
	 *
	 * @param e the e
	 * @param logger the logger
	 * @return the web application exception
	 */
	public static WebApplicationException buildDefaultException(RuntimeException e, Logger logger) {
		if (e instanceof WebApplicationException) {
			return (WebApplicationException) e;
		} else {
			logger.error("500:" + e.getMessage(), e);
			return new WebApplicationException();
		}
	}
}
