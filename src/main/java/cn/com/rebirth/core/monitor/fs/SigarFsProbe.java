/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core SigarFsProbe.java 2012-7-6 14:30:28 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.fs;

import java.io.File;
import java.util.Map;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.monitor.sigar.SigarService;

import com.google.common.collect.Maps;

/**
 * The Class SigarFsProbe.
 *
 * @author l.xue.nong
 */
public class SigarFsProbe extends AbstractComponent implements FsProbe {

	private final boolean hasNodeFile;
	private final File[] dataLocations;

	/** The sigar service. */
	private final SigarService sigarService;

	/** The file systems. */
	private Map<File, FileSystem> fileSystems = Maps.newHashMap();

	/**
	 * Instantiates a new sigar fs probe.
	 *
	 * @param settings the settings
	 * @param nodeEnv the node env
	 * @param sigarService the sigar service
	 */
	@Inject
	public SigarFsProbe(Settings settings, SigarService sigarService, File[] dataLocations, boolean hasNodeFile) {
		super(settings);
		this.sigarService = sigarService;
		this.dataLocations = dataLocations;
		this.hasNodeFile = hasNodeFile;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.fs.FsProbe#stats()
	 */
	@Override
	public synchronized FsStats stats() {
		if (!hasNodeFile) {
			return new FsStats(System.currentTimeMillis(), new FsStats.Info[0]);
		}
		FsStats.Info[] infos = new FsStats.Info[dataLocations.length];
		for (int i = 0; i < dataLocations.length; i++) {
			File dataLocation = dataLocations[i];

			FsStats.Info info = new FsStats.Info();
			info.path = dataLocation.getAbsolutePath();

			try {
				FileSystem fileSystem = fileSystems.get(dataLocation);
				if (fileSystem == null) {
					fileSystem = sigarService.sigar().getFileSystemMap().getMountPoint(dataLocation.getPath());
					fileSystems.put(dataLocation, fileSystem);
				}

				FileSystemUsage fileSystemUsage = sigarService.sigar().getFileSystemUsage(fileSystem.getDirName());
				info.mount = fileSystem.getDirName();
				info.dev = fileSystem.getDevName();

				info.total = fileSystemUsage.getTotal() * 1024;
				info.free = fileSystemUsage.getFree() * 1024;
				info.available = fileSystemUsage.getAvail() * 1024;
				info.diskReads = fileSystemUsage.getDiskReads();
				info.diskWrites = fileSystemUsage.getDiskWrites();
				info.diskReadBytes = fileSystemUsage.getDiskReadBytes();
				info.diskWriteBytes = fileSystemUsage.getDiskWriteBytes();
				info.diskQueue = fileSystemUsage.getDiskQueue();
				info.diskServiceTime = fileSystemUsage.getDiskServiceTime();
			} catch (SigarException e) {

			}

			infos[i] = info;
		}

		return new FsStats(System.currentTimeMillis(), infos);
	}
}
