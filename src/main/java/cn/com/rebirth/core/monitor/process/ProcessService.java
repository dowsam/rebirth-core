/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core ProcessService.java 2012-7-6 14:30:01 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.process;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.unit.TimeValue;
import cn.com.rebirth.core.inject.Inject;

/**
 * The Class ProcessService.
 *
 * @author l.xue.nong
 */
public class ProcessService extends AbstractComponent {

	/** The probe. */
	private final ProcessProbe probe;

	/** The info. */
	private final ProcessInfo info;

	/** The refresh interval. */
	private final TimeValue refreshInterval;

	/** The cached stats. */
	private ProcessStats cachedStats;

	/**
	 * Instantiates a new process service.
	 *
	 * @param settings the settings
	 * @param probe the probe
	 */
	@Inject
	public ProcessService(Settings settings, ProcessProbe probe) {
		super(settings);
		this.probe = probe;

		this.refreshInterval = componentSettings.getAsTime("refresh_interval", TimeValue.timeValueSeconds(1));

		this.info = probe.processInfo();
		this.info.refreshInterval = refreshInterval.millis();
		this.cachedStats = probe.processStats();

		logger.debug("Using probe [{}] with refresh_interval [{}]", probe, refreshInterval);
	}

	/**
	 * Info.
	 *
	 * @return the process info
	 */
	public ProcessInfo info() {
		return this.info;
	}

	/**
	 * Stats.
	 *
	 * @return the process stats
	 */
	public synchronized ProcessStats stats() {
		if ((System.currentTimeMillis() - cachedStats.timestamp()) > refreshInterval.millis()) {
			cachedStats = probe.processStats();
		}
		return cachedStats;
	}
}
