/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: SqlParamEntity.java 2011-5-16 11:09:46 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.util.Arrays;

import cn.com.rebirth.commons.entity.BaseEntity;

/**
 * The Class SqlParamEntity.
 */
public abstract class SqlParamEntity extends BaseEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1752055957548713440L;

	/** The sql. */
	private String sql;

	/** The native sql. */
	private String nativeSql;

	/** The param. */
	private Object[] param;

	/**
	 * Instantiates a new sql param entity.
	 */
	public SqlParamEntity() {
		super();
	}

	/**
	 * Instantiates a new sql param entity.
	 * 
	 * @param sql
	 *            the sql
	 * @param param
	 *            the param
	 */
	public SqlParamEntity(String sql, Object[] param) {
		super();
		this.sql = sql;
		this.param = param;
	}

	/**
	 * Instantiates a new sql param entity.
	 * 
	 * @param sql
	 *            the sql
	 * @param nativeSql
	 *            the native sql
	 * @param param
	 *            the param
	 */
	public SqlParamEntity(String sql, String nativeSql, Object[] param) {
		super();
		this.sql = sql;
		this.nativeSql = nativeSql;
		this.param = param;
	}

	/**
	 * Gets the sql.
	 * 
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * Sets the sql.
	 * 
	 * @param sql
	 *            the new sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * Gets the param.
	 * 
	 * @return the param
	 */
	public Object[] getParam() {
		return param;
	}

	/**
	 * Sets the param.
	 * 
	 * @param param
	 *            the new param
	 */
	public void setParam(Object[] param) {
		this.param = param;
	}

	/**
	 * Gets the native sql.
	 * 
	 * @return the native sql
	 */
	public String getNativeSql() {
		return nativeSql;
	}

	/**
	 * Sets the native sql.
	 * 
	 * @param nativeSql
	 *            the new native sql
	 */
	public void setNativeSql(String nativeSql) {
		this.nativeSql = nativeSql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SqlParamEntity [nativeSql=" + nativeSql + ", param="
				+ Arrays.toString(param) + ", sql=" + sql + "]";
	}
}
