/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: LogSqlException.java 2011-5-16 11:13:37 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import cn.com.rebirth.commons.exception.RebirthException;

/**
 * The Class LogSqlException.
 */
public class LogSqlException extends RebirthException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5599272359799670434L;
	/**
	 * Instantiates a new log sql exception.
	 *
	 * @param message the message
	 */
	public LogSqlException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new log sql exception.
	 *
	 * @param t the t
	 */
	public LogSqlException(Throwable t) {
		this(t.toString(), t);
	}

	/**
	 * Instantiates a new log sql exception.
	 *
	 * @param message the message
	 * @param t the t
	 */
	public LogSqlException(String message, Throwable t) {
		super(message, t);
		this.setStackTrace(t.getStackTrace());
	}
}
