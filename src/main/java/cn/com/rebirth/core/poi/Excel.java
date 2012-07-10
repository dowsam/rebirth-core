/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core Excel.java 2012-2-3 11:23:56 l.xue.nong$$
 */
package cn.com.rebirth.core.poi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.poi.editor.CellEditor;
import cn.com.rebirth.core.poi.editor.ColumnEditor;
import cn.com.rebirth.core.poi.editor.RegionEditor;
import cn.com.rebirth.core.poi.editor.RowEditor;
import cn.com.rebirth.core.poi.editor.SheetEditor;
import cn.com.rebirth.core.poi.utils.ExcelUtils;

/**
 * The Class Excel.
 *
 * @author l.xue.nong
 */
public class Excel {

	/** The ctx. */
	private ExcelContext ctx;

	/** The excel name. */
	private String excelName;

	/**
	 * Instantiates a new excel.
	 *
	 * @param excelName the excel path or name
	 */
	public Excel(String excelName) {
		this(excelName, new DefaultExcelStyle());
	}

	/**
	 * Instantiates a new excel.
	 *
	 * @param excelName the excel name
	 * @param defaultStyle the default style
	 */
	public Excel(String excelName, DefaultExcelStyle defaultStyle) {
		super();
		if (StringUtils.isBlank(excelName)) {
			throw new IllegalArgumentException("Error 'excelName' not be null");
		}
		if (defaultStyle == null) {
			throw new IllegalArgumentException("Error 'DefaultExcelStyle' not be null");
		}
		Workbook workBook = buildWorkBook(excelName);
		this.excelName = excelName;
		ctx = new ExcelContext(this, workBook);
		ctx.setDefaultStyle(defaultStyle);
		setWorkingSheet(workBook.getNumberOfSheets());
		CellStyle tempCellStyle = workBook.createCellStyle();
		ctx.setTempCellStyle(tempCellStyle);
		Font tempFont = workBook.createFont();
		ctx.setTempFont(tempFont);
		//设置默认样式
		Cell cell = ExcelUtils.getCell(ctx.getWorkingSheet(), 0, 0);
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFillForegroundColor(defaultStyle.getBackgroundColor().getIndex());
		cellStyle.setFillPattern(defaultStyle.getFillPattern().getFillPattern());
		cellStyle.setAlignment(defaultStyle.getAlign().getAlignment());
		cellStyle.setVerticalAlignment(defaultStyle.getvAlign().getAlignment());
		//设置边框样式
		cellStyle.setBorderBottom(defaultStyle.getBorderStyle().getBorderType());
		cellStyle.setBorderLeft(defaultStyle.getBorderStyle().getBorderType());
		cellStyle.setBorderRight(defaultStyle.getBorderStyle().getBorderType());
		cellStyle.setBorderTop(defaultStyle.getBorderStyle().getBorderType());
		cellStyle.setBottomBorderColor(defaultStyle.getBorderColor().getIndex());
		cellStyle.setTopBorderColor(defaultStyle.getBorderColor().getIndex());
		cellStyle.setLeftBorderColor(defaultStyle.getBorderColor().getIndex());
		cellStyle.setRightBorderColor(defaultStyle.getBorderColor().getIndex());
		//默认字体
		Font font = workBook.getFontAt(cellStyle.getFontIndex());
		font.setFontHeightInPoints(defaultStyle.getFontSize());
		font.setFontName(defaultStyle.getFontName());
		font.setColor(defaultStyle.getFontColor().getIndex());
	}

	/**
	 * Builds the work book.
	 *
	 * @param excelName the excel name
	 * @return the workbook
	 */
	private Workbook buildWorkBook(String excelName) {
		Workbook workbook = readExcel(excelName);
		if (workbook != null) {
			return workbook;
		}
		String filenameExtension = org.springframework.util.StringUtils.getFilenameExtension(excelName);
		if (StringUtils.isBlank(filenameExtension)) {
			throw new IllegalArgumentException("File Name not to[xls,xlsx] format Office File");
		}
		if ("xls".equalsIgnoreCase(filenameExtension)) {
			workbook = new HSSFWorkbook();
		} else if ("xlsx".equalsIgnoreCase(filenameExtension)) {
			workbook = new XSSFWorkbook();
		}
		if (workbook == null) {
			throw new IllegalArgumentException("Office WorkBook be not null");
		}
		return workbook;
	}

