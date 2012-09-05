/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core OffsetLimitResultSetExtractor.java 2012-8-6 9:16:08 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/**
 * The Class OffsetLimitResultSetExtractor.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class OffsetLimitResultSetExtractor<T> implements ResultSetExtractor<T> {

	/** The limit. */
	private int limit;

	/** The offset. */
	private int offset;

	/** The row mapper. */
	private RowMapper<T> rowMapper;

	/**
	 * Instantiates a new offset limit result set extractor.
	 *
	 * @param offset the offset
	 * @param limit the limit
	 * @param rowMapper the row mapper
	 */
	public OffsetLimitResultSetExtractor(int offset, int limit, RowMapper<T> rowMapper) {
		Assert.notNull(rowMapper, "'rowMapper' must be not null");
		this.rowMapper = rowMapper;
		this.offset = offset;
		this.limit = limit;
	}

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<T> results = new ArrayList<T>(limit > 50 ? 50 : limit);

		if (offset > 0) {
			rs.absolute(offset);
		}

		int rowNum = 0;
		while (rs.next()) {
			T row = rowMapper.mapRow(rs, rowNum++);
			results.add(row);
			if ((rowNum + 1) >= limit)
				break;
		}
		return (T) results;
	}
}
