/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core CellEditor.java 2012-2-3 11:18:18 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import cn.com.rebirth.core.poi.ExcelContext;
import cn.com.rebirth.core.poi.editor.listener.CellValueListener;
import cn.com.rebirth.core.poi.style.AlignDecorator;
import cn.com.rebirth.core.poi.style.BorderStyleDecorator;
import cn.com.rebirth.core.poi.style.ColorDecorator;
import cn.com.rebirth.core.poi.style.FillPatternDecorator;
import cn.com.rebirth.core.poi.style.ValignDecorator;
import cn.com.rebirth.core.poi.style.font.FontDecorator;

/**
 * The Class CellEditor.
 *
 * @author l.xue.nong
 */
public class CellEditor extends AbstractEditor {

	/** The working cell. */
	private List<Cell> workingCell = new ArrayList<Cell>(2);

	/**
	 * Instantiates a new cell editor.
	 *
	 * @param context the context
	 */
	protected CellEditor(ExcelContext context) {
		super(context);
	}

	/**
	 * Instantiates a new cell editor.
	 *
	 * @param row the row
	 * @param col the col
	 * @param context the context
	 */
	public CellEditor(int row, int col, ExcelContext context) {
		this(context);
		this.add(row, col);
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the cell editor
	 */
	public CellEditor value(Object value) {
		for (Cell cell : workingCell) {
			this.setCellValue(cell, value, null);
		}
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @param pattern the pattern
	 * @return the cell editor
	 */
	public CellEditor value(Date value, String pattern) {
		for (Cell cell : workingCell) {
			this.setCellValue(cell, value, pattern);
		}
		return this;
	}

	/**
	 * Value.
	 *
	 * @return the object
	 */
	public Object value() {
		if (workingCell.size() == 1) {
			return this.getCellValue(workingCell.get(0));
		} else {
			Object[] vals = new Object[workingCell.size()];
			int i = 0;
			for (Cell cell : workingCell) {
				vals[i++] = this.getCellValue(cell);
			}
			return vals;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Cell cell : workingCell) {
			str.append(cell.toString()).append("\t");
		}
		if (str.length() > 0) {
			str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * Adds the.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell editor
	 */
	protected CellEditor add(int row, int col) {
		Cell cell = getCell(row, col);
		workingCell.add(cell);
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell editor
	 */
	protected CellEditor add(RowEditor row, int col) {
		Cell cell = getCell(row.getRow(), col);
		workingCell.add(cell);
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param row the row
	 * @param col the col
	 * @return the cell editor
	 */
	protected CellEditor add(int row, ColumnEditor col) {
		return add(row, col.getCol());
	}

	/**
	 * Adds the.
	 *
	 * @param cell the cell
	 * @return the cell editor
	 */
	protected CellEditor add(CellEditor cell) {
		workingCell.addAll(cell.getWorkingCell());
		return this;
	}

	/**
	 * Border.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the cell editor
	 */
	public CellEditor border(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setBorderBottom(borderStyle.getBorderType());
			tempCellStyle.setBorderTop(borderStyle.getBorderType());
			tempCellStyle.setBorderLeft(borderStyle.getBorderType());
			tempCellStyle.setBorderRight(borderStyle.getBorderType());
			tempCellStyle.setBottomBorderColor(borderColor.getIndex());
			tempCellStyle.setTopBorderColor(borderColor.getIndex());
			tempCellStyle.setLeftBorderColor(borderColor.getIndex());
			tempCellStyle.setRightBorderColor(borderColor.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Border left.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the cell editor
	 */
	public CellEditor borderLeft(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setBorderLeft(borderStyle.getBorderType());
			tempCellStyle.setLeftBorderColor(borderColor.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Border right.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the cell editor
	 */
	public CellEditor borderRight(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setBorderRight(borderStyle.getBorderType());
			tempCellStyle.setRightBorderColor(borderColor.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Border top.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the cell editor
	 */
	public CellEditor borderTop(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setBorderTop(borderStyle.getBorderType());
			tempCellStyle.setTopBorderColor(borderColor.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Border bottom.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the cell editor
	 */
	public CellEditor borderBottom(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setBorderBottom(borderStyle.getBorderType());
			tempCellStyle.setBottomBorderColor(borderColor.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Font.
	 *
	 * @param fontEditor the font editor
	 * @return the cell editor
	 */
	public CellEditor font(FontOutEditor fontEditor) {
		Map<Integer, Font> fontCache = ctx.getFontCache();
		for (Cell cell : workingCell) {
			//System.out.println("===============================================");
			//System.out.println("设置单元格字体："+(cell.getCellType()== 1 ? cell.getRichStringCellValue():cell.getNumericCellValue()));
			Font font = workBook.getFontAt(cell.getCellStyle().getFontIndex());
			copyFont(font, tempFont);
			fontEditor.updateFont(new FontDecorator(tempFont));
			int fontHash = tempFont.hashCode() - tempFont.getIndex();
			//System.out.println("修改字体后，计算Hash:"+fontHash);
			//System.out.println("设置的字体:"+tempFont);
			tempCellStyle.cloneStyleFrom(cell.getCellStyle());
			if (fontCache.containsKey(fontHash)) {
				//System.out.println("缓存里找到字体");
				//System.out.println("找到的字体:"+fontCache.get(fontHash)+", fontIndex="+fontCache.get(fontHash).getIndex());
				tempCellStyle.setFont(fontCache.get(fontHash));
			} else {
				//System.out.println("没找到字体，新建一个");
				Font newFont = workBook.createFont();
				copyFont(tempFont, newFont);
				//System.out.println("设置的字体:"+newFont.toString()+", fontIndex="+newFont.getIndex());
				tempCellStyle.setFont(newFont);
				int newFontHash = newFont.hashCode() - newFont.getIndex();
				fontCache.put(newFontHash, newFont);
			}
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Bg color.
	 *
	 * @param bg the bg
	 * @return the cell editor
	 */
	public CellEditor bgColor(ColorDecorator bg) {
		return bgColor(bg, FillPatternDecorator.SOLID_FOREGROUND);
	}

	/**
	 * Bg color.
	 *
	 * @param bg the bg
	 * @param fillPattern the fill pattern
	 * @return the cell editor
	 */
	public CellEditor bgColor(ColorDecorator bg, FillPatternDecorator fillPattern) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setFillPattern(fillPattern.getFillPattern());
			tempCellStyle.setFillForegroundColor(bg.getIndex());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Align.
	 *
	 * @param align the align
	 * @return the cell editor
	 */
	public CellEditor align(AlignDecorator align) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setAlignment(align.getAlignment());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * V align.
	 *
	 * @param align the align
	 * @return the cell editor
	 */
	public CellEditor vAlign(ValignDecorator align) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setVerticalAlignment(align.getAlignment());
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Warp text.
	 *
	 * @param autoWarp the auto warp
	 * @return the cell editor
	 */
	public CellEditor warpText(boolean autoWarp) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setWrapText(autoWarp);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Hidden.
	 *
	 * @param hidden the hidden
	 * @return the cell editor
	 */
	public CellEditor hidden(boolean hidden) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setHidden(hidden);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Indent.
	 *
	 * @param indent the indent
	 * @return the cell editor
	 */
	public CellEditor indent(int indent) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setIndention((short) indent);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Lock.
	 *
	 * @param locked the locked
	 * @return the cell editor
	 */
	public CellEditor lock(boolean locked) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setLocked(locked);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Rotate.
	 *
	 * @param rotation the rotation
	 * @return the cell editor
	 */
	public CellEditor rotate(int rotation) {
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			tempCellStyle.setRotation((short) rotation);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Comment.
	 *
	 * @param content the content
	 * @return the cell editor
	 */
	public CellEditor comment(String content) {
		Drawing patr = ctx.getDrawing(workingSheet);
		for (Cell cell : workingCell) {
			ClientAnchor anchor = workBook.getCreationHelper().createClientAnchor();
			anchor.setDx1(0);
			anchor.setDy1(0);
			anchor.setDx2(0);
			anchor.setDy2(0);
			anchor.setCol1(cell.getColumnIndex());
			anchor.setRow1(cell.getRowIndex());
			anchor.setCol2(cell.getColumnIndex() + 3);
			anchor.setRow2(cell.getRowIndex() + 4);
			Comment comment = patr.createCellComment(anchor);
			comment.setString(workBook.getCreationHelper().createRichTextString(content));
			cell.setCellComment(comment);
		}
		return this;
	}

	/**
	 * Style.
	 *
	 * @param style the style
	 * @return the cell editor
	 */
	public CellEditor style(CellStyle style) {
		for (Cell cell : workingCell) {
			cell.setCellStyle(style);
		}
		return this;
	}

	/**
	 * Data format.
	 *
	 * @param format the format
	 * @return the cell editor
	 */
	public CellEditor dataFormat(String format) {
		short index = (short) BuiltinFormats.getBuiltinFormat(format);
		for (Cell cell : workingCell) {
			CellStyle style = cell.getCellStyle();
			tempCellStyle.cloneStyleFrom(style);
			if (index == -1) {
				DataFormat dataFormat = ctx.getWorkBook().createDataFormat();
				index = dataFormat.getFormat(format);
			}
			tempCellStyle.setDataFormat(index);
			updateCellStyle(cell);
		}
		return this;
	}

	/**
	 * Width.
	 *
	 * @param width the width
	 * @return the cell editor
	 */
	public CellEditor width(int width) {
		for (Cell cell : workingCell) {
			workingSheet.setColumnWidth(cell.getColumnIndex(), width);
		}
		return this;
	}

	/**
	 * Adds the width.
	 *
	 * @param width the width
	 * @return the cell editor
	 */
	public CellEditor addWidth(int width) {
		for (Cell cell : workingCell) {
			int w = workingSheet.getColumnWidth(cell.getColumnIndex());
			workingSheet.setColumnWidth(cell.getColumnIndex(), width + w);
		}
		return this;
	}

	/**
	 * Height.
	 *
	 * @param height the height
	 * @return the cell editor
	 */
	public CellEditor height(float height) {
		for (Cell cell : workingCell) {
			Row row = getRow(cell.getRowIndex());
			row.setHeightInPoints(height);
		}
		return this;
	}

	/**
	 * Adds the height.
	 *
	 * @param height the height
	 * @return the cell editor
	 */
	public CellEditor addHeight(float height) {
		for (Cell cell : workingCell) {
			Row row = getRow(cell.getRowIndex());
			float h = row.getHeightInPoints();
			row.setHeightInPoints(height + h);
		}
		return this;
	}

	/**
	 * Row.
	 *
	 * @return the row editor
	 */
	public RowEditor row() {
		return new RowEditor(workingCell.get(0).getRowIndex(), ctx);
	}

	/**
	 * Colunm.
	 *
	 * @return the column editor
	 */
	public ColumnEditor colunm() {
		return new ColumnEditor(workingCell.get(0).getColumnIndex(), ctx);
	}

	/**
	 * Sheet.
	 *
	 * @return the sheet editor
	 */
	public SheetEditor sheet() {
		return new SheetEditor(workingCell.get(0).getSheet(), ctx);
	}

	/**
	 * To cell.
	 *
	 * @return the cell
	 */
	public Cell toCell() {
		if (workingCell.size() > 0) {
			return workingCell.get(0);
		}
		return null;
	}

	/**
	 * Update cell style.
	 *
	 * @param cell the cell
	 */
	private void updateCellStyle(Cell cell) {
		Map<Integer, CellStyle> styleCache = ctx.getStyleCache();
		int tempStyleHash = tempCellStyle.hashCode() - tempCellStyle.getIndex();
		//System.out.println("寻找样式:"+tempStyleHash);
		if (styleCache.containsKey(tempStyleHash)) {
			//System.out.println("在缓存里找到样式");
			cell.setCellStyle(styleCache.get(tempStyleHash));
		} else {
			CellStyle newStyle = workBook.createCellStyle();
			newStyle.cloneStyleFrom(tempCellStyle);
			cell.setCellStyle(newStyle);
			int newStyleHash = newStyle.hashCode() - newStyle.getIndex();
			//System.out.println("新建样式，Hash="+newStyleHash);
			styleCache.put(newStyleHash, newStyle);
		}
	}

	/**
	 * Copy font.
	 *
	 * @param src the src
	 * @param dest the dest
	 */
	private void copyFont(Font src, Font dest) {
		dest.setBoldweight(src.getBoldweight());
		dest.setCharSet(src.getCharSet());
		dest.setColor(src.getColor());
		dest.setFontHeight(src.getFontHeight());
		dest.setFontHeightInPoints(src.getFontHeightInPoints());
		dest.setFontName(src.getFontName());
		dest.setItalic(src.getItalic());
		dest.setStrikeout(src.getStrikeout());
		dest.setTypeOffset(src.getTypeOffset());
		dest.setUnderline(src.getUnderline());
	}

	/**
	 * Sets the cell value.
	 *
	 * @param cell the cell
	 * @param value the value
	 * @param pattern the pattern
	 */
	private void setCellValue(Cell cell, Object value, String pattern) {
		if (value instanceof Double || value instanceof Float || value instanceof Long || value instanceof Integer
				|| value instanceof Short || value instanceof BigDecimal) {
			cell.setCellValue(null2Double(value.toString()));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);//这应该在setValue之后
		} else {
			if (value != null && value.toString().startsWith("=")) {
				cell.setCellFormula(value.toString().substring(1));
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
			} else {
				if (value instanceof Date) {//日期
					if (pattern == null || pattern.trim().equals("")) {
						pattern = ctx.getDefaultStyle().getDefaultDatePattern();
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
					cell.setCellValue(ctx.getWorkBook().getCreationHelper()
							.createRichTextString(dateFormat.format(value)));
				} else {
					cell.setCellValue(ctx.getWorkBook().getCreationHelper()
							.createRichTextString(value == null ? "" : value.toString()));
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
			}
		}
		invokeListener(cell, value);
	}

	/**
	 * Gets the cell value.
	 *
	 * @param cell the cell
	 * @return the cell value
	 */
	private Object getCellValue(Cell cell) {
		int cellType = cell.getCellType();
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().toString();
		default:
			return "";
		}
	}

	/**
	 * Invoke listener.
	 *
	 * @param cell the cell
	 * @param value the value
	 */
	private void invokeListener(Cell cell, Object value) {
		int sheetIndex = workBook.getSheetIndex(cell.getSheet());
		List<CellValueListener> listeners = ctx.getListenerList(sheetIndex);
		for (CellValueListener l : listeners) {
			l.onValueChange(this, value, cell.getRowIndex(), cell.getColumnIndex(), sheetIndex, ctx.getExcel());
		}
	}

	/**
	 * Gets the working cell.
	 *
	 * @return the working cell
	 */
	protected List<Cell> getWorkingCell() {
		return workingCell;
	}

	/**
	 * Null2 double.
	 *
	 * @param s the s
	 * @return the double
	 */
	private double null2Double(Object s) {
		double v = 0;
		if (s != null) {
			try {
				v = Double.parseDouble(s.toString());
			} catch (Exception e) {
			}
		}
		return v;
	}

}
