/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core ColumnEditor.java 2012-2-3 11:19:08 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.com.rebirth.core.poi.ExcelContext;
import cn.com.rebirth.core.poi.utils.ExcelUtils;

/**
 * The Class ColumnEditor.
 *
 * @author l.xue.nong
 */
public class ColumnEditor extends AbstractRegionEditor<ColumnEditor> {

	/** The col. */
	private int col = 0;

	/** The start row. */
	private int startRow = 0;

	/**
	 * Instantiates a new column editor.
	 *
	 * @param col the col
	 * @param startRow the start row
	 * @param context the context
	 */
	public ColumnEditor(int col, int startRow, ExcelContext context) {
		super(context);
		this.col = col;
		this.startRow = startRow;
	}

	/**
	 * Instantiates a new column editor.
	 *
	 * @param col the col
	 * @param context the context
	 */
	public ColumnEditor(int col, ExcelContext context) {
		this(col, 0, context);
	}

	/**
	 * Value.
	 *
	 * @param rowData the row data
	 * @return the column editor
	 */
	public ColumnEditor value(Object[] rowData) {
		value(rowData, startRow);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param rowData the row data
	 * @param startRow the start row
	 * @return the column editor
	 */
	public ColumnEditor value(Object[] rowData, int startRow) {
		if (startRow < 0) {
			startRow = 0;
		}
		insertData(rowData, col, startRow);
		return this;
	}

	/**
	 * Auto width.
	 */
	public void autoWidth() {
		workingSheet.autoSizeColumn((short) col, false);
		workingSheet.setColumnWidth(col, workingSheet.getColumnWidth(col) + 1000);
	}

	/**
	 * Cell.
	 *
	 * @param row the row
	 * @param rows the rows
	 * @return the cell editor
	 */
	public CellEditor cell(int row, int... rows) {
		CellEditor cellEditor = new CellEditor(row, col, ctx);
		for (int r : rows) {
			cellEditor.add(r, col);
		}
		return cellEditor;
	}

	/**
	 * Insert data.
	 *
	 * @param rowData the row data
	 * @param col the col
	 * @param startRow the start row
	 */
	private void insertData(Object[] rowData, int col, int startRow) {
		short i = 0;
		for (Object obj : rowData) {
			CellEditor cellEditor = new CellEditor(startRow + i, col, ctx);
			cellEditor.value(obj);
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newBottomCellEditor()
	 */
	@Override
	protected CellEditor newBottomCellEditor() {
		int lastRowNum = ExcelUtils.getLastRowNum(workingSheet);
		CellEditor cellEditor = new CellEditor(ctx);
		cellEditor.add(lastRowNum, col);
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newCellEditor()
	 */
	@Override
	protected CellEditor newCellEditor() {
		CellEditor cellEditor = new CellEditor(ctx);
		int lastRowNum = ExcelUtils.getLastRowNum(workingSheet);
		int firstRowNum = startRow;//workingSheet.getFirstRowNum();
		for (int i = firstRowNum; i <= lastRowNum; i++) {
			Row row = getRow(i);
			cellEditor.add(row.getRowNum(), col);
		}
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newLeftCellEditor()
	 */
	@Override
	protected CellEditor newLeftCellEditor() {
		return newCellEditor();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newRightCellEditor()
	 */
	@Override
	protected CellEditor newRightCellEditor() {
		return newCellEditor();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newTopCellEditor()
	 */
	@Override
	protected CellEditor newTopCellEditor() {
		int firstRowNum = startRow;//workingSheet.getFirstRowNum();
		CellEditor cellEditor = new CellEditor(ctx);
		cellEditor.add(firstRowNum, col);
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#getCellRange()
	 */
	@Override
	protected CellRangeAddress getCellRange() {
		int firstRowNum = startRow;//workingSheet.getFirstRowNum();
		int lastRowNum = ExcelUtils.getLastRowNum(workingSheet);
		return new CellRangeAddress(firstRowNum, lastRowNum, col, col);
	}

	/**
	 * Gets the col.
	 *
	 * @return the col
	 */
	protected int getCol() {
		return col;
	}
}
