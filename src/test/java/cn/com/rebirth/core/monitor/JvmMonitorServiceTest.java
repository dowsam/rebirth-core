package cn.com.rebirth.core.monitor;

import cn.com.rebirth.commons.settings.ImmutableSettings;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.monitor.dump.DumpMonitorService;
import cn.com.rebirth.core.monitor.jvm.JvmMonitorService;
import cn.com.rebirth.core.threadpool.ThreadPool;

public class JvmMonitorServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;
		ThreadPool threadPool = new ThreadPool(settings);
		DumpMonitorService dumpMonitorService = new DumpMonitorService();
		JvmMonitorService jvmMonitorService = new JvmMonitorService(settings, threadPool, dumpMonitorService);
		jvmMonitorService.start();
		jvmMonitorService.stop();
		jvmMonitorService.close();
	}

}
