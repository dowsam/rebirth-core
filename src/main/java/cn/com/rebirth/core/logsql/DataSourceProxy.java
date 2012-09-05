/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core DataSourceProxy.java 2012-8-16 11:01:38 l.xue.nong$$
 */
package cn.com.rebirth.core.logsql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.InjectInitialization;

/**
 * The Class DataSourceProxy.
 *
 * @author l.xue.nong
 */
public class DataSourceProxy extends AbstractComponent implements DataSource {

	/** The real data source. */
	private final DataSource realDataSource;

	/** The rdbms. */
	private Rdbms rdbms = new Rdbms();

	/**
	 * Instantiates a new data source proxy.
	 *
	 * @param dataSource the data source
	 */
	public DataSourceProxy(DataSource dataSource) {
		this(dataSource, InjectInitialization.injector().getInstance(Settings.class));
	}

	/**
	 * Instantiates a new data source proxy.
	 *
	 * @param dataSource the data source
	 * @param settings the settings
	 */
	public DataSourceProxy(DataSource dataSource, Settings settings) {
		super(settings);
		this.realDataSource = dataSource;
	}

	/**
	 * Gets the rdbms.
	 *
	 * @return the rdbms
	 */
	public Rdbms getRdbms() {
		return rdbms;
	}

	/**
	 * Sets the rdbms.
	 *
	 * @param rdbms the new rdbms
	 */
	public void setRdbms(Rdbms rdbms) {
		this.rdbms = rdbms;
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return realDataSource.getLogWriter();
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		realDataSource.setLogWriter(out);
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		realDataSource.setLoginTimeout(seconds);
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return realDataSource.getLoginTimeout();
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realDataSource.unwrap(iface);
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return realDataSource.isWrapperFor(iface);
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if (settings.getAsBoolean("switchSql", false)) {
			return new ConnectionWarpper(realDataSource.getConnection(), rdbms, settings);
		}
		return realDataSource.getConnection();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (settings.getAsBoolean("switchSql", false)) {
			return new ConnectionWarpper(realDataSource.getConnection(username, password), rdbms, settings);
		}
		return realDataSource.getConnection(username, password);
	}

}
