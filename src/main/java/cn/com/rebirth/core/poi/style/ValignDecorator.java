/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core ValignDecorator.java 2012-2-3 11:23:26 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The Enum ValignDecorator.
 *
 * @author l.xue.nong
 */
public enum ValignDecorator {

	/** The TOP. */
	TOP(CellStyle.VERTICAL_TOP),
	/** The CENTER. */
	CENTER(CellStyle.VERTICAL_CENTER),
	/** The BOTTOM. */
	BOTTOM(CellStyle.VERTICAL_BOTTOM),
	/** The JUSTIFY. */
	JUSTIFY(CellStyle.VERTICAL_JUSTIFY);

	/** The alignment. */
	private short alignment;

	/**
	 * Instantiates a new valign decorator.
	 *
	 * @param alignment the alignment
	 */
	private ValignDecorator(short alignment) {
		this.alignment = alignment;
	}

	/**
	 * Gets the alignment.
	 *
	 * @return the alignment
	 */
	public short getAlignment() {
		return alignment;
	}
}
