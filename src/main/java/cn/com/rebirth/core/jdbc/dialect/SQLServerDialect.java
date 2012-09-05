/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core SQLServerDialect.java 2012-8-6 9:14:54 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc.dialect;

/**
 * The Class SQLServerDialect.
 *
 * @author l.xue.nong
 */
public class SQLServerDialect extends Dialect {

	/* (non-Javadoc)
	 * @see com.chinacti.dao.jdbc.dialect.Dialect#supportsLimit()
	 */
	@Override
	public boolean supportsLimit() {
		return false;
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
	public String getLimitString(String querySelect, int offset, String offsetPlaceholder, int limit,
			String limitPlaceholder) {
		if (offset > 0) {
			throw new UnsupportedOperationException("sql server has no offset");
		}

		return new StringBuffer(querySelect.length() + 8).append(querySelect)
				.insert(getAfterSelectInsertPoint(querySelect), " top " + limit).toString();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.dao.jdbc.dialect.Dialect#getLimitString(java.lang.String, int, int)
	 */
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, null, limit, null);
	}

	/**
	 * Gets the after select insert point.
	 *
	 * @param sql the sql
	 * @return the after select insert point
	 */
	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

}
