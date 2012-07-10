/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core WebDriverFactory.java 2012-2-3 12:50:18 l.xue.nong$$
 */
package cn.com.rebirth.core.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.util.Assert;

import cn.com.rebirth.commons.utils.ExceptionUtils;

/**
 * A factory for creating WebDriver objects.
 */
public class WebDriverFactory {

	/**
	 * The Enum BrowserType.
	 *
	 * @author l.xue.nong
	 */
	public enum BrowserType {

		/** The firefox. */
		firefox,
		/** The ie. */
		ie,
		/** The remote. */
		remote
	}

	/**
	 * 根据driverName创建各种WebDriver的简便方法.
	 * 
	 * 当持续集成服务器安装在非Windows机器上, 没有IE浏览器与XWindows时, 需要使用remote dirver调用远程的Windows机器.
	 * drivername如remote:192.168.0.2:3000:firefox, 此时要求远程服务器在http://192.168.0.2:3000/wd上启动selnium remote服务.
	 *
	 * @param driverName the driver name
	 * @return the web driver
	 */
	public static WebDriver createDriver(String driverName) {
		WebDriver driver = null;

		if (BrowserType.firefox.name().equals(driverName)) {
			driver = new FirefoxDriver();
		} else if (BrowserType.ie.name().equals(driverName)) {
			driver = new InternetExplorerDriver();
		} else if (driverName.startsWith(BrowserType.remote.name())) {
			String[] params = driverName.split(":");
			Assert.isTrue(params.length == 4,
					"Remote driver is not right, accept format is \"remote:localhost:3000:firefox\", but the input is\""
							+ driverName + "\"");

			String remoteHost = params[1];
			String remotePort = params[2];
			String driverType = params[3];
			DesiredCapabilities cap = null;

			if (BrowserType.firefox.name().equals(driverType)) {
				cap = DesiredCapabilities.firefox();
			} else if (BrowserType.ie.name().equals(driverType)) {
				cap = DesiredCapabilities.internetExplorer();
			}

			try {
				driver = new RemoteWebDriver(new URL("http://" + remoteHost + ":" + remotePort + "/wd"), cap);
			} catch (MalformedURLException e) {
				throw ExceptionUtils.unchecked(e);
			}
		}

		Assert.notNull(driver, "Driver could be found by name:" + driverName);

		return driver;
	}
}
