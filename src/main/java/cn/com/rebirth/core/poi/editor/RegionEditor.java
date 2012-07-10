/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core RegionEditor.java 2012-2-3 11:19:42 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.editor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.com.rebirth.core.poi.ExcelContext;

/**
 * The Class RegionEditor.
 *
 * @author l.xue.nong
 */
public class RegionEditor extends AbstractRegionEditor<RegionEditor> {

	/** The cell range. */
	private CellRangeAddress cellRange;

	/**
	 * Instantiates a new region editor.
	 *
	 * @param beginRow the begin row
	 * @param beginCol the begin col
	 * @param endRow the end row
	 * @param endCol the end col
	 * @param context the context
	 */
	public RegionEditor(int beginRow, int beginCol, int endRow, int endCol, ExcelContext context) {
		super(context);
		cellRange = new CellRangeAddress(beginRow, endRow, beginCol, endCol);
	}

	/**
	 * Image.
	 *
	 * @param imgPath the img path
	 * @return the region editor
	 */
	public RegionEditor image(String imgPath) {
		ByteArrayOutputStream byteArrayOut = null;
		BufferedImage bufferImg = null;
		try {
			if (imgPath.startsWith("http")) {
				URL url = new URL(imgPath);
				URLConnection conn = url.openConnection();
				bufferImg = ImageIO.read(conn.getInputStream());
			} else {
				bufferImg = ImageIO.read(new File(imgPath));
			}
			ClientAnchor anchor = workBook.getCreationHelper().createClientAnchor();
			anchor.setDx1(10);
			anchor.setDy1(10);
			anchor.setDx2(0);
			anchor.setDy2(0);
			anchor.setCol1(cellRange.getFirstColumn());
			anchor.setRow1(cellRange.getFirstRow());
			anchor.setCol2((cellRange.getLastColumn() + 1));
			anchor.setRow2(cellRange.getLastRow() + 1);
			anchor.setAnchorType(3);
			Drawing patr = ctx.getDrawing(workingSheet);
			byteArrayOut = new ByteArrayOutputStream();
			ImageIO.write(bufferImg, "JPEG", byteArrayOut);
			int imageIndex = workBook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
			patr.createPicture(anchor, imageIndex);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				byteArrayOut.close();
			} catch (Exception e) {
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newCellEditor()
	 */
	@Override
	protected CellEditor newCellEditor() {
		CellEditor cellEditor = new CellEditor(ctx);
		for (int i = cellRange.getFirstRow(); i <= cellRange.getLastRow(); i++) {
			for (int j = cellRange.getFirstColumn(); j <= cellRange.getLastColumn(); j++) {
				cellEditor.add(i, j);
			}
		}
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newTopCellEditor()
	 */
	@Override
	protected CellEditor newTopCellEditor() {
		CellEditor cellEditorTop = new CellEditor(ctx);
		for (int i = cellRange.getFirstColumn(); i <= cellRange.getLastColumn(); i++) {
			cellEditorTop.add(cellRange.getFirstRow(), i);
		}
		return cellEditorTop;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newBottomCellEditor()
	 */
	@Override
	protected CellEditor newBottomCellEditor() {
		CellEditor cellEditorBottom = new CellEditor(ctx);
		for (int i = cellRange.getFirstColumn(); i <= cellRange.getLastColumn(); i++) {
			cellEditorBottom.add(cellRange.getLastRow(), i);
		}
		return cellEditorBottom;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newLeftCellEditor()
	 */
	@Override
	protected CellEditor newLeftCellEditor() {
		CellEditor cellEditorLeft = new CellEditor(ctx);
		for (int i = cellRange.getFirstRow(); i <= cellRange.getLastRow(); i++) {
			cellEditorLeft.add(i, cellRange.getFirstColumn());
		}
		return cellEditorLeft;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#newRightCellEditor()
	 */
	@Override
	protected CellEditor newRightCellEditor() {
		CellEditor cellEditorRight = new CellEditor(ctx);
		for (int i = cellRange.getFirstRow(); i <= cellRange.getLastRow(); i++) {
			cellEditorRight.add(i, cellRange.getLastColumn());
		}
		return cellEditorRight;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.core.poi.editor.AbstractRegionEditor#getCellRange()
	 */
	@Override
	protected CellRangeAddress getCellRange() {
		return cellRange;
	}
}
