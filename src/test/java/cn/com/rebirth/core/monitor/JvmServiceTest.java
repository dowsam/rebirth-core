package cn.com.rebirth.core.monitor;

import cn.com.rebirth.commons.settings.ImmutableSettings;
import cn.com.rebirth.commons.xcontent.ToXContent;
import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentFactory;
import cn.com.rebirth.core.monitor.jvm.JvmInfo;
import cn.com.rebirth.core.monitor.jvm.JvmService;
import cn.com.rebirth.core.monitor.jvm.JvmStats;

public class JvmServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JvmService jvmService = new JvmService(ImmutableSettings.Builder.EMPTY_SETTINGS);
		JvmInfo jvmInfo = jvmService.info();
		println(jvmInfo);
		JvmStats jvmStats = jvmService.stats();
		println(jvmStats);
	}

	private static void println(ToXContent content) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.prettyPrint();
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
