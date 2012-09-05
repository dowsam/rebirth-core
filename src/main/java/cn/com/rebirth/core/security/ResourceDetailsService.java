/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core ResourceDetailsService.java 2012-7-19 16:01:22 l.xue.nong$$
 */
package cn.com.rebirth.core.security;

import java.util.LinkedHashMap;

import cn.com.rebirth.commons.exception.RebirthException;

/**
 * The Interface ResourceDetailsService.
 *
 * @author l.xue.nong
 */
public interface ResourceDetailsService {

	/**
	 * Gets the request map.
	 *
	 * @return the request map
	 * @throws Exception the exception
	 */
	public LinkedHashMap<String, String> getRequestMap() throws RebirthException;
}
