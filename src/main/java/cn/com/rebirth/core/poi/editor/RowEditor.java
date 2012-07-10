/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core RowEditor.java 2012-2-3 11:20:01 l.xue.nong$$
 */

package cn.com.rebirth.core.poi.editor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.com.rebirth.core.poi.ExcelContext;

/**
 * The Class RowEditor.
 *
 * @author l.xue.nong
 */
public class RowEditor extends AbstractRegionEditor<RowEditor> {

	/** The row. */
	private Row row;

	/** The start col. */
	private int startCol = 0;

	/**
	 * Instantiates a new row editor.
	 *
	 * @param row the row
	 * @param startCol the start col
	 * @param context the context
	 */
	public RowEditor(int row, int startCol, ExcelContext context) {
		super(context);
		this.row = this.getRow(row);
		this.startCol = startCol;
	}

	/**
	 * Instantiates a new row editor.
	 *
	 * @param row the row
	 * @param context the context
	 */
	public RowEditor(int row, ExcelContext context) {
		this(row, 0, context);
	}

	/**
	 * Value.
	 *
	 * @param rowData the row data
	 * @return the row editor
	 */
	public RowEditor value(Object[] rowData) {
		value(rowData, startCol);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param rowData the row data
	 * @param startCol the start col
	 * @return the row editor
	 */
	public RowEditor value(Object[] rowData, int startCol) {
		if (startCol < 0) {
			startCol = 0;
		}
		insertData(rowData, row, startCol, true);
		return this;
	}

	/**
	 * Insert.
	 *
	 * @param rowData the row data
	 * @return the row editor
	 */
	public RowEditor insert(Object[] rowData) {
		return insert(rowData, startCol);
	}

	/**
	 * Insert.
	 *
	 * @param rowData the row data
	 * @param startCol the start col
	 * @return the row editor
	 */
	public RowEditor insert(Object[] rowData, int startCol) {
		if (startCol < 0) {
			startCol = 0;
		}
		insertData(rowData, row, startCol, false);
		return this;
	}

	/**
	 * Append.
	 *
	 * @param rowData the row data
	 * @return the row editor
	 */
	public RowEditor append(Object[] rowData) {
		insertData(rowData, row, row.getLastCellNum(), true);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#height(float)
	 */
	@Override
	public RowEditor height(float h) {
		row.setHeightInPoints(h);
		return this;
	}

	/**
	 * Cell.
	 *
	 * @param col the col
	 * @param cols the cols
	 * @return the cell editor
	 */
	public CellEditor cell(int col, int... cols) {
		CellEditor cellEditor = new CellEditor(row.getRowNum(), col, ctx);
		for (int c : cols) {
			cellEditor.add(row.getRowNum(), c);
		}
		return cellEditor;
	}

	/**
	 * To row.
	 *
	 * @return the row
	 */
	public Row toRow() {
		return row;
	}

	/**
	 * Insert data.
	 *
	 * @param rowData the row data
	 * @param row the row
	 * @param startCol the start col
	 * @param overwrite the overwrite
	 */
	private void insertData(Object[] rowData, Row row, int startCol, boolean overwrite) {
		if (!overwrite) {
			workingSheet.shiftRows(row.getRowNum(), workingSheet.getLastRowNum(), 1, true, false);
		}
		short i = 0;
		for (Object obj : rowData) {
			CellEditor cellEditor = new CellEditor(row.getRowNum(), startCol + i, ctx);
			cellEditor.value(obj);
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newCellEditor()
	 */
	@Override
	protected CellEditor newCellEditor() {
		CellEditor cellEditor = new CellEditor(ctx);
		short minColIx = 0;
		short maxColIx = 0;
		minColIx = (short) startCol;//row.getFirstCellNum();
		maxColIx = row.getLastCellNum();
		for (int i = minColIx; i < maxColIx; i++) {
			cellEditor.add(row.getRowNum(), i);
		}
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newTopCellEditor()
	 */
	@Override
	protected CellEditor newTopCellEditor() {
		return newCellEditor();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newBottomCellEditor()
	 */
	@Override
	protected CellEditor newBottomCellEditor() {
		return newCellEditor();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newLeftCellEditor()
	 */
	@Override
	protected CellEditor newLeftCellEditor() {
		CellEditor cellEditor = new CellEditor(ctx);
		cellEditor.add(row.getRowNum(), startCol);
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newRightCellEditor()
	 */
	@Override
	protected CellEditor newRightCellEditor() {
		CellEditor cellEditor = new CellEditor(ctx);
		cellEditor.add(row.getRowNum(), row.getLastCellNum());
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#getCellRange()
	 */
	@Override
	protected CellRangeAddress getCellRange() {
		return new CellRangeAddress(row.getRowNum(), row.getRowNum(), startCol, row.getLastCellNum() - 1);
	}

	/**
	 * Gets the row.
	 *
	 * @return the row
	 */
	protected Row getRow() {
		return row;

	}

}
