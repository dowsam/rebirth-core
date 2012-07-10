/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core MergingPersistenceUnitManager.java 2012-7-10 15:46:59 l.xue.nong$$
 */
package cn.com.rebirth.core;

import java.net.URL;

import javax.persistence.spi.PersistenceUnitInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

/**
 * The Class MergingPersistenceUnitManager.
 *
 * @author l.xue.nong
 */
public class MergingPersistenceUnitManager extends DefaultPersistenceUnitManager {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(MergingPersistenceUnitManager.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager
	 * #
	 * postProcessPersistenceUnitInfo(org.springframework.orm.jpa.persistenceunit
	 * .MutablePersistenceUnitInfo)
	 */
	@Override
	protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {

		// Invoke normal post processing
		super.postProcessPersistenceUnitInfo(pui);

		PersistenceUnitInfo oldPui = getPersistenceUnitInfo(pui.getPersistenceUnitName());

		if (oldPui != null) {
			postProcessPersistenceUnitInfo(pui, oldPui);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager#isPersistenceUnitOverrideAllowed()
	 */
	protected boolean isPersistenceUnitOverrideAllowed() {
		return true;
	}

	/**
	 * Post process persistence unit info.
	 *
	 * @param pui the pui
	 * @param oldPui the old pui
	 */
	void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui, PersistenceUnitInfo oldPui) {

		for (URL url : oldPui.getJarFileUrls()) {

			// Add jar file url to PUI
			if (!pui.getJarFileUrls().contains(url)) {
				log.debug("Adding {} to persistence units", url);
				pui.addJarFileUrl(url);
			}
		}

		for (String className : oldPui.getManagedClassNames()) {

			if (!pui.getManagedClassNames().contains(className)) {
				log.debug("Adding class {} to PersistenceUnit {}", className, pui.getPersistenceUnitName());
				pui.addManagedClassName(className);
			}
		}

		pui.addJarFileUrl(oldPui.getPersistenceUnitRootUrl());
	}
}
