/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core FsService.java 2012-7-6 14:29:13 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.fs;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.unit.TimeValue;
import cn.com.rebirth.core.inject.Inject;

/**
 * The Class FsService.
 *
 * @author l.xue.nong
 */
public class FsService extends AbstractComponent {

	/** The probe. */
	private final FsProbe probe;

	/** The refresh interval. */
	private final TimeValue refreshInterval;

	/** The cached stats. */
	private FsStats cachedStats;

	/**
	 * Instantiates a new fs service.
	 *
	 * @param settings the settings
	 * @param probe the probe
	 */
	@Inject
	public FsService(Settings settings, FsProbe probe) {
		super(settings);
		this.probe = probe;
		this.cachedStats = probe.stats();

		this.refreshInterval = componentSettings.getAsTime("refresh_interval", TimeValue.timeValueSeconds(1));

		logger.debug("Using probe [{}] with refresh_interval [{}]", probe, refreshInterval);
	}

	/**
	 * Stats.
	 *
	 * @return the fs stats
	 */
	public synchronized FsStats stats() {
		if ((System.currentTimeMillis() - cachedStats.timestamp()) > refreshInterval.millis()) {
			cachedStats = probe.stats();
		}
		return cachedStats;
	}

}
