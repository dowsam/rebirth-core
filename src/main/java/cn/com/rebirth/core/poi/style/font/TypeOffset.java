/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core TypeOffset.java 2012-2-3 11:21:54 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style.font;

import org.apache.poi.ss.usermodel.Font;

/**
 * The Enum TypeOffset.
 *
 * @author l.xue.nong
 */
public enum TypeOffset {

	/** 正常. */
	NONE(Font.SS_NONE),

	/** 上标. */
	SUPER(Font.SS_SUPER),

	/** 下标. */
	SUB(Font.SS_SUB);

	/** The offset. */
	private short offset;

	/**
	 * Instantiates a new type offset.
	 *
	 * @param offset the offset
	 */
	private TypeOffset(short offset) {
		this.offset = offset;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public short getOffset() {
		return offset;
	}

	/**
	 * 根据值返回对应的枚举值.
	 *
	 * @param offset the offset
	 * @return the type offset
	 */
	public static TypeOffset instance(short offset) {
		for (TypeOffset e : TypeOffset.values()) {
			if (e.getOffset() == offset) {
				return e;
			}
		}
		return TypeOffset.NONE;
	}
}
