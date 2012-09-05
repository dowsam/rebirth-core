/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: ConcurrentWaySql.java 2011-5-16 14:28:41 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

import java.util.concurrent.BlockingQueue;

/**
 * The Interface ConcurrentWaySql.
 */
public interface ConcurrentWaySql extends WaySql {

	/**
	 * Gets the blocking queue.
	 *
	 * @return the blocking queue
	 */
	public BlockingQueue<LogSqlEntity> getBlockingQueue();
}
