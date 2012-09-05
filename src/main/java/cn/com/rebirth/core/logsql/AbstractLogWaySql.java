/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: AbstractLogWaySql.java 2011-5-16 11:11:46 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.utils.DateUtils;
import cn.com.rebirth.commons.utils.JndiUtils;
import cn.com.rebirth.core.queue.BlockingConsumer;

/**
 * The Class AbstractLogWaySql.
 */
public abstract class AbstractLogWaySql extends BlockingConsumer implements WaySql {
	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Settings settings;

	protected AbstractLogWaySql(Settings settings) {
		super();
		this.settings = settings;
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.WaySql#action(com.chinacti.kb.logsql.LogSqlEntity)
	 */
	@Override
	public void action(LogSqlEntity entity) {
		if (this instanceof ConcurrentWaySql) {
			((ConcurrentWaySql) this).getBlockingQueue().offer(entity);
		} else {
			execute(entity);
		}
	}

	/**
	 * Execute.
	 *
	 * @param entity the entity
	 */
	public abstract void execute(LogSqlEntity entity);

	/**
	 * Gets the log message.
	 *
	 * @param entity the entity
	 * @return the log message
	 */
	protected String getLogMessage(LogSqlEntity entity) {
		String message = "MethodName:[" + entity.getMethodName() + "] SQL:[" + entity.getSql() + "] Time["
				+ entity.getTime() + "] User:[" + entity.getUserId() + "] Date:["
				+ (entity.getCreateDate() == null ? "null" : DateUtils.formatDate(entity.getCreateDate(), null))
				+ "] RequestIp:[" + entity.getRequestIp() + "] serverIp:[" + entity.getServerIp() + "] serverPort:["
				+ entity.getServerPort() + "] sqlTypeEnum:[" + entity.getSqlTypeEnum().name() + "]";
		if (settings.getAsBoolean("printStackTrace", true)) {
			message = message + " StackTraceElement:[" + entity.getStackTraceElement() + "]";
		}
		return message;
	}

	/**
	 * Bulid other data source.
	 *
	 * @return the data source
	 */
	protected DataSource bulidOtherDataSource() {
		String dataSourceType = settings.get("dataSourceType", "dataSource");
		if ("jndi".equalsIgnoreCase(dataSourceType)) {
			return (DataSource) JndiUtils.lookup(settings.get("jndi"));
		} else {
			return getDefaultDataSource();
		}
	}

	DataSource getDefaultDataSource() {
		DriverManagerDataSource connectionDataSource = new DriverManagerDataSource();
		String driverClassName = settings.get("driverClassName");
		String url = settings.get("url");
		String username = settings.get("username");
		String password = settings.get("password");
		if (StringUtils.isNotBlank(driverClassName))
			connectionDataSource.setDriverClassName(driverClassName);
		if (StringUtils.isNotBlank(url))
			connectionDataSource.setUrl(url);
		if (StringUtils.isNotBlank(username))
			connectionDataSource.setUsername(username);
		if (StringUtils.isNotBlank(password))
			connectionDataSource.setPassword(password);
		return connectionDataSource;
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

	/* (non-Javadoc)
	 * @see com.chinacti.queue.BlockingConsumer#processMessage(java.lang.Object)
	 */
	@Override
	protected void processMessage(Object message) {
		execute((LogSqlEntity) message);
	}

	/* (non-Javadoc)
	 * @see com.chinacti.queue.BlockingConsumer#clean()
	 */
	@Override
	protected void clean() {

	}

}
