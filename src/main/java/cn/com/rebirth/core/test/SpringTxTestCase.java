/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core SpringTxTestCase.java 2012-2-3 12:47:47 l.xue.nong$$
 */
package cn.com.rebirth.core.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring的支持数据库访问和依赖注入的JUnit4 集成测试基类.
 * 
 * 1.Spring Context IOC support 2.Spring Transaction support 3.Spring
 * JdbcTemplate and util functions 4.JUnit Assert functions
 * 
 * 子类需要定义applicationContext文件的位置, 如:
 * 
 * @ContextConfiguration(locations = { "/applicationContext-test.xml" })
 * 
 * @see AbstractTransactionalJUnit4SpringContextTests
 * @see SpringContextTestCase
 * 
 * @author XiaoLu
 */
@SuppressWarnings("deprecation")
@Transactional
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class SpringTxTestCase extends SpringContextTestCase {

	/** The data source. */
	protected DataSource dataSource;

	/** The jdbc template. */
	protected SimpleJdbcTemplate jdbcTemplate;

	/** The sql script encoding. */
	protected String sqlScriptEncoding;

	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new data source
	 */
	@Autowired(required = false)
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
		this.dataSource = dataSource;

	}

	/**
	 * Sets the sql script encoding.
	 *
	 * @param sqlScriptEncoding the new sql script encoding
	 */
	public void setSqlScriptEncoding(String sqlScriptEncoding) {
		this.sqlScriptEncoding = sqlScriptEncoding;
	}

	/**
	 * Count rows in table.
	 *
	 * @param tableName the table name
	 * @return the int
	 */
	protected int countRowsInTable(String tableName) {
		return SimpleJdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
	}

	/**
	 * Delete from tables.
	 *
	 * @param names the names
	 * @return the int
	 */
	protected int deleteFromTables(String... names) {
		return SimpleJdbcTestUtils.deleteFromTables(this.jdbcTemplate, names);
	}

	/**
	 * Run sql.
	 *
	 * @param sqlResourcePath the sql resource path
	 * @param continueOnError the continue on error
	 * @throws DataAccessException the data access exception
	 */
	protected void runSql(String sqlResourcePath, boolean continueOnError) throws DataAccessException {
		Resource resource = this.applicationContext.getResource(sqlResourcePath);
		SimpleJdbcTestUtils.executeSqlScript(this.jdbcTemplate, new EncodedResource(resource, this.sqlScriptEncoding),
				continueOnError);
	}
}
