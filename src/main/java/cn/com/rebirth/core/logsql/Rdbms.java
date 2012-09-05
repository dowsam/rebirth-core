/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: Rdbms.java 2011-5-16 11:20:00 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Rdbms.
 */
class Rdbms {
	/**
	 * Instantiates a new rdbms.
	 */
	Rdbms() {
		super();
	}

	/** The Constant dateFormat. */
	protected static final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * Format parameter object.
	 *
	 * @param object the object
	 * @return the string
	 */
	String formatParameterObject(Object object) {
		if (object == null) {
			return "NULL";
		} else {
			if (object instanceof String) {
				// todo: need to handle imbedded quotes??
				return "'" + object + "'";
			} else if (object instanceof Date) {
				return "'" + new SimpleDateFormat(dateFormat).format(object) + "'";
			} else if (object instanceof Boolean) {
				return ((Boolean) object).booleanValue() ? "1" : "0";
			} else {
				return object.toString();
			}
		}
	}

}
