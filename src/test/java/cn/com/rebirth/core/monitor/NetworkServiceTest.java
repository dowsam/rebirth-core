package cn.com.rebirth.core.monitor;

import cn.com.rebirth.commons.settings.ImmutableSettings;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.xcontent.ToXContent;
import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentFactory;
import cn.com.rebirth.core.monitor.network.NetworkInfo;
import cn.com.rebirth.core.monitor.network.NetworkProbe;
import cn.com.rebirth.core.monitor.network.NetworkService;
import cn.com.rebirth.core.monitor.network.SigarNetworkProbe;
import cn.com.rebirth.core.monitor.sigar.SigarService;

public class NetworkServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;
		SigarService sigarService = new SigarService(settings);
		NetworkProbe probe = new SigarNetworkProbe(settings, sigarService);
		NetworkService networkService = new NetworkService(settings, probe);
		NetworkInfo networkInfo = networkService.info();
		println(networkInfo);
		println(networkService.stats());
	}

	private static void println(ToXContent content) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			content.toXContent(builder, ToXContent.EMPTY_PARAMS);
			builder.endObject();
			builder.close();
			System.out.println(builder.string());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
