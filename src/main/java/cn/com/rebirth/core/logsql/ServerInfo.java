/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: ServerInfo.java 2011-5-27 13:53:44 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.io.Serializable;

/**
 * The Interface ServerInfo.
 */
public interface ServerInfo extends Serializable {
	
	/**
	 * Bulid server ip.
	 *
	 * @return the string
	 */
	public String bulidServerIp();

	/**
	 * Bulid server port.
	 *
	 * @return the integer
	 */
	public Integer bulidServerPort();
}
