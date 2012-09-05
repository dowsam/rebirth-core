/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: LogWaySql.java 2011-5-16 11:16:33 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import cn.com.rebirth.commons.settings.Settings;

/**
 * The Class LogWaySql.
 */
public class LogWaySql extends AbstractLogWaySql {
	protected LogWaySql(Settings settings) {
		super(settings);
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.AbstractLogWaySql#execute(com.chinacti.kb.logsql.LogSqlEntity)
	 */
	@Override
	public void execute(LogSqlEntity entity) {
		logger.error(getLogMessage(entity));
	}

}
