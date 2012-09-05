/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: DataSourceInterceptor.java 2011-5-16 11:22:12 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import cn.com.rebirth.commons.settings.Settings;

/**
 * The Class DataSourceInterceptor.
 */
public class DataSourceInterceptor implements MethodInterceptor {

	/** The rdbms. */
	private Rdbms rdbms;
	private Settings settings;

	/**
	 * Gets the rdbms.
	 *
	 * @return the rdbms
	 */
	public Rdbms getRdbms() {
		return rdbms;
	}

	/**
	 * Sets the rdbms.
	 *
	 * @param rdbms the new rdbms
	 */
	public void setRdbms(Rdbms rdbms) {
		this.rdbms = rdbms;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		Object result = arg0.proceed();
		if (settings.getAsBoolean("switchSql", false)) {
			if (result instanceof Connection) {
				Connection conn = (Connection) result;
				return rdbms == null && settings == null ? new ConnectionWarpper(conn)
						: (rdbms == null ? new ConnectionWarpper(conn, null, settings) : new ConnectionWarpper(conn,
								rdbms));
			}
		}
		return result;
	}

}
