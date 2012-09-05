/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core JdbcDao.java 2012-8-6 9:23:29 l.xue.nong$$
 */
package cn.com.rebirth.core.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import cn.com.rebirth.commons.Page;
import cn.com.rebirth.commons.PageRequest;
import cn.com.rebirth.commons.PageRequest.Order;
import cn.com.rebirth.core.jdbc.dialect.Dialect;
import cn.com.rebirth.core.jdbc.dialect.H2Dialect;
import cn.com.rebirth.core.jdbc.dialect.MySQLDialect;
import cn.com.rebirth.core.jdbc.dialect.OracleDialect;
import cn.com.rebirth.core.jdbc.spring.OffsetLimitResultSetExtractor;

/**
 * The Class JdbcDao.
 *
 * @author l.xue.nong
 */
@Repository
public class JdbcDao {

	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** The simple jdbc template. */
	protected JdbcTemplate simpleJdbcTemplate;

	/** The simple jdbc insert. */
	protected SimpleJdbcInsert simpleJdbcInsert;

	/** The named parameter jdbc operations. */
	protected NamedParameterJdbcOperations namedParameterJdbcOperations;

	/** The dialect. */
	protected Dialect dialect = new OracleDialect();

	/**
	 * Instantiates a new jdbc dao.
	 *
	 * @param dataSource the data source
	 */
	public JdbcDao(DataSource dataSource) {
		super();
		this.setDataSource(dataSource);
	}

	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new data source
	 */
	@Autowired(required = false)
	public void setDataSource(DataSource dataSource) {
		simpleJdbcTemplate = new JdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
		this.dialect = getDialect(dataSource);
	}

