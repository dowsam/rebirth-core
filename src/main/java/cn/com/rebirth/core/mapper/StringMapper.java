/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core StringMapper.java 2012-2-3 9:58:00 l.xue.nong$$
 */
package cn.com.rebirth.core.mapper;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import cn.com.rebirth.commons.utils.ReflectionUtils;

/**
 * 简单封装Apache BeanUtils的ConvertUtils, 实现String<->Java Object的工具类.
 * 
 * 可转换的类型包括各种Primitives和Date。
 *
 * @author l.xue.nong
 */
public class StringMapper {

	/** The DEFAUL t_ dat e_ format. */
	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/** The DEFAUL t_ datetim e_ format. */
	public static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * String->Object.
	 *
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 * @return the object
	 */
	public static Object fromString(String value, Class<?> toType) {
		try {
			return ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * Object -> String.
	 *
	 * @param object the object
	 * @return the string
	 */
	public static String toString(Object object) {
		try {
			return ConvertUtils.convert(object);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 定义Apache BeanUtils日期Converter的格式,可注册多个格式,以','分隔.
	 *
	 * @param patterns the patterns
	 */
	public static void registerDateConverter(String... patterns) {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(patterns);
		ConvertUtils.register(dc, Date.class);
	}

	static {
		//初始化日期格式为yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
		StringMapper.registerDateConverter(DEFAULT_DATE_FORMAT, DEFAULT_DATETIME_FORMAT);
	}

}
