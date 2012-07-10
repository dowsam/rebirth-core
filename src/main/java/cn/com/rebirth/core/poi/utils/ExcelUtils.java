/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core ExcelUtils.java 2012-2-3 11:16:58 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * The Class ExcelUtils.
 *
 * @author l.xue.nong
 */
public abstract class ExcelUtils {
	/**
	 * 获取工作表的行数.
	 *
	 * @param sheet HSSFSheet表对象
	 * @return 表行数
	 */
	public static int getLastRowNum(Sheet sheet) {
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum == 0) {
			lastRowNum = sheet.getPhysicalNumberOfRows() - 1;
		}
		return lastRowNum;
	}

	/**
	 * 获取该行第一个单元格的下标.
	 *
	 * @param row 行对象
	 * @return 第一个单元格下标，从0开始
	 */
	public static int getFirstCellNum(Row row) {
		return row.getFirstCellNum();
	}

	/**
	 * 获取该行最后一个单元格的下标.
	 *
	 * @param row 行对象
	 * @return 最后一个单元格下标，从0开始
	 */
	public static int getLastCellNum(Row row) {
		return row.getLastCellNum();
	}

	/**
	 * 获取POI的行对象.
	 *
	 * @param sheet 表对象
	 * @param row 行号，从0开始
	 * @return the hSSF row
	 */
	public static Row getRow(Sheet sheet, int row) {
		if (row < 0) {
			row = 0;
		}
		Row r = sheet.getRow(row);
		if (r == null) {
			r = sheet.createRow(row);
		}
		return r;
	}

	/**
	 * 获取单元格对象.
	 *
	 * @param sheet 表对象
	 * @param row 行，从0开始
	 * @param col 列，从0开始
	 * @return row行col列的单元格对象
	 */
	public static Cell getCell(Sheet sheet, int row, int col) {
		Row r = getRow(sheet, row);
		return getCell(r, col);
	}

	/**
	 * 获取单元格对象.
	 *
	 * @param row 行，从0开始
	 * @param col 列，从0开始
	 * @return 指定行对象上第col行的单元格
	 */
	public static Cell getCell(Row row, int col) {
		if (col < 0) {
			col = 0;
		}
		Cell c = row.getCell(col);
		c = c == null ? row.createCell(col) : c;
		return c;
	}

	/**
	 * 获取工作表对象.
	 *
	 * @param workbook 工作簿对象
	 * @param index 表下标，从0开始
	 * @return the hSSF sheet
	 */
	public static Sheet getSheet(Workbook workbook, int index) {
		if (index < 0) {
			index = 0;
		}
		if (index > workbook.getNumberOfSheets() - 1) {
			workbook.createSheet();
			return workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
		} else {
			return workbook.getSheetAt(index);
		}
	}
}
