/**
 * Copyright (c) 2005-2011 www.china-cti.com
 * Id: DbWaySql.java 2011-5-16 11:22:51 l.xue.nong$$
 */
package cn.com.rebirth.core.logsql;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import cn.com.rebirth.commons.settings.Settings;

/**
 * The Class DbWaySql.
 */
public class DbWaySql extends AbstractLogWaySql {

	protected DbWaySql(Settings settings) {
		super(settings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinacti.kb.logsql.AbstractLogWaySql#execute(com.chinacti.kb.logsql
	 * .LogSqlEntity)
	 */
	@Override
	public void execute(final LogSqlEntity entity) {
		DataSource dataSource = bulidOtherDataSource();
		final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				namedParameterJdbcTemplate
						.update("insert into system_logsql(ID,SQL,TIME,USERID,CREATEDATE,METHODNAME,REQUESTIP,STACKTRACEELEMENT,SERVERIP,SERVERPORT,SQLTYPEENUM) values(:id,:sql,:time,:userId,:createDate,:methodName,:requestIp,:stackTraceElement,:serverIp,:serverPort,:sqlTypeEnum)",
								paramBeanMapper(entity));
			}
		});

	}

}
