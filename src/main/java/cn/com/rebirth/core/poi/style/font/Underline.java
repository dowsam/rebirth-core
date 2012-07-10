/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core Underline.java 2012-2-3 11:22:02 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style.font;

import org.apache.poi.ss.usermodel.Font;

/**
 * The Enum Underline.
 *
 * @author l.xue.nong
 */
public enum Underline {

	/** The NONE. */
	NONE(Font.U_NONE),

	/** 单下横线. */
	SINGLE(Font.U_SINGLE),

	/** 双下横线. */
	DOUBLE(Font.U_DOUBLE),

	/** 会计用单下横线. */
	SINGLE_ACCOUNTING(Font.U_SINGLE_ACCOUNTING),

	/** 会计用双下横线. */
	DOUBLE_ACCOUNTING(Font.U_DOUBLE_ACCOUNTING);

	/** The line. */
	private byte line;

	/**
	 * Instantiates a new underline.
	 *
	 * @param line the line
	 */
	private Underline(byte line) {
		this.line = line;
	}

	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public byte getLine() {
		return line;
	}

	/**
	 * 根据值返回对应的枚举值.
	 *
	 * @param line the line
	 * @return the underline
	 */
	public static Underline instance(byte line) {
		for (Underline e : Underline.values()) {
			if (e.getLine() == line) {
				return e;
			}
		}
		return Underline.NONE;
	}
}
