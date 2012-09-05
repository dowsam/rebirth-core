/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core Html5TemplateEngine.java 2012-7-10 15:46:36 l.xue.nong$$
 */
package cn.com.rebirth.core.template;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

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
		ITemplateResolver iTemplateResolver = templateResolver(templateName);
		if (iTemplateResolver instanceof ClassLoaderTemplateResolver) {
			templateName = templateName.substring(8);
		} else if ((iTemplateResolver instanceof FileTemplateResolver)) {
			templateName = templateName.substring(7);
		}
		engine.addTemplateResolver(iTemplateResolver);
		IContext context = buildContext(param);
		return engine.process(templateName, context);
	}

	/**
	 * Template resolver.
	 *
	 * @param templateName the template name
	 * @return the i template resolver
	 */

	protected ITemplateResolver templateResolver(String templateName) {
		if (templateName.startsWith("class://")) {
			// substring(7) is intentional as we "reuse" the last slash
			return new ClassLoaderTemplateResolver();
		} else {
			if (templateName.startsWith("file://")) {
				return new FileTemplateResolver();
			} else {
				return new ServletContextTemplateResolver();
			}
		}
	}

	/**
	 * Builds the context.
	 *
	 * @param param the param
	 * @return the i context
	 */
	protected IContext buildContext(Map<String, Object> param) {
		return new Context(Locale.CHINA, param);
	}

}
