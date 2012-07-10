/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core JerseyClientFactory.java 2012-2-3 10:07:21 l.xue.nong$$
 */
package cn.com.rebirth.core.jersey;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * A factory for creating JerseyClient objects.
 */
public class JerseyClientFactory {

	/**
	 * Instantiates a new jersey client factory.
	 */
	private JerseyClientFactory() {
	}

	/**
	 * 创建JerseyClient, 设定JSON字符串使用Jackson解析.
	 *
	 * @param baseUrl the base url
	 * @return the web resource
	 */
	public static WebResource createClient(String baseUrl) {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		return client.resource(baseUrl);
	}
}
