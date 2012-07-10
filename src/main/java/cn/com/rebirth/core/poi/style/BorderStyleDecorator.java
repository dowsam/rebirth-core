/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core BorderStyleDecorator.java 2012-2-3 11:22:33 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The Enum BorderStyleDecorator.
 *
 * @author l.xue.nong
 */
public enum BorderStyleDecorator {

	/** 无边框. */
	NONE(CellStyle.BORDER_NONE),

	/** 细实线. */
	THIN(CellStyle.BORDER_THIN),

	/** 粗实线. */
	MEDIUM(CellStyle.BORDER_MEDIUM),

	/** 最粗的实线. */
	THICK(CellStyle.BORDER_THICK),

	/** 细虚线. */
	DASHED(CellStyle.BORDER_DASHED),

	/** 细点线. */
	HAIR(CellStyle.BORDER_HAIR),

	/** 双实线. */
	DOUBLE(CellStyle.BORDER_DOUBLE),

	/** 细密点线. */
	DOTTED(CellStyle.BORDER_DOTTED),

	/** 粗虚线. */
	MEDIUM_DASHED(CellStyle.BORDER_MEDIUM_DASHED),

	/** 虚线. */
	DASH_DOT(CellStyle.BORDER_DASH_DOT),

	/** 虚线-点，粗线. */
	MEDIUM_DASH_DOT(CellStyle.BORDER_MEDIUM_DASH_DOT),

	/** 虚线-点-点，细线. */
	DASH_DOT_DOT(CellStyle.BORDER_DASH_DOT_DOT),

	/** 虚线-点-点，粗线. */
	MEDIUM_DASH_DOT_DOT(CellStyle.BORDER_MEDIUM_DASH_DOT_DOT),

	/** 虚线-点，倾斜的粗线. */
	SLANTED_DASH_DOT(CellStyle.BORDER_SLANTED_DASH_DOT);

	/** The border type. */
	private short borderType;

	/**
	 * Instantiates a new border style decorator.
	 *
	 * @param borderType the border type
	 */
	private BorderStyleDecorator(short borderType) {
		this.borderType = borderType;
	}

	/**
	 * Gets the border type.
	 *
	 * @return the border type
	 */
	public short getBorderType() {
		return borderType;
	}
}