	/**
	 * Read excel.
	 *
	 * @param excelPath the excel path
	 * @return the workbook
	 */
	private Workbook readExcel(String excelPath) {
		try {
			return WorkbookFactory.create(new FileInputStream(excelPath));
		} catch (Exception e) {
			try {
				return WorkbookFactory.create(getClass().getResourceAsStream(excelPath));
			} catch (Exception e2) {
				try {
					//调用者的相对路径
					InputStream stream = null;
					StackTraceElement[] st = new Throwable().getStackTrace();
					for (int i = 2; i < st.length; i++) {
						stream = Class.forName(st[i].getClassName()).getResourceAsStream(excelPath);
						if (stream != null) {
							return WorkbookFactory.create(stream);
						}
					}
				} catch (Exception e3) {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Save excel.
	 *
	 * @param excelPath the excel path
	 * @return true, if successful
	 */
	public boolean saveExcel(String excelPath) {
		BufferedOutputStream fileOut;
		try {
			File file = new File(excelPath);
			if (file.isDirectory()) {
				String tempExcelName = org.springframework.util.StringUtils.getFilename(this.excelName);
				excelPath = excelPath.endsWith(File.separator) ? excelPath + tempExcelName : excelPath + File.separator
						+ tempExcelName;
				file = new File(excelPath);
			}
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					FileUtils.forceMkdir(file.getParentFile());
				}
			}
			fileOut = new BufferedOutputStream(new FileOutputStream(excelPath));
			return saveExcel(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Save excel.
	 *
	 * @param response the response
	 */
	public void saveExcel(HttpServletResponse response) {
		response.setContentType(ServletUtils.EXCEL_TYPE);
		ServletUtils.setFileDownloadHeader(response, org.springframework.util.StringUtils.getFilename(this.excelName));
		try {
			saveExcel(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save excel.
	 *
	 * @param fileOut the file out
	 * @return true, if successful
	 */
	public boolean saveExcel(OutputStream fileOut) {
		boolean result = false;
		try {
			ctx.getWorkBook().write(fileOut);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				fileOut.flush();
				fileOut.close();
			} catch (Exception e) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Sets the working sheet.
	 *
	 * @param index the index
	 * @return the sheet editor
	 */
	public SheetEditor setWorkingSheet(int index) {
		if (index < 0) {
			index = 0;
		}
		ctx.setWorkingSheet(ExcelUtils.getSheet(ctx.getWorkBook(), index));
		return this.sheet(index);
	}

	/**
	 * Cell.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell editor
	 */
	public CellEditor cell(int row, int col) {
		CellEditor cellEditor = new CellEditor(row, col, ctx);
		return cellEditor;
	}

	/**
	 * Row.
	 *
	 * @param row the row
	 * @return the row editor
	 */
	public RowEditor row(int row) {
		return new RowEditor(row, ctx);
	}

	/**
	 * Row.
	 *
	 * @param row the row
	 * @param startCol the start col
	 * @return the row editor
	 */
	public RowEditor row(int row, int startCol) {
		return new RowEditor(row, startCol, ctx);
	}

	/**
	 * Row.
	 *
	 * @return the row editor
	 */
	public RowEditor row() {
		int rowNum = ExcelUtils.getLastRowNum(ctx.getWorkingSheet());
		if (!checkEmptyRow(rowNum)) {
			rowNum++;
		}
		return new RowEditor(rowNum, ctx);
	}

	/**
	 * Check empty row.
	 *
	 * @param rowNum the row num
	 * @return true, if successful
	 */
	private boolean checkEmptyRow(int rowNum) {
		Row row = ctx.getWorkingSheet().getRow(rowNum);
		int lastCell = row != null ? row.getLastCellNum() : 2;
		return (lastCell == 1 || lastCell == -1);
	}

	/**
	 * Column.
	 *
	 * @param col the col
	 * @return the column editor
	 */
	public ColumnEditor column(int col) {
		ColumnEditor columnEditor = new ColumnEditor(col, ctx);
		return columnEditor;
	}

	/**
	 * Column.
	 *
	 * @param col the col
	 * @param startRow the start row
	 * @return the column editor
	 */
	public ColumnEditor column(int col, int startRow) {
		ColumnEditor columnEditor = new ColumnEditor(col, startRow, ctx);
		return columnEditor;
	}

	/**
	 * Region.
	 *
	 * @param beginRow the begin row
	 * @param beginCol the begin col
	 * @param endRow the end row
	 * @param endCol the end col
	 * @return the region editor
	 */
	public RegionEditor region(int beginRow, int beginCol, int endRow, int endCol) {
		RegionEditor regionEditor = new RegionEditor(beginRow, beginCol, endRow, endCol, ctx);
		return regionEditor;
	}

	/**
	 * Sheet.
	 *
	 * @param index the index
	 * @return the sheet editor
	 */
	public SheetEditor sheet(int index) {
		if (index < 0) {
			index = 0;
		}
		SheetEditor sheetEditor = new SheetEditor(ExcelUtils.getSheet(ctx.getWorkBook(), index), ctx);
		return sheetEditor;
	}

	/**
	 * Sheet.
	 *
	 * @return the sheet editor
	 */
	public SheetEditor sheet() {
		return this.sheet(ctx.getWorkingSheetIndex());
	}

	/**
	 * Gets the work book.
	 *
	 * @return the work book
	 */
	public Workbook getWorkBook() {
		return ctx.getWorkBook();
	}

	/**
	 * Gets the working sheet index.
	 *
	 * @return the working sheet index
	 */
	public int getWorkingSheetIndex() {
		return ctx.getWorkingSheetIndex();
	}
}
