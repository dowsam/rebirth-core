/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core FillPatternDecorator.java 2012-2-3 11:22:50 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * The Enum FillPatternDecorator.
 *
 * @author l.xue.nong
 */
public enum FillPatternDecorator {

	/** The N o_ fill. */
	NO_FILL(CellStyle.NO_FILL),

	/** The SOLI d_ foreground. */
	SOLID_FOREGROUND(CellStyle.SOLID_FOREGROUND),

	/** The FIN e_ dots. */
	FINE_DOTS(CellStyle.FINE_DOTS),

	/** The AL t_ bars. */
	ALT_BARS(CellStyle.ALT_BARS),

	/** The SPARS e_ dots. */
	SPARSE_DOTS(CellStyle.SPARSE_DOTS),

	/** The THIC k_ hor z_ bands. */
	THICK_HORZ_BANDS(CellStyle.THICK_HORZ_BANDS),

	/** The THIC k_ ver t_ bands. */
	THICK_VERT_BANDS(CellStyle.THICK_VERT_BANDS),

	/** The THIC k_ backwar d_ diag. */
	THICK_BACKWARD_DIAG(CellStyle.THICK_BACKWARD_DIAG),

	/** The THIC k_ forwar d_ diag. */
	THICK_FORWARD_DIAG(CellStyle.THICK_FORWARD_DIAG),

	/** The BI g_ spots. */
	BIG_SPOTS(CellStyle.BIG_SPOTS),

	/** The BRICKS. */
	BRICKS(CellStyle.BRICKS),

	/** The THI n_ hor z_ bands. */
	THIN_HORZ_BANDS(CellStyle.THIN_HORZ_BANDS),

	/** The THI n_ ver t_ bands. */
	THIN_VERT_BANDS(CellStyle.THIN_VERT_BANDS),

	/** The THI n_ backwar d_ diag. */
	THIN_BACKWARD_DIAG(CellStyle.THIN_BACKWARD_DIAG),

	/** The THI n_ forwar d_ diag. */
	THIN_FORWARD_DIAG(CellStyle.THIN_FORWARD_DIAG),

	/** The SQUARES. */
	SQUARES(CellStyle.SQUARES),

	/** The DIAMONDS. */
	DIAMONDS(CellStyle.DIAMONDS),

	/** The LES s_ dots. */
	LESS_DOTS(CellStyle.LESS_DOTS),

	/** The LEAS t_ dots. */
	LEAST_DOTS(CellStyle.LEAST_DOTS);

	/** The fill pattern. */
	private short fillPattern;

	/**
	 * Instantiates a new fill pattern decorator.
	 *
	 * @param fillPattern the fill pattern
	 */
	private FillPatternDecorator(short fillPattern) {
		this.fillPattern = fillPattern;
	}

	/**
	 * Gets the fill pattern.
	 *
	 * @return the fill pattern
	 */
	public short getFillPattern() {
		return fillPattern;
	}
}
