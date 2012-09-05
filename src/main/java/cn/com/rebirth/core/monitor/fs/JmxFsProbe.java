/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core JmxFsProbe.java 2012-7-6 14:29:08 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.fs;

import java.io.File;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.Inject;

/**
 * The Class JmxFsProbe.
 *
 * @author l.xue.nong
 */
public class JmxFsProbe extends AbstractComponent implements FsProbe {

	/** The node env. */
	private final boolean hasNodeFile;
	private final File[] dataLocations;

	/**
	 * Instantiates a new jmx fs probe.
	 *
	 * @param settings the settings
	 * @param nodeEnv the node env
	 */
	@Inject
	public JmxFsProbe(Settings settings, File[] dataLocations, boolean hasNodeFile) {
		super(settings);
		this.dataLocations = dataLocations;
		this.hasNodeFile = hasNodeFile;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.fs.FsProbe#stats()
	 */
	@Override
	public FsStats stats() {
		if (!hasNodeFile) {
			return new FsStats(System.currentTimeMillis(), new FsStats.Info[0]);
		}
		FsStats.Info[] infos = new FsStats.Info[dataLocations.length];
		for (int i = 0; i < dataLocations.length; i++) {
			File dataLocation = dataLocations[i];
			FsStats.Info info = new FsStats.Info();
			info.path = dataLocation.getAbsolutePath();
			info.total = dataLocation.getTotalSpace();
			info.free = dataLocation.getFreeSpace();
			info.available = dataLocation.getUsableSpace();
			infos[i] = info;
		}
		return new FsStats(System.currentTimeMillis(), infos);
	}
}
