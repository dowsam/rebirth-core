/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core TemplateEngine.java 2012-7-10 15:36:18 l.xue.nong$$
 */
package cn.com.rebirth.core.template;

import java.io.IOException;
import java.util.Map;

/**
 * The Interface TemplateEngine.
 *
 * @author l.xue.nong
 */
public interface TemplateEngine {

	/**
	 * Render file.
	 *
	 * @param templateName the template name
	 * @param param the param
	 * @return the string
	 */
	public String renderFile(String templateName, Map<String, Object> param) throws IOException;
}
