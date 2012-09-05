/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core MySQLDialect.java 2012-8-6 9:14:54 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc.dialect;

/**
 * The Class MySQLDialect.
 *
 * @author l.xue.nong
 */
public class MySQLDialect extends Dialect {

	/* (non-Javadoc)
	 * @see com.chinacti.dao.jdbc.dialect.Dialect#supportsLimit()
	 */
	@Override
	public boolean supportsLimit() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.dao.jdbc.dialect.Dialect#supportsLimitOffset()
	 */
	@Override
	public boolean supportsLimitOffset() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.dao.jdbc.dialect.Dialect#getLimitString(java.lang.String, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset > 0) {
			return sql + " limit " + offsetPlaceholder + "," + limitPlaceholder;
		} else {
			return sql + " limit " + limitPlaceholder;
		}
	}

}
