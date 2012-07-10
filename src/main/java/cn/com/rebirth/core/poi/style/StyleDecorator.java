/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core StyleDecorator.java 2012-2-3 11:23:10 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style;

/**
 * The Class StyleDecorator.
 *
 * @author l.xue.nong
 */
public class StyleDecorator {

	/** The bg color. */
	private ColorDecorator bgColor;

	/**
	 * Sets the bg color.
	 *
	 * @param bgColor the new bg color
	 */
	public void setBgColor(ColorDecorator bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Gets the bg color.
	 *
	 * @return the bg color
	 */
	public ColorDecorator getBgColor() {
		return bgColor;
	}
}
