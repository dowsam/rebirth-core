/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: FileChangeListener.java 2011-5-16 11:14:36 l.xue.nong$$
*/
package cn.com.rebirth.core.logsql;

/**
 * The listener interface for receiving fileChange events.
 * The class that is interested in processing a fileChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileChangeListener<code> method. When
 * the fileChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FileChangeEvent
 */
public interface FileChangeListener {
	/**
	 * 文件变化重新调用读取内容.
	 *
	 * @param filename the filename
	 */
	public void fileChanged(String filename);
}
