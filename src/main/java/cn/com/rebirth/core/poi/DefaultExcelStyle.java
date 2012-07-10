/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core DefaultExcelStyle.java 2012-2-3 11:23:45 l.xue.nong$$
 */
package cn.com.rebirth.core.poi;

import cn.com.rebirth.core.poi.style.AlignDecorator;
import cn.com.rebirth.core.poi.style.BorderStyleDecorator;
import cn.com.rebirth.core.poi.style.ColorDecorator;
import cn.com.rebirth.core.poi.style.FillPatternDecorator;
import cn.com.rebirth.core.poi.style.ValignDecorator;

/**
 * The Class DefaultExcelStyle.
 *
 * @author l.xue.nong
 */
public class DefaultExcelStyle {

	/** The font size. */
	private short fontSize = 12;

	/** The font name. */
	private String fontName = "宋体";

	/** The background color. */
	private ColorDecorator backgroundColor = ColorDecorator.AUTOMATIC;//HSSFColor.AUTOMATIC.index;

	/** The fill pattern. */
	private FillPatternDecorator fillPattern = FillPatternDecorator.NO_FILL;//HSSFCellStyle.NO_FILL;

	/** The align. */
	private AlignDecorator align = AlignDecorator.GENERAL;//HSSFCellStyle.ALIGN_GENERAL;

	/** The v align. */
	private ValignDecorator vAlign = ValignDecorator.CENTER;//HSSFCellStyle.VERTICAL_CENTER;

	/** The font color. */
	private ColorDecorator fontColor = ColorDecorator.AUTOMATIC;//HSSFFont.COLOR_NORMAL;

	/** The default date pattern. */
	private String defaultDatePattern = "yyyy/MM/dd HH:mm:ss";

	/** The border color. */
	private ColorDecorator borderColor = ColorDecorator.AUTOMATIC;

	/** The border style. */
	private BorderStyleDecorator borderStyle = BorderStyleDecorator.NONE;

	/**
	 * Gets the font size.
	 *
	 * @return the font size
	 */
	public short getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the font size.
	 *
	 * @param fontSize the new font size
	 */
	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Gets the font name.
	 *
	 * @return the font name
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * Sets the font name.
	 *
	 * @param fontName the new font name
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	/**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
	public ColorDecorator getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the background color.
	 *
	 * @param backgroundColor the new background color
	 */
	public void setBackgroundColor(ColorDecorator backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the fill pattern.
	 *
	 * @return the fill pattern
	 */
	public FillPatternDecorator getFillPattern() {
		return fillPattern;
	}

	/**
	 * Sets the fill pattern.
	 *
	 * @param fillPattern the new fill pattern
	 */
	public void setFillPattern(FillPatternDecorator fillPattern) {
		this.fillPattern = fillPattern;
	}

	/**
	 * Gets the align.
	 *
	 * @return the align
	 */
	public AlignDecorator getAlign() {
		return align;
	}

	/**
	 * Sets the align.
	 *
	 * @param align the new align
	 */
	public void setAlign(AlignDecorator align) {
		this.align = align;
	}

	/**
	 * Gets the v align.
	 *
	 * @return the v align
	 */
	public ValignDecorator getvAlign() {
		return vAlign;
	}

	/**
	 * Sets the v align.
	 *
	 * @param vAlign the new v align
	 */
	public void setvAlign(ValignDecorator vAlign) {
		this.vAlign = vAlign;
	}

	/**
	 * Gets the font color.
	 *
	 * @return the font color
	 */
	public ColorDecorator getFontColor() {
		return fontColor;
	}

	/**
	 * Sets the font color.
	 *
	 * @param fontColor the new font color
	 */
	public void setFontColor(ColorDecorator fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * Gets the default date pattern.
	 *
	 * @return the default date pattern
	 */
	public String getDefaultDatePattern() {
		return defaultDatePattern;
	}

	/**
	 * Sets the default date pattern.
	 *
	 * @param defaultDatePattern the new default date pattern
	 */
	public void setDefaultDatePattern(String defaultDatePattern) {
		this.defaultDatePattern = defaultDatePattern;
	}

	/**
	 * Gets the border color.
	 *
	 * @return the border color
	 */
	public ColorDecorator getBorderColor() {
		return borderColor;
	}

	/**
	 * Sets the border color.
	 *
	 * @param borderColor the new border color
	 */
	public void setBorderColor(ColorDecorator borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Gets the border style.
	 *
	 * @return the border style
	 */
	public BorderStyleDecorator getBorderStyle() {
		return borderStyle;
	}

	/**
	 * Sets the border style.
	 *
	 * @param borderStyle the new border style
	 */
	public void setBorderStyle(BorderStyleDecorator borderStyle) {
		this.borderStyle = borderStyle;
	}

}
