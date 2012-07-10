/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core OpenEntityManagerInViewFilter.java 2012-7-10 15:46:59 l.xue.nong$$
 */
package cn.com.rebirth.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import cn.com.rebirth.commons.utils.SpringContextHolder;

/**
 * The Class OpenEntityManagerInViewFilter.
 *
 * @author l.xue.nong
 */
public class OpenEntityManagerInViewFilter {

	/** The logger. */
	protected static Log logger = LogFactory.getLog(OpenEntityManagerInViewFilter.class);

	/** The Constant DEFAULT_ENTITY_MANAGER_FACTORY_BEAN_NAME. */
	public static final String DEFAULT_ENTITY_MANAGER_FACTORY_BEAN_NAME = "entityManagerFactory";

	/** The entity manager factory bean name. */
	private String entityManagerFactoryBeanName;

	/** The persistence unit name. */
	private String persistenceUnitName;

	/** The entity manager factory. */
	private volatile EntityManagerFactory entityManagerFactory;

	/** The participate. */
	boolean participate = false;

	/**
	 * Instantiates a new open entity manager in view filter.
	 */
	public OpenEntityManagerInViewFilter() {
		super();
	}

	/**
	 * Instantiates a new open entity manager in view filter.
	 *
	 * @param entityManagerFactory the entity manager factory
	 */
	public OpenEntityManagerInViewFilter(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * Sets the entity manager factory bean name.
	 *
	 * @param entityManagerFactoryBeanName the new entity manager factory bean name
	 */
	public void setEntityManagerFactoryBeanName(String entityManagerFactoryBeanName) {
		this.entityManagerFactoryBeanName = entityManagerFactoryBeanName;
	}

	/**
	 * Gets the entity manager factory bean name.
	 *
	 * @return the entity manager factory bean name
	 */
	protected String getEntityManagerFactoryBeanName() {
		return this.entityManagerFactoryBeanName;
	}

	/**
	 * Sets the persistence unit name.
	 *
	 * @param persistenceUnitName the new persistence unit name
	 */
	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	/**
	 * Gets the persistence unit name.
	 *
	 * @return the persistence unit name
	 */
	protected String getPersistenceUnitName() {
		return this.persistenceUnitName;
	}

	/**
	 * Lookup entity manager factory.
	 *
	 * @param request the request
	 * @return the entity manager factory
	 */
	protected EntityManagerFactory lookupEntityManagerFactory(HttpServletRequest request) {
		if (this.entityManagerFactory == null) {
			this.entityManagerFactory = lookupEntityManagerFactory();
		}
		return this.entityManagerFactory;
	}

	/**
	 * Begin filter.
	 *
	 * @return the entity manager factory
	 */
	public EntityManagerFactory beginFilter() {
		EntityManagerFactory emf = lookupEntityManagerFactory();
		participate = false;

		if (TransactionSynchronizationManager.hasResource(emf)) {
			// Do not modify the EntityManager: just set the participate flag.
			participate = true;
		} else {
			logger.debug("Opening JPA EntityManager in OpenEntityManagerInViewFilter");
			try {
				EntityManager em = createEntityManager(emf);
				TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
			} catch (PersistenceException ex) {
				throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
			}
		}
		return emf;
	}

	/**
	 * End filter.
	 */
	public void endFilter() {
		if (!participate) {
			EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
					.unbindResource(entityManagerFactory);
			logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewFilter");
			EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
		}
	}

	/**
	 * Lookup entity manager factory.
	 *
	 * @return the entity manager factory
	 */
	protected EntityManagerFactory lookupEntityManagerFactory() {
		if (this.entityManagerFactory != null)
			return this.entityManagerFactory;
		ApplicationContext wac = SpringContextHolder.getApplicationContext();
		String emfBeanName = getEntityManagerFactoryBeanName();
		String puName = getPersistenceUnitName();
		if (StringUtils.hasLength(emfBeanName)) {
			return entityManagerFactory = wac.getBean(emfBeanName, EntityManagerFactory.class);
		} else if (!StringUtils.hasLength(puName) && wac.containsBean(DEFAULT_ENTITY_MANAGER_FACTORY_BEAN_NAME)) {
			return entityManagerFactory = wac.getBean(DEFAULT_ENTITY_MANAGER_FACTORY_BEAN_NAME,
					EntityManagerFactory.class);
		} else {
			// Includes fallback search for single EntityManagerFactory bean by type.
			return entityManagerFactory = EntityManagerFactoryUtils.findEntityManagerFactory(wac, puName);
		}
	}

	/**
	 * Creates the entity manager.
	 *
	 * @param emf the emf
	 * @return the entity manager
	 */
	protected EntityManager createEntityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}
}
