/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core BlockTag.java 2012-2-12 16:38:09 l.xue.nong$$
 */
package cn.com.rebirth.core.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * The Class BlockTag.
 *
 * @author l.xue.nong
 */
public class BlockTag extends TagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8246166191638588615L;

	/** The name. */
	private String name;

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Do start tag.
	 *
	 * @return EVAL_BODY_INCLUDE or EVAL_BODY_BUFFERED or SKIP_BODY
	 * @throws JspException the jsp exception
	 */
	@Override
	public int doStartTag() throws JspException {
		return getOverriedContent() == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

	/**
	 * Do end tag.
	 *
	 * @return EVAL_PAGE or SKIP_PAGE
	 * @throws JspException the jsp exception
	 */
	@Override
	public int doEndTag() throws JspException {
		String overriedContent = getOverriedContent();
		if (overriedContent == null) {
			return EVAL_PAGE;
		}

		try {
			pageContext.getOut().write(overriedContent);
		} catch (IOException e) {
			throw new JspException("write overridedContent occer IOException,block name:" + name, e);
		}
		return EVAL_PAGE;
	}

	/**
	 * Gets the overried content.
	 *
	 * @return the overried content
	 */
	private String getOverriedContent() {
		String varName = Utils.getOverrideVariableName(name);
		return (String) pageContext.getRequest().getAttribute(varName);
	}
}
