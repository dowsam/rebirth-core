/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core JmxClientTemplate.java 2012-2-3 10:28:09 l.xue.nong$$
 */
package cn.com.rebirth.core.jmx;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * JMX客户端模板.
 * 
 * 职责有：
 * 1.负责连接和关闭远程JMX Server,并持有连接.
 * 2.创建可操作远程MBean的本地MBean代理.
 * 3.按属性名直接读取或设置远程MBean属性(无MBean的Class文件时使用).
 *
 * @author l.xue.nong
 */
public class JmxClientTemplate {

	/** The connector. */
	private JMXConnector connector;

	/** The connection. */
	private MBeanServerConnection connection;

	/** The connected. */
	private AtomicBoolean connected = new AtomicBoolean(false);

	/**
	 * 无认证信息的构造函数.
	 *
	 * @param serviceUrl the service url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public JmxClientTemplate(final String serviceUrl) throws IOException {
		initConnector(serviceUrl, null, null);
	}

	/**
	 * 带认证信息的构造函数.
	 *
	 * @param serviceUrl the service url
	 * @param userName the user name
	 * @param passwd the passwd
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public JmxClientTemplate(final String serviceUrl, final String userName, final String passwd) throws IOException {
		initConnector(serviceUrl, userName, passwd);
	}

	/**
	 * 连接远程JMX Server.
	 *
	 * @param serviceUrl the service url
	 * @param userName the user name
	 * @param passwd the passwd
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initConnector(final String serviceUrl, final String userName, final String passwd) throws IOException {
		JMXServiceURL url = new JMXServiceURL(serviceUrl);

		boolean hasCredentlals = StringUtils.isNotBlank(userName);
		if (hasCredentlals) {
			Map environment = Collections.singletonMap(JMXConnector.CREDENTIALS, new String[] { userName, passwd });
			connector = JMXConnectorFactory.connect(url, environment);
		} else {
			connector = JMXConnectorFactory.connect(url);
		}

		connection = connector.getMBeanServerConnection();
		connected.set(true);
	}

	/**
	 * 断开JMX连接.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void close() throws IOException {
		connector.close();
		connected.set(false);
		connection = null;
	}

	/**
	 * 创建标准MBean代理.
	 *
	 * @param <T> the generic type
	 * @param mbeanName the mbean name
	 * @param mBeanInterface the m bean interface
	 * @return the t
	 */
	public <T> T createMBeanProxy(final String mbeanName, final Class<T> mBeanInterface) {
		Assert.hasText(mbeanName, "mbeanName不能为空");
		assertConnected();

		ObjectName objectName = buildObjectName(mbeanName);
		return (T) MBeanServerInvocationHandler.newProxyInstance(connection, objectName, mBeanInterface, false);
	}

	/**
	 * 按属性名直接读取MBean属性(无MBean的Class文件时使用).
	 * 
	 * attributeName首字母大写.
	 *
	 * @param mbeanName the mbean name
	 * @param attributeName the attribute name
	 * @return the attribute
	 */
	public Object getAttribute(final String mbeanName, final String attributeName) {
		Assert.hasText(mbeanName, "mbeanName不能为空");
		Assert.hasText(attributeName, "attributeName不能为空");
		assertConnected();

		try {
			ObjectName objectName = buildObjectName(mbeanName);
			return connection.getAttribute(objectName, attributeName);
		} catch (JMException e) {
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 按属性名直接设置MBean属性(无MBean的Class文件时使用).
	 * 
	 * attributeName首字母大写.
	 *
	 * @param mbeanName the mbean name
	 * @param attributeName the attribute name
	 * @param value the value
	 */
	public void setAttribute(final String mbeanName, final String attributeName, final Object value) {
		Assert.hasText(mbeanName, "mbeanName不能为空");
		Assert.hasText(attributeName, "attributeName不能为空");
		assertConnected();

		try {
			ObjectName objectName = buildObjectName(mbeanName);
			Attribute attribute = new Attribute(attributeName, value);
			connection.setAttribute(objectName, attribute);
		} catch (JMException e) {
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 按方法名直接调用MBean方法(无MBean的Class文件时使用).
	 * 
	 * 调用的方法无参数时的简写函数.
	 *
	 * @param mbeanName the mbean name
	 * @param methodName the method name
	 */
	public void inoke(final String mbeanName, final String methodName) {
		invoke(mbeanName, methodName, new String[] {}, new Object[] {});
	}

	/**
	 * 按方法名直接调用MBean方法(无MBean的Class文件时使用).
	 *
	 * @param mbeanName the mbean name
	 * @param methodName the method name
	 * @param paramClassNames 所有参数的Class名全称数组.
	 * @param paramValues the param values
	 * @return the object
	 */
	public Object invoke(final String mbeanName, final String methodName, final String[] paramClassNames,
			final Object[] paramValues) {
		Assert.hasText(mbeanName, "mbeanName不能为空");
		Assert.hasText(methodName, "methodName不能为空");
		assertConnected();

		try {
			ObjectName objectName = buildObjectName(mbeanName);
			return connection.invoke(objectName, methodName, paramValues, paramClassNames);
		} catch (JMException e) {
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 按方法名直接调用MBean方法(无MBean的Class文件时使用).
	 *
	 * @param mbeanName the mbean name
	 * @param methodName the method name
	 * @param paramClasses 所有参数的Class数组.
	 * @param paramValues the param values
	 * @return the object
	 */
	@SuppressWarnings("rawtypes")
	public Object invoke(final String mbeanName, final String methodName, final Class[] paramClasses,
			final Object[] paramValues) {
		String[] paramClassNames = new String[paramClasses.length];
		for (int i = 0; i < paramClasses.length; i++) {
			paramClassNames[i] = paramClasses[i].getName();
		}

		return invoke(mbeanName, methodName, paramClassNames, paramValues);
	}

	/**
	 * 确保Connection已连接.
	 */
	private void assertConnected() {
		if (!connected.get()) {
			throw new IllegalStateException("connector已关闭");
		}
	}

	/**
	 * 构造ObjectName对象,并转换其抛出的异常为unchecked exception.
	 *
	 * @param mbeanName the mbean name
	 * @return the object name
	 */
	private ObjectName buildObjectName(final String mbeanName) {
		try {
			return new ObjectName(mbeanName);
		} catch (MalformedObjectNameException e) {
			throw new IllegalArgumentException("mbeanName:" + mbeanName + "不正确,无法转换为ObjectName.", e);
		}
	}
}
