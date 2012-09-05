/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: TraceLogWaySql.java 2011-5-16 11:42:38 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.utils.TraceUtils;

/**
 * The Class TraceLogWaySql.
 */
public class TraceLogWaySql extends AbstractLogWaySql {

	protected TraceLogWaySql(Settings settings) {
		super(settings);
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.AbstractLogWaySql#execute(com.chinacti.kb.logsql.LogSqlEntity)
	 */
	@Override
	public void execute(LogSqlEntity entity) {
		TraceUtils.beginTrace();
		logger.debug(getLogMessage(entity));
		TraceUtils.endTrace();
	}

}