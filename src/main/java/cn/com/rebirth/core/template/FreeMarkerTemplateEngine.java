/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core FreeMarkerTemplateEngine.java 2012-8-3 7:42:28 l.xue.nong$$
 */
package cn.com.rebirth.core.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletContext;

import cn.com.rebirth.commons.utils.ExceptionUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * The Class FreeMarkerTemplateEngine.
 *
 * @author l.xue.nong
 */
public class FreeMarkerTemplateEngine implements TemplateEngine {

	/** The configuration. */
	private final Configuration configuration;

	/** The servlet context. */
	private ServletContext servletContext;

	/** The path. */
	private String path;

	/**
	 * Instantiates a new free marker template engine.
	 *
	 * @param configuration the configuration
	 */
	public FreeMarkerTemplateEngine(Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * Instantiates a new free marker template engine.
	 *
	 * @param configuration the configuration
	 * @param servletContext the servlet context
	 */
	public FreeMarkerTemplateEngine(Configuration configuration, ServletContext servletContext) {
		this(configuration, servletContext, "/");
	}

	/**
	 * Instantiates a new free marker template engine.
	 *
	 * @param configuration the configuration
	 * @param servletContext the servlet context
	 * @param path the path
	 */
	public FreeMarkerTemplateEngine(Configuration configuration, ServletContext servletContext, String path) {
		super();
		this.configuration = configuration;
		this.servletContext = servletContext;
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.template.TemplateEngine#renderFile(java.lang.String, java.util.Map)
	 */
	@Override
	public String renderFile(String templateName, Map<String, Object> param) throws IOException {
		ServletContext context = (ServletContext) param.get("servletContext");
		if (context != null) {
			this.servletContext = context;
		}
		TemplateLoader templateLoader = createTemplateLoader(templateName);
		configuration.setTemplateLoader(templateLoader);
		StringWriter result = new StringWriter();
		Template template = configuration.getTemplate(templateName);
		try {
			template.process(param, result);
		} catch (TemplateException e) {
			throw ExceptionUtils.unchecked(e);
		}
		return result.toString();
	}

	/**
	 * Creates the template loader.
	 *
	 * @param templatePath the template path
	 * @return the template loader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected TemplateLoader createTemplateLoader(String templatePath) throws IOException {
		if (templatePath.startsWith("class://")) {
			// substring(7) is intentional as we "reuse" the last slash
			return new ClassTemplateLoader(getClass(), templatePath.substring(7));
		} else {
			if (templatePath.startsWith("file://")) {
				templatePath = templatePath.substring(7);
				return new FileTemplateLoader(new File(templatePath));
			} else {
				if (this.servletContext != null && this.path != null) {
					return new WebappTemplateLoader(servletContext, path);
				}
				return new FileTemplateLoader(new File(System.getProperty("user.dir") + this.path));
			}
		}
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
