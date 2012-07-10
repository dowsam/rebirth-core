/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core ExcelFactory.java 2012-2-3 11:25:02 l.xue.nong$$
 */
package cn.com.rebirth.core.poi;

/**
 * A factory for creating Excel objects.
 */
public abstract class ExcelFactory {

	/**
	 * Creates a new Excel object.
	 *
	 * @param excelName the excel name
	 * @return the excel
	 */
	public static Excel createExcel(String excelName) {
		return new Excel(excelName);
	}
}
