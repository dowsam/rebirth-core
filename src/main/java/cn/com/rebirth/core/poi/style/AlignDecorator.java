/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AlignDecorator.java 2012-2-3 11:22:21 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The Enum AlignDecorator.
 *
 * @author l.xue.nong
 */
public enum AlignDecorator {

	/** The GENERAL. */
	GENERAL(CellStyle.ALIGN_GENERAL),

	/** The LEFT. */
	LEFT(CellStyle.ALIGN_LEFT),

	/** The CENTER. */
	CENTER(CellStyle.ALIGN_CENTER),

	/** The RIGHT. */
	RIGHT(CellStyle.ALIGN_RIGHT),

	/** The FILL. */
	FILL(CellStyle.ALIGN_FILL),

	/** The JUSTIFY. */
	JUSTIFY(CellStyle.ALIGN_JUSTIFY),

	/** The CENTE r_ selection. */
	CENTER_SELECTION(CellStyle.ALIGN_CENTER_SELECTION);

	/** The alignment. */
	private short alignment;

	/**
	 * Instantiates a new align decorator.
	 *
	 * @param alignment the alignment
	 */
	private AlignDecorator(short alignment) {
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
