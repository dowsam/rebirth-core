/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core CharSet.java 2012-2-3 11:21:23 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style.font;

import org.apache.poi.ss.usermodel.Font;

/**
 * The Enum CharSet.
 *
 * @author l.xue.nong
 */
public enum CharSet {

	/** The ANSI. */
	ANSI(Font.ANSI_CHARSET),

	/** The DEFAULT. */
	DEFAULT(Font.DEFAULT_CHARSET),

	/** The SYMBOL. */
	SYMBOL(Font.SYMBOL_CHARSET);

	/** The charset. */
	private byte charset;

	/**
	 * Instantiates a new char set.
	 *
	 * @param charset the charset
	 */
	private CharSet(byte charset) {
		this.charset = charset;
	}

	/**
	 * Gets the charset.
	 *
	 * @return the charset
	 */
	public byte getCharset() {
		return charset;
	}

	/**
	 * Instance.
	 *
	 * @param charset the charset
	 * @return the char set
	 */
	public static CharSet instance(byte charset) {
		for (CharSet e : CharSet.values()) {
			if (e.getCharset() == charset) {
				return e;
			}
		}
		return CharSet.DEFAULT;
	}
}
