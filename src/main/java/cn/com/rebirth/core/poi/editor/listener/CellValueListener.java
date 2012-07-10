/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core CellValueListener.java 2012-2-3 11:05:03 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor.listener;

import cn.com.rebirth.core.poi.Excel;
import cn.com.rebirth.core.poi.editor.CellEditor;

/**
 * The listener interface for receiving cellValue events.
 * The class that is interested in processing a cellValue
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCellValueListener<code> method. When
 * the cellValue event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CellValueEvent
 */
public interface CellValueListener {

	/**
	 * On value change.
	 *
	 * @param cell the cell
	 * @param newValue the new value
	 * @param row the row
	 * @param col the col
	 * @param sheetIndex the sheet index
	 * @param excel the excel
	 */
	public void onValueChange(CellEditor cell, Object newValue, int row, int col, int sheetIndex, Excel excel);
}
