/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: OracleRdbms.java 2011-5-16 11:20:38 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class OracleRdbms.
 */
class OracleRdbms extends Rdbms {
	
	/**
	 * Instantiates a new oracle rdbms.
	 */
	OracleRdbms() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.kb.logsql.Rdbms#formatParameterObject(java.lang.Object)
	 */
	@Override
	String formatParameterObject(Object object) {
		if (object instanceof Timestamp) {
			return "to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(object)
					+ "', 'yyyy-mm-dd hh24:mi:ss.ff3')";
		} else if (object instanceof Date) {
			return "to_date('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object)
					+ "', 'yyyy-mm-dd hh24:mi:ss')";
		} else {
			return super.formatParameterObject(object);
		}
	}
}
