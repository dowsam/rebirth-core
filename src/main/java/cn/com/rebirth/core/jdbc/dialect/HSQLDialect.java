/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core HSQLDialect.java 2012-8-6 9:14:53 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc.dialect;

/**
 * The Class HSQLDialect.
 *
 * @author l.xue.nong
 */
public class HSQLDialect extends Dialect {

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
		boolean hasOffset = offset > 0;
		return new StringBuffer(sql.length() + 10)
				.append(sql)
				.insert(sql.toLowerCase().indexOf("select") + 6,
						hasOffset ? " limit " + offsetPlaceholder + " " + limitPlaceholder : " top " + limitPlaceholder)
				.toString();
	}

}
