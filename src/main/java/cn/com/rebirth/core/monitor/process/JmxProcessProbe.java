/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core JmxProcessProbe.java 2012-7-6 14:30:28 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.process;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.monitor.jvm.JvmInfo;

/**
 * The Class JmxProcessProbe.
 *
 * @author l.xue.nong
 */
public class JmxProcessProbe extends AbstractComponent implements ProcessProbe {

	/** The Constant osMxBean. */
	private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

	/** The Constant getMaxFileDescriptorCountField. */
	private static final Method getMaxFileDescriptorCountField;

	/** The Constant getOpenFileDescriptorCountField. */
	private static final Method getOpenFileDescriptorCountField;

	static {
		Method method = null;
		try {
			method = osMxBean.getClass().getDeclaredMethod("getMaxFileDescriptorCount");
			method.setAccessible(true);
		} catch (Exception e) {

		}
		getMaxFileDescriptorCountField = method;

		method = null;
		try {
			method = osMxBean.getClass().getDeclaredMethod("getOpenFileDescriptorCount");
			method.setAccessible(true);
		} catch (Exception e) {

		}
		getOpenFileDescriptorCountField = method;
	}

	/**
	 * Gets the max file descriptor count.
	 *
	 * @return the max file descriptor count
	 */
	public static long getMaxFileDescriptorCount() {
		if (getMaxFileDescriptorCountField == null) {
			return -1;
		}
		try {
			return (Long) getMaxFileDescriptorCountField.invoke(osMxBean);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Gets the open file descriptor count.
	 *
	 * @return the open file descriptor count
	 */
	public static long getOpenFileDescriptorCount() {
		if (getOpenFileDescriptorCountField == null) {
			return -1;
		}
		try {
			return (Long) getOpenFileDescriptorCountField.invoke(osMxBean);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Instantiates a new jmx process probe.
	 *
	 * @param settings the settings
	 */
	@Inject
	public JmxProcessProbe(Settings settings) {
		super(settings);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.process.ProcessProbe#processInfo()
	 */
	@Override
	public ProcessInfo processInfo() {
		return new ProcessInfo(JvmInfo.jvmInfo().pid(), getMaxFileDescriptorCount());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.process.ProcessProbe#processStats()
	 */
	@Override
	public ProcessStats processStats() {
		ProcessStats stats = new ProcessStats();
		stats.timestamp = System.currentTimeMillis();
		stats.openFileDescriptors = getOpenFileDescriptorCount();
		return stats;
	}
}
