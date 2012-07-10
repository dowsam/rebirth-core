/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core Html5TemplateEngine.java 2012-7-10 15:46:36 l.xue.nong$$
 */
package cn.com.rebirth.core.template;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.context.Context;

/**
 * The Class Html5TemplateEngine.
 *
 * @author l.xue.nong
 */
public class Html5TemplateEngine implements TemplateEngine {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.template.TemplateEngine#renderFile(java.lang.String, java.util.Map)
	 */
	@Override
	public String renderFile(String templateName, Map<String, Object> param) {
		org.thymeleaf.TemplateEngine engine = new org.thymeleaf.TemplateEngine();
		Context context = new Context(Locale.CHINA, param);
		return engine.process(templateName, context);
	}

}
