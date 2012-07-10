/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core SheetEditor.java 2012-2-3 11:20:18 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Sheet;

import cn.com.rebirth.core.poi.ExcelContext;
import cn.com.rebirth.core.poi.editor.listener.CellValueListener;
import cn.com.rebirth.core.poi.utils.ExcelUtils;

/**
 * The Class SheetEditor.
 *
 * @author l.xue.nong
 */
public class SheetEditor extends AbstractEditor {

	/** The sheet. */
	private Sheet sheet;

	/** The sheet index. */
	private int sheetIndex;

	/**
	 * Instantiates a new sheet editor.
	 *
	 * @param sheet the sheet
	 * @param context the context
	 */
	public SheetEditor(Sheet sheet, ExcelContext context) {
		super(context);
		this.sheet = sheet;
		sheetIndex = workBook.getSheetIndex(this.sheet);
	}

	/**
	 * Header.
	 *
	 * @param left the left
	 * @param center the center
	 * @param right the right
	 * @return the sheet editor
	 */
	public SheetEditor header(String left, String center, String right) {
		Header header = sheet.getHeader();
		header.setLeft(left == null ? "" : left);
		header.setCenter(center == null ? "" : center);
		header.setRight(right == null ? "" : right);
		return this;
	}

	/**
	 * Footer.
	 *
	 * @param left the left
	 * @param center the center
	 * @param right the right
	 * @return the sheet editor
	 */
	public SheetEditor footer(String left, String center, String right) {
		Footer footer = sheet.getFooter();
		footer.setLeft(left == null ? "" : left);
		footer.setCenter(center == null ? "" : center);
		footer.setRight(right == null ? "" : right);
		return this;
	}

	/**
	 * Sheet name.
	 *
	 * @param name the name
	 * @return the sheet editor
	 */
	public SheetEditor sheetName(final String name) {
		sheetName(name, false);
		return this;
	}

	/**
	 * Sheet name.
	 *
	 * @param name the name
	 * @param auto the auto
	 * @return the sheet editor
	 */
	public SheetEditor sheetName(final String name, boolean auto) {
		if (auto) {
			String newName = new String(name);
			Sheet sheet = workBook.getSheet(name);
			while (sheet != null) {
				newName += "_";
				sheet = workBook.getSheet(newName);
			}
			workBook.setSheetName(sheetIndex, newName);
		} else {
			workBook.setSheetName(sheetIndex, name);
		}
		return this;
	}

	/**
	 * Active.
	 *
	 * @return the sheet editor
	 */
	public SheetEditor active() {
		workBook.setActiveSheet(sheetIndex);
		return this;
	}

	/**
	 * Freeze.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the sheet editor
	 */
	public SheetEditor freeze(int row, int col) {
		if (row < 0) {
			row = 0;
		}
		if (col < 0) {
			col = 0;
		}
		sheet.createFreezePane(col, row, col, row);
		return this;
	}

	/**
	 * Gets the last row num.
	 *
	 * @return the last row num
	 */
	public int getLastRowNum() {
		return ExcelUtils.getLastRowNum(sheet);
	}

	/**
	 * Display gridlines.
	 *
	 * @param show the show
	 * @return the sheet editor
	 */
	public SheetEditor displayGridlines(boolean show) {
		sheet.setDisplayGridlines(show);
		return this;
	}

	/**
	 * Prints the gridlines.
	 *
	 * @param newPrintGridlines the new print gridlines
	 * @return the sheet editor
	 */
	public SheetEditor printGridlines(boolean newPrintGridlines) {
		sheet.setPrintGridlines(newPrintGridlines);
		return this;
	}

	/**
	 * Fit to page.
	 *
	 * @param isFit the is fit
	 * @return the sheet editor
	 */
	public SheetEditor fitToPage(boolean isFit) {
		sheet.setFitToPage(isFit);
		return this;
	}

	/**
	 * Horizontally center.
	 *
	 * @param isCenter the is center
	 * @return the sheet editor
	 */
	public SheetEditor horizontallyCenter(boolean isCenter) {
		sheet.setHorizontallyCenter(isCenter);
		return this;
	}

	/**
	 * Password.
	 *
	 * @param pw the pw
	 * @return the sheet editor
	 */
	public SheetEditor password(String pw) {
		sheet.protectSheet(pw);
		return this;
	}

	/**
	 * Prints the setup.
	 *
	 * @param printSetup the print setup
	 * @return the sheet editor
	 */
	public SheetEditor printSetup(PrintOutSetup printSetup) {
		printSetup.setup(sheet.getPrintSetup());
		return this;
	}

	/**
	 * Autobreaks.
	 *
	 * @param b the b
	 * @return the sheet editor
	 */
	public SheetEditor autobreaks(boolean b) {
		sheet.setAutobreaks(b);
		return this;
	}

	/**
	 * Adds the cell value listener.
	 *
	 * @param listener the listener
	 */
	public void addCellValueListener(CellValueListener listener) {
		ctx.getListenerList(sheetIndex).add(listener);
	}

	/**
	 * Removes the cell value listener.
	 *
	 * @param listener the listener
	 */
	public void removeCellValueListener(CellValueListener listener) {
		ctx.getListenerList(sheetIndex).remove(listener);
	}

	/**
	 * To sheet.
	 *
	 * @return the sheet
	 */
	public Sheet toSheet() {
		return sheet;
	}

}
