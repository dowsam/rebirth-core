/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core Selenium2.java 2012-2-3 12:47:36 l.xue.nong$$
 */
package cn.com.rebirth.core.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;

import cn.com.rebirth.commons.utils.ThreadUtils;

import com.thoughtworks.selenium.Selenium;

/**
 * The Class Selenium2.
 *
 * @author l.xue.nong
 */
public class Selenium2 {

	/** The Constant DEFAULT_TIMEOUT. */
	public static final int DEFAULT_TIMEOUT = 5000;

	/** The Constant DEFAULT_PAUSE_TIME. */
	public static final int DEFAULT_PAUSE_TIME = 250;

	/** The driver. */
	private WebDriver driver;

	/** The selenium. */
	private Selenium selenium;

	/** The default timeout. */
	private int defaultTimeout = DEFAULT_TIMEOUT;

	/**
	 * Instantiates a new selenium2.
	 *
	 * @param driver the driver
	 * @param baseUrl the base url
	 */
	public Selenium2(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	/**
	 * 不设置baseUrl的构造函数, 调用open函数时必须使用绝对路径.
	 *
	 * @param driver the driver
	 */
	public Selenium2(WebDriver driver) {
		this(driver, "");
	}

	/**
	 * 打开地址,如果url为相对地址, 自动添加baseUrl.
	 *
	 * @param url the url
	 */
	public void open(String url) {
		selenium.open(url);
		waitForPageToLoad();
	}

	/**
	 * 获取页面标题.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * 获取页面地址.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return driver.getCurrentUrl();
	}

	/**
	 * 查找Element.
	 *
	 * @param by the by
	 * @return the web element
	 */
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	/**
	 * 查找所有符合条件的Element.
	 *
	 * @param by the by
	 * @return the list
	 */
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	/**
	 * 判断页面内是否存在文本内容.
	 *
	 * @param text the text
	 * @return true, if is text present
	 */
	public boolean isTextPresent(String text) {
		return selenium.isTextPresent(text);
	}

	/**
	 * 判断页面内是否存在Element.
	 *
	 * @param by the by
	 * @return true, if is element present
	 */
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * 判断Element是否可见.
	 *
	 * @param by the by
	 * @return true, if is displayed
	 */
	public boolean isDisplayed(By by) {
		return driver.findElement(by).isDisplayed();
	}

	/**
	 * 在Element中输入文本内容.
	 *
	 * @param by the by
	 * @param text the text
	 */
	public void type(By by, String text) {
		WebElement element = driver.findElement(by);
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * 点击Element.
	 *
	 * @param by the by
	 */
	public void click(By by) {
		driver.findElement(by).click();
	}

	/**
	 * 点击Element, 跳转到新页面.
	 *
	 * @param by the by
	 */
	public void clickTo(By by) {
		driver.findElement(by).click();
		waitForPageToLoad();
	}

	/**
	 * 选中Element.
	 *
	 * @param by the by
	 */
	public void check(By by) {
		WebElement element = driver.findElement(by);
		element.click();
	}

	/**
	 * 取消Element的选中.
	 *
	 * @param by the by
	 */
	public void uncheck(By by) {
		WebElement element = driver.findElement(by);
		if (element.isSelected()) {
			element.click();
		}
	}

	/**
	 * 判断Element有否被选中.
	 *
	 * @param by the by
	 * @return true, if is checked
	 */
	public boolean isChecked(By by) {
		WebElement element = driver.findElement(by);
		return element.isSelected();
	}

	/**
	 * 获取Element的文本.
	 *
	 * @param by the by
	 * @return the text
	 */
	public String getText(By by) {
		return driver.findElement(by).getText();
	}

	/**
	 * 取得单元格的内容, 序列从0开始, Selnium1.0的常用函数.
	 *
	 * @param table the table
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 * @return the table
	 */
	public String getTable(WebElement table, int rowIndex, int columnIndex) {
		return table.findElement(By.xpath("//tr[" + (rowIndex + 1) + "]//td[" + (columnIndex + 1) + "]")).getText();
	}

	/**
	 * 取得单元格的内容, 序列从0开始, Selnium1.0的常用函数.
	 *
	 * @param by the by
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 * @return the table
	 */
	public String getTable(By by, int rowIndex, int columnIndex) {
		return getTable(driver.findElement(by), rowIndex, columnIndex);
	}

	/**
	 * 等待页面载入完成, timeout时间为defaultTimeout的值.
	 */
	public void waitForPageToLoad() {
		waitForPageToLoad(defaultTimeout);
	}

	/**
	 * 等待页面载入完成, timeout单位为毫秒.
	 *
	 * @param timeout the timeout
	 */
	public void waitForPageToLoad(int timeout) {
		selenium.waitForPageToLoad(String.valueOf(timeout));
	}

	/**
	 * 等待Element的内容展现, timeout单位为毫秒.
	 *
	 * @param by the by
	 * @param timeout the timeout
	 */
	public void waitForVisible(By by, int timeout) {
		long timeoutTime = System.currentTimeMillis() + timeout;
		while (System.currentTimeMillis() < timeoutTime) {
			if (isDisplayed(by)) {
				return;
			}
			ThreadUtils.sleep(DEFAULT_PAUSE_TIME);
		}
		throw new RuntimeException("waitForVisible timeout");
	}

	/**
	 * 退出Selenium.
	 */
	public void quit() {
		driver.close();
		driver.quit();
	}

	/**
	 * 获取WebDriver实例, 调用未封装的函数.
	 *
	 * @return the driver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * 获取Selenium实例,调用未封装的函数.
	 *
	 * @return the selenium
	 */
	public Selenium getSelenium() {
		return selenium;
	}

	/**
	 * 设置默认页面超时.
	 *
	 * @param defaultTimeout the new default timeout
	 */
	public void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}
}