	/**
	 * Gets the dialect.
	 *
	 * @param dataSource the data source
	 * @return the dialect
	 */
	protected Dialect getDialect(DataSource dataSource) {
		//从DataSource中取出jdbcUrl.
		String jdbcUrl = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (connection == null) {
				throw new IllegalStateException("Connection returned by DataSource [" + dataSource + "] was null");
			}
			jdbcUrl = connection.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		//根据jdbc url判断dialect
		if (StringUtils.contains(jdbcUrl, ":h2:")) {
			return new H2Dialect();
		} else if (StringUtils.contains(jdbcUrl, ":mysql:")) {
			return new MySQLDialect();
		} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
			return new OracleDialect();
		} else {
			throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
		}
	}

	/**
	 * Find.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param clazz the clazz
	 * @param parameters the parameters
	 * @return the list
	 */
	public <T> List<T> find(final String sql, Class<T> clazz, Map<String, Object> parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			Assert.notNull(clazz, "集合中对象类型不能为空!");
			if (parameters != null) {
				return simpleJdbcTemplate.query(sql, resultBeanMapper(clazz), parameters);
			} else {
				return simpleJdbcTemplate.query(sql, resultBeanMapper(clazz));
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param clazz the clazz
	 * @param objects the objects
	 * @return the list
	 */
	public <T> List<T> find(final String sql, Class<T> clazz, Object... objects) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			Assert.notNull(clazz, "集合中对象类型不能为空!");
			return simpleJdbcTemplate.query(sql, resultBeanMapper(clazz), objects);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find for object.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param clazz the clazz
	 * @param parameters the parameters
	 * @return the t
	 */
	public <T> T findForObject(final String sql, Class<T> clazz, Map<String, Object> parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			Assert.notNull(clazz, "集合中对象类型不能为空!");
			if (parameters != null) {
				return simpleJdbcTemplate.queryForObject(sql, resultBeanMapper(clazz), parameters);
			} else {
				return simpleJdbcTemplate.queryForObject(sql, resultBeanMapper(clazz));
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find for object.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param clazz the clazz
	 * @param parameters the parameters
	 * @return the t
	 */
	public <T> T findForObject(final String sql, Class<T> clazz, Object... parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			Assert.notNull(clazz, "集合中对象类型不能为空!");
			return simpleJdbcTemplate.queryForObject(sql, resultBeanMapper(clazz), parameters);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Query for int.
	 *
	 * @param sql the sql
	 * @param args the args
	 * @return the int
	 */
	public int queryForInt(String sql, Map<String, ?> args) {
		return simpleJdbcTemplate.queryForInt(sql, args);
	}

	/**
	 * Query for int.
	 *
	 * @param sql the sql
	 * @param args the args
	 * @return the int
	 */
	public int queryForInt(String sql, Object... args) {
		return simpleJdbcTemplate.queryForInt(sql, args);
	}

	/**
	 * Find for long.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the long
	 */
	public long findForLong(final String sql, Map<String, Object> parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			if (parameters != null) {
				return simpleJdbcTemplate.queryForLong(sql, parameters);
			} else {
				return simpleJdbcTemplate.queryForLong(sql);
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Find for long.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the long
	 */
	public long findForLong(final String sql, Object... parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			return simpleJdbcTemplate.queryForLong(sql, parameters);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Find for map.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the map
	 */
	public Map<String, Object> findForMap(final String sql, Map<String, Object> parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			if (parameters != null) {
				return simpleJdbcTemplate.queryForMap(sql, parameters);
			} else {
				return simpleJdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find for map.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the map
	 */
	public Map<String, Object> findForMap(final String sql, Object... parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			return simpleJdbcTemplate.queryForMap(sql, parameters);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find for list map.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the list
	 */
	public List<Map<String, Object>> findForListMap(final String sql, Map<String, Object> parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			if (parameters != null) {
				return simpleJdbcTemplate.queryForList(sql, parameters);
			} else {
				return simpleJdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Find for list map.
	 *
	 * @param sql the sql
	 * @param parameters the parameters
	 * @return the list
	 */
	public List<Map<String, Object>> findForListMap(final String sql, Object... parameters) {
		try {
			Assert.hasText(sql, "sql语句不正确!");
			return simpleJdbcTemplate.queryForList(sql, parameters);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Execute for object.
	 *
	 * @param sql the sql
	 * @param bean the bean
	 * @return the int
	 */
	public int executeForObject(final String sql, Object bean) {
		Assert.hasText(sql, "sql语句不正确!");
		return simpleJdbcTemplate.update(sql, paramBeanMapper(bean));
	}

	/**
	 * Execute for object.
	 *
	 * @param sql the sql
	 * @param args the args
	 * @return the int
	 */
	public int executeForObject(final String sql, Map<String, Object> args) {
		Assert.hasText(sql, "sql语句不正确!");
		return simpleJdbcTemplate.update(sql, args);
	}

	/**
	 * Execute for object.
	 *
	 * @param sql the sql
	 * @param args the args
	 * @return the int
	 */
	public int executeForObject(final String sql, Object... args) {
		Assert.hasText(sql, "sql语句不正确!");
		return simpleJdbcTemplate.update(sql, args);
	}

	/**
	 * Batch update.
	 *
	 * @param sql the sql
	 * @param batch the batch
	 * @return the int[]
	 */
	public int[] batchUpdate(final String sql, List<Object[]> batch) {
		int[] updateCounts = simpleJdbcTemplate.batchUpdate(sql, batch);
		return updateCounts;
	}

	/**
	 * Batch update.
	 *
	 * @param sql the sql
	 * @param batch the batch
	 * @param argTypes the arg types
	 * @return the int[]
	 */
	public int[] batchUpdate(final String sql, List<Object[]> batch, int[] argTypes) {
		int[] updateCounts = simpleJdbcTemplate.batchUpdate(sql, batch, argTypes);
		return updateCounts;
	}

	/**
	 * Batch update.
	 *
	 * @param sql the sql
	 * @param batchValues the batch values
	 * @return the int[]
	 */
	public int[] batchUpdate(final String sql, Map<String, ?>[] batchValues) {
		int[] updateCounts = namedParameterJdbcOperations.batchUpdate(sql, batchValues);
		return updateCounts;
	}

	/**
	 * Result bean mapper.
	 *
	 * @param <T> the generic type
	 * @param entity the entity
	 * @return the row mapper
	 */
	protected <T> RowMapper<T> resultBeanMapper(Class<T> entity) {
		if (isPrimitiveOrWrapper(entity)) {
			return new SingleColumnRowMapper<T>(entity);
		}
		return new BeanPropertyRowMapper<T>(entity);
	}

	/**
	 * Checks if is primitive or wrapper.
	 *
	 * @param <T> the generic type
	 * @param entity the entity
	 * @return true, if is primitive or wrapper
	 */
	protected <T> boolean isPrimitiveOrWrapper(Class<T> entity) {
		boolean b = ClassUtils.isPrimitiveOrWrapper(entity);
		if (b)
			return true;
		Class<?> tempClass = ClassUtils.resolvePrimitiveClassName(entity.getName());
		if (tempClass != null)
			return true;
		if (entity == String.class)
			return true;
		return false;
	}

	/**
	 * Param bean mapper.
	 *
	 * @param object the object
	 * @return the bean property sql parameter source
	 */
	protected BeanPropertySqlParameterSource paramBeanMapper(Object object) {
		return new BeanPropertySqlParameterSource(object);
	}

	/**
	 * Gets the simple jdbc template.
	 *
	 * @return the simple jdbc template
	 */
	public JdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	/**
	 * Gets the simple jdbc insert.
	 *
	 * @return the simple jdbc insert
	 */
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	/**
	 * Gets the named parameter jdbc operations.
	 *
	 * @return the named parameter jdbc operations
	 */
	public NamedParameterJdbcOperations getNamedParameterJdbcOperations() {
		return namedParameterJdbcOperations;
	}

	/**
	 * Find page.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param query the query
	 * @param pageRequest the page request
	 * @param values the values
	 * @return the page
	 */
	public <T> Page<T> findPage(Class<T> entityClass, String query, PageRequest pageRequest, Object... values) {
		Page<T> page = new Page<T>(pageRequest);
		if (pageRequest.isCountTotal()) {
			String countQuery = "select count(*) from (" + (removeOrders(query)) + ")";
			long totalCount = findForLong(countQuery, values);
			page.setTotalItems(totalCount);
		}
		if (pageRequest.getSort() != null) {
			query = setOrderParameterToSqlOrHql(removeOrders(query), pageRequest);
		}
		List<T> list = pageQuery(query, pageRequest.getOffset(), pageRequest.getPageSize(),
				resultBeanMapper(entityClass), values);
		page.setResult(list);
		return page;
	}

	/**
	 * Page query.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param startRow the start row
	 * @param pageSize the page size
	 * @param rowMapper the row mapper
	 * @param values the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> pageQuery(String sql, int startRow, int pageSize, final RowMapper<T> rowMapper, Object... values) {
		//支持limit查询
		if (dialect.supportsLimit()) {
			//支持limit及offset.则完全使用数据库分页
			if (dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql, startRow, pageSize);
				startRow = 0;
			} else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0, pageSize);
			}

			pageSize = Integer.MAX_VALUE;
		}
		return (List<T>) this.namedParameterJdbcOperations.getJdbcOperations().query(sql, values,
				new OffsetLimitResultSetExtractor<T>(startRow, pageSize, rowMapper));
	}

	/**
	 * Removes the select.
	 *
	 * @param sql the sql
	 * @return the string
	 */
	protected String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = indexOfByRegex(sql.toLowerCase(), "\\sfrom\\s");
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	/**
	 * Index of by regex.
	 *
	 * @param input the input
	 * @param regex the regex
	 * @return the int
	 */
	private int indexOfByRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if (m.find()) {
			return m.start();
		}
		return -1;
	}

	/**
	 * Removes the orders.
	 *
	 * @param sqlOrHql the sql or hql
	 * @return the string
	 */
	public static String removeOrders(String sqlOrHql) {
		Assert.hasText(sqlOrHql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sqlOrHql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Sets the order parameter to sql or hql.
	 *
	 * @param hql the hql
	 * @param pageRequest the page request
	 * @return the string
	 */
	protected String setOrderParameterToSqlOrHql(final String hql, final PageRequest pageRequest) {
		StringBuilder builder = new StringBuilder(hql);
		builder.append(" order by");
		for (Order order : pageRequest.getSort()) {
			builder.append(String.format(" %s %s,", order.getProperty(), order.getDirection().name()));
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

}
