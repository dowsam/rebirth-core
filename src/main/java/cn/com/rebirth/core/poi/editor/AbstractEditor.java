/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AbstractEditor.java 2012-2-3 11:05:41 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.com.rebirth.core.poi.ExcelContext;
import cn.com.rebirth.core.poi.utils.ExcelUtils;

/**
 * The Class AbstractEditor.
 *
 * @author l.xue.nong
 */
public abstract class AbstractEditor {

	/** The work book. */
	protected Workbook workBook;

	/** The temp cell style. */
	protected CellStyle tempCellStyle;// 临时的样式

	/** The temp font. */
	protected Font tempFont;// 临时的字体

	/** The working sheet. */
	protected Sheet workingSheet;

	/** The ctx. */
	protected ExcelContext ctx;

	/**
	 * Instantiates a new abstract editor.
	 *
	 * @param context the context
	 */
	protected AbstractEditor(ExcelContext context) {
		this.workBook = context.getWorkBook();
		this.workingSheet = context.getWorkingSheet();
		this.tempFont = context.getTempFont();
		this.tempCellStyle = context.getTempCellStyle();
		this.ctx = context;
	}

	/**
	 * Gets the row.
	 *
	 * @param row the row
	 * @return the row
	 */
	protected Row getRow(int row) {
		return ExcelUtils.getRow(this.workingSheet, row);
	}

	/**
	 * Gets the cell.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell
	 */
	protected Cell getCell(int row, int col) {
		return ExcelUtils.getCell(this.workingSheet, row, col);
	}

	/**
	 * Gets the cell.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell
	 */
	protected Cell getCell(Row row, int col) {
		return ExcelUtils.getCell(row, col);
	}

}
