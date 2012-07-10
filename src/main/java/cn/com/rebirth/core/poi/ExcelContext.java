/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core ExcelContext.java 2012-2-3 11:24:42 l.xue.nong$$
 */
package cn.com.rebirth.core.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.com.rebirth.core.poi.editor.listener.CellValueListener;

/**
 * The Class ExcelContext.
 *
 * @author l.xue.nong
 */
public final class ExcelContext {

	/** The style cache. */
	private Map<Integer, CellStyle> styleCache = new HashMap<Integer, CellStyle>();

	/** The font cache. */
	private Map<Integer, Font> fontCache = new HashMap<Integer, Font>();

	/** The patriarch cache. */
	private Map<Sheet, Drawing> patriarchCache = new HashMap<Sheet, Drawing>();

	/** The work book. */
	private Workbook workBook;

	/** The temp cell style. */
	private CellStyle tempCellStyle;

	/** The temp font. */
	private Font tempFont;

	/** The excel. */
	private Excel excel;

	/** The default style. */
	private DefaultExcelStyle defaultStyle;

	/** The working sheet. */
	private Sheet workingSheet;

	/** The working sheet index. */
	private int workingSheetIndex = 0;

	/** The cell value listener. */
	private Map<Integer, List<CellValueListener>> cellValueListener;

	/**
	 * Instantiates a new excel context.
	 *
	 * @param excel the excel
	 * @param workBook the work book
	 */
	protected ExcelContext(Excel excel, Workbook workBook) {
		this.workBook = workBook;
		short numStyle = workBook.getNumCellStyles();
		for (short i = 0; i < numStyle; i++) {
			CellStyle style = workBook.getCellStyleAt(i);
			if (style != tempCellStyle) {
				styleCache.put(style.hashCode() - style.getIndex(), style);
			}
		}
		short numFont = workBook.getNumberOfFonts();
		for (short i = 0; i < numFont; i++) {
			Font font = workBook.getFontAt(i);
			if (font != tempFont) {
				fontCache.put(font.hashCode() - font.getIndex(), font);
			}
		}
	}

	/**
	 * Gets the work book.
	 *
	 * @return the work book
	 */
	public Workbook getWorkBook() {
		return workBook;
	}

	/**
	 * Sets the work book.
	 *
	 * @param workBook the new work book
	 */
	public void setWorkBook(Workbook workBook) {
		this.workBook = workBook;
	}

	/**
	 * Gets the temp cell style.
	 *
	 * @return the temp cell style
	 */
	public CellStyle getTempCellStyle() {
		return tempCellStyle;
	}

	/**
	 * Sets the temp cell style.
	 *
	 * @param tempCellStyle the new temp cell style
	 */
	public void setTempCellStyle(CellStyle tempCellStyle) {
		this.tempCellStyle = tempCellStyle;
	}

	/**
	 * Gets the temp font.
	 *
	 * @return the temp font
	 */
	public Font getTempFont() {
		return tempFont;
	}

	/**
	 * Sets the temp font.
	 *
	 * @param tempFont the new temp font
	 */
	public void setTempFont(Font tempFont) {
		this.tempFont = tempFont;
	}

	/**
	 * Gets the working sheet.
	 *
	 * @return the working sheet
	 */
	public Sheet getWorkingSheet() {
		return workingSheet;
	}

	/**
	 * Sets the working sheet.
	 *
	 * @param workingSheet the new working sheet
	 */
	public void setWorkingSheet(Sheet workingSheet) {
		this.workingSheet = workingSheet;
		workingSheetIndex = workBook.getSheetIndex(workingSheet);
	}

	/**
	 * Gets the drawing.
	 *
	 * @param sheet the sheet
	 * @return the drawing
	 */
	public Drawing getDrawing(Sheet sheet) {
		Drawing patr = null;
		try {
			patr = patriarchCache.get(sheet);
			if (patr == null) {
				patr = sheet.createDrawingPatriarch();
				patriarchCache.put(sheet, patr);
			}
		} catch (Exception e) {
			patr = sheet.createDrawingPatriarch();
		}
		return patr;
	}

	/**
	 * Sets the default style.
	 *
	 * @param defaultStyle the new default style
	 */
	public void setDefaultStyle(DefaultExcelStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	/**
	 * Gets the default style.
	 *
	 * @return the default style
	 */
	public DefaultExcelStyle getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * Gets the working sheet index.
	 *
	 * @return the working sheet index
	 */
	public int getWorkingSheetIndex() {
		return workingSheetIndex;
	}

	/**
	 * Sets the style cache.
	 *
	 * @param styleCache the style cache
	 */
	public void setStyleCache(Map<Integer, CellStyle> styleCache) {
		this.styleCache = styleCache;
	}

	/**
	 * Gets the style cache.
	 *
	 * @return the style cache
	 */
	public Map<Integer, CellStyle> getStyleCache() {
		return styleCache;
	}

	/**
	 * Sets the font cache.
	 *
	 * @param fontCache the font cache
	 */
	public void setFontCache(Map<Integer, Font> fontCache) {
		this.fontCache = fontCache;
	}

	/**
	 * Gets the font cache.
	 *
	 * @return the font cache
	 */
	public Map<Integer, Font> getFontCache() {
		return fontCache;
	}

	/**
	 * Gets the cell value listener.
	 *
	 * @return the cell value listener
	 */
	private Map<Integer, List<CellValueListener>> getCellValueListener() {
		if (cellValueListener == null) {
			cellValueListener = new HashMap<Integer, List<CellValueListener>>();
		}
		return cellValueListener;
	}

	/**
	 * Gets the listener list.
	 *
	 * @param sheetIndex the sheet index
	 * @return the listener list
	 */
	public List<CellValueListener> getListenerList(int sheetIndex) {
		Map<Integer, List<CellValueListener>> map = getCellValueListener();
		List<CellValueListener> listenerList = map.get(sheetIndex);
		if (listenerList == null) {
			listenerList = new ArrayList<CellValueListener>();
			map.put(sheetIndex, listenerList);
		}
		return listenerList;
	}

	/**
	 * Gets the excel.
	 *
	 * @return the excel
	 */
	public Excel getExcel() {
		return excel;
	}
}
