/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: DefaultSystemEvnServerInfo.java 2011-5-27 14:15:14 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import cn.com.rebirth.commons.utils.IpUtils;

/**
 * The Class DefaultSystemEvnServerInfo.
 */
public class DefaultSystemEvnServerInfo implements ServerInfo {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4327828229220837981L;

	/** The Constant KEY. */
	public static final String KEY = "log.sql.port";

	/* (non-Javadoc)
	 * @see com.chinacti.logsql.ServerInfo#bulidServerIp()
	 */
	@Override
	public String bulidServerIp() {
		return IpUtils.getRealIp();
	}

	/* (non-Javadoc)
	 * @see com.chinacti.logsql.ServerInfo#bulidServerPort()
	 */
	@Override
	public Integer bulidServerPort() {
		try {
			String value = System.getProperty(DefaultSystemEvnServerInfo.KEY);
			return value != null ? Integer.parseInt(value) : null;
		} catch (Exception e) {
			return null;
		}
	}

}
