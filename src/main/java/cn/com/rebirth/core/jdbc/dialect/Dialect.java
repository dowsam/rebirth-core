/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core Dialect.java 2012-8-6 9:14:56 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc.dialect;

/**
 * The Class Dialect.
 *
 * @author l.xue.nong
 */
public abstract class Dialect {

	/**
	 * Supports limit.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsLimit();

	/**
	 * Supports limit offset.
	 *
	 * @return true, if successful
	 */
	public abstract boolean supportsLimitOffset();

	/**
	 * Gets the limit string.
	 *
	 * @param sql the sql
	 * @param offset the offset
	 * @param limit the limit
	 * @return the limit string
	 */
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	/**
	 * Gets the limit string.
	 *
	 * @param sql the sql
	 * @param offset the offset
	 * @param offsetPlaceholder the offset placeholder
	 * @param limit the limit
	 * @param limitPlaceholder the limit placeholder
	 * @return the limit string
	 */
	public abstract String getLimitString(String sql, int offset, String offsetPlaceholder, int limit,
			String limitPlaceholder);
}
