/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core TemplateEngineFactory.java 2012-8-2 12:38:11 l.xue.nong$$
 */
package cn.com.rebirth.core.template;

import org.apache.commons.io.FilenameUtils;

import cn.com.rebirth.commons.exception.RebirthIllegalArgumentException;
import freemarker.template.Configuration;

/**
 * A factory for creating TemplateEngine objects.
 */
public abstract class TemplateEngineFactory {

	/**
	 * Creates a new TemplateEngine object.
	 *
	 * @param templateName the template name
	 * @return the template engine
	 */
	public static TemplateEngine createTemplateEngine(String templateName) {
		String exName = FilenameUtils.getExtension(templateName);
		if ("flt".equalsIgnoreCase(exName)) {
			Configuration configuration = new Configuration();
			return new FreeMarkerTemplateEngine(configuration);
		} else if ("html".equalsIgnoreCase(exName)) {
			return new Html5TemplateEngine();
		}
		throw new RebirthIllegalArgumentException("Failed to find support for the processing class");
	}

}
