/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeConverter.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.TypeLiteral;


/**
 * The Interface TypeConverter.
 *
 * @author l.xue.nong
 */
public interface TypeConverter {

	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param toType the to type
	 * @return the object
	 */
	Object convert(String value, TypeLiteral<?> toType);
}
