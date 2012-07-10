/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core BoldWeight.java 2012-2-3 11:21:06 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style.font;

import org.apache.poi.ss.usermodel.Font;

/**
 * The Enum BoldWeight.
 *
 * @author l.xue.nong
 */
public enum BoldWeight {

	/** The NORMAL. */
	NORMAL(Font.BOLDWEIGHT_NORMAL),
	/** The BOLD. */
	BOLD(Font.BOLDWEIGHT_BOLD);

	/** The weight. */
	private short weight;

	/**
	 * Instantiates a new bold weight.
	 *
	 * @param weight the weight
	 */
	private BoldWeight(short weight) {
		this.weight = weight;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public short getWeight() {
		return weight;
	}

	/**
	 * Instance.
	 *
	 * @param weight the weight
	 * @return the bold weight
	 */
	public static BoldWeight instance(short weight) {
		for (BoldWeight e : BoldWeight.values()) {
			if (e.getWeight() == weight) {
				return e;
			}
		}
		return BoldWeight.NORMAL;
	}
}
