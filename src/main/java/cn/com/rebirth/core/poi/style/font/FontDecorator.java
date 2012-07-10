/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core FontDecorator.java 2012-2-3 11:21:37 l.xue.nong$$
 */
package cn.com.rebirth.core.poi.style.font;

import org.apache.poi.ss.usermodel.Font;

import cn.com.rebirth.core.poi.style.ColorDecorator;

/**
 * The Class FontDecorator.
 *
 * @author l.xue.nong
 */
public class FontDecorator {

	/** The font. */
	private Font font;

	/**
	 * Instantiates a new font decorator.
	 *
	 * @param font the font
	 */
	public FontDecorator(Font font) {
		super();
		this.font = font;
	}

	/**
	 * Boldweight.
	 *
	 * @param boldweight the boldweight
	 * @return the font decorator
	 */
	public FontDecorator boldweight(BoldWeight boldweight) {
		font.setBoldweight(boldweight.getWeight());
		return this;
	}

	/**
	 * Boldweight.
	 *
	 * @return the bold weight
	 */
	public BoldWeight boldweight() {
		return BoldWeight.instance(font.getBoldweight());
	}

	/**
	 * Char set.
	 *
	 * @param charset the charset
	 * @return the font decorator
	 */
	public FontDecorator charSet(CharSet charset) {
		font.setCharSet(charset.getCharset());
		return this;
	}

	/**
	 * Char set.
	 *
	 * @return the char set
	 */
	public CharSet charSet() {
		return CharSet.instance((byte) font.getCharSet());
	}

	/**
	 * Color.
	 *
	 * @param color the color
	 * @return the font decorator
	 */
	public FontDecorator color(ColorDecorator color) {
		if (color.equals(ColorDecorator.AUTOMATIC)) {
			font.setColor(Font.COLOR_NORMAL);
		} else {
			font.setColor(color.getIndex());
		}
		return this;
	}

	/**
	 * Color.
	 *
	 * @return the color decorator
	 */
	public ColorDecorator color() {
		return ColorDecorator.instance(font.getColor());
	}

	/**
	 * Font height.
	 *
	 * @param height the height
	 * @return the font decorator
	 */
	public FontDecorator fontHeight(int height) {
		font.setFontHeight((short) height);
		return this;
	}

	/**
	 * Font height.
	 *
	 * @return the short
	 */
	public short fontHeight() {
		return font.getFontHeight();
	}

	/**
	 * Font height in points.
	 *
	 * @param height the height
	 * @return the font decorator
	 */
	public FontDecorator fontHeightInPoints(int height) {
		font.setFontHeightInPoints((short) height);
		return this;
	}

	/**
	 * Font height in points.
	 *
	 * @return the short
	 */
	public short fontHeightInPoints() {
		return font.getFontHeightInPoints();
	}

	/**
	 * Font name.
	 *
	 * @param name the name
	 * @return the font decorator
	 */
	public FontDecorator fontName(String name) {
		font.setFontName(name);
		return this;
	}

	/**
	 * Font name.
	 *
	 * @return the string
	 */
	public String fontName() {
		return font.getFontName();
	}

	/**
	 * Italic.
	 *
	 * @param italic the italic
	 * @return the font decorator
	 */
	public FontDecorator italic(boolean italic) {
		font.setItalic(italic);
		return this;
	}

	/**
	 * Italic.
	 *
	 * @return true, if successful
	 */
	public boolean italic() {
		return font.getItalic();
	}

	/**
	 * Strikeout.
	 *
	 * @param strikeout the strikeout
	 * @return the font decorator
	 */
	public FontDecorator strikeout(boolean strikeout) {
		font.setStrikeout(strikeout);
		return this;
	}

	/**
	 * Strikeout.
	 *
	 * @return true, if successful
	 */
	public boolean strikeout() {
		return font.getStrikeout();
	}

	/**
	 * Type offset.
	 *
	 * @param offset the offset
	 * @return the font decorator
	 */
	public FontDecorator typeOffset(TypeOffset offset) {
		font.setTypeOffset(offset.getOffset());
		return this;
	}

	/**
	 * Type offset.
	 *
	 * @return the type offset
	 */
	public TypeOffset typeOffset() {
		return TypeOffset.instance(font.getTypeOffset());
	}

	/**
	 * Underline.
	 *
	 * @param underline the underline
	 * @return the font decorator
	 */
	public FontDecorator underline(Underline underline) {
		font.setUnderline(underline.getLine());
		return this;
	}

	/**
	 * Underline.
	 *
	 * @return the underline
	 */
	public Underline underline() {
		return Underline.instance(font.getUnderline());
	}
}
