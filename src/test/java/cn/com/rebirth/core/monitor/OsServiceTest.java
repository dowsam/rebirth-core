package cn.com.rebirth.core.monitor;

import cn.com.rebirth.commons.settings.ImmutableSettings;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.xcontent.ToXContent;
import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentFactory;
import cn.com.rebirth.core.monitor.os.OsInfo;
import cn.com.rebirth.core.monitor.os.OsService;
import cn.com.rebirth.core.monitor.os.SigarOsProbe;
import cn.com.rebirth.core.monitor.sigar.SigarService;

public class OsServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;
		SigarService sigarService = new SigarService(settings);
		OsService osService = new OsService(settings, new SigarOsProbe(settings, sigarService));
		OsInfo osInfo = osService.info();
		println(osInfo);
		println(osService.stats());

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
