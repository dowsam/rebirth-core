/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core OsService.java 2012-7-6 14:29:08 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.os;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.unit.TimeValue;
import cn.com.rebirth.core.inject.Inject;

/**
 * The Class OsService.
 *
 * @author l.xue.nong
 */
public class OsService extends AbstractComponent {

	/** The probe. */
	private final OsProbe probe;

	/** The info. */
	private final OsInfo info;

	/** The refresh interval. */
	private final TimeValue refreshInterval;

	/** The cached stats. */
	private OsStats cachedStats;

	/**
	 * Instantiates a new os service.
	 *
	 * @param settings the settings
	 * @param probe the probe
	 */
	@Inject
	public OsService(Settings settings, OsProbe probe) {
		super(settings);
		this.probe = probe;

		this.refreshInterval = componentSettings.getAsTime("refresh_interval", TimeValue.timeValueSeconds(1));

		this.info = probe.osInfo();
		this.info.refreshInterval = refreshInterval.millis();
		this.cachedStats = probe.osStats();

		logger.debug("Using probe [{}] with refresh_interval [{}]", probe, refreshInterval);
	}

	/**
	 * Info.
	 *
	 * @return the os info
	 */
	public OsInfo info() {
		return this.info;
	}

	/**
	 * Stats.
	 *
	 * @return the os stats
	 */
	public synchronized OsStats stats() {
		if ((System.currentTimeMillis() - cachedStats.timestamp()) > refreshInterval.millis()) {
			cachedStats = probe.osStats();
		}
		return cachedStats;
	}
}
