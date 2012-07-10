/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core RebirthCoreVersion.java 2012-7-10 15:46:59 l.xue.nong$$
 */
package cn.com.rebirth.core;

import cn.com.rebirth.commons.Version;

/**
 * The Class RebirthCoreVersion.
 *
 * @author l.xue.nong
 */
public class RebirthCoreVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 817306910476474378L;

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.Version#getModuleVersion()
	 */
	@Override
	public String getModuleVersion() {
		return "0.0.1.RELEASE";
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "rebirth-Core";
	}

}
