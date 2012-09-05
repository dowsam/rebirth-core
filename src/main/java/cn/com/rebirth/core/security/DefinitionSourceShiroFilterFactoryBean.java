/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core DefinitionSourceShiroFilterFactoryBean.java 2012-7-19 16:24:05 l.xue.nong$$
 */
package cn.com.rebirth.core.security;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

/**
 * The Class DefinitionSourceShiroFilterFactoryBean.
 *
 * @author l.xue.nong
 */
public class DefinitionSourceShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	/** The resource details service. */
	private ResourceDetailsService resourceDetailsService;

	/**
	 * Sets the resource details service.
	 *
	 * @param resourceDetailsService the new resource details service
	 */
	public void setResourceDetailsService(ResourceDetailsService resourceDetailsService) {
		this.resourceDetailsService = resourceDetailsService;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>(); //order matters!
		map.putAll(resourceDetailsService.getRequestMap());
		map.putAll(getFilterChainDefinitionMap());
		setFilterChainDefinitionMap(map);
		return super.getObject();
	}

}
