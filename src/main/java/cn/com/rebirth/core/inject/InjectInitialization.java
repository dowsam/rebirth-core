/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core InjectInitialization.java 2012-8-8 11:14:43 l.xue.nong$$
 */
package cn.com.rebirth.core.inject;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cn.com.rebirth.commons.Initialization;
import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.commons.utils.ClassResolverUtils.AbstractFindCallback;
import cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback;
import cn.com.rebirth.commons.utils.ResolverUtils;

/**
 * The Class InjectInitialization.
 *
 * @author l.xue.nong
 */
public final class InjectInitialization implements Initialization {

	/** The businesses. */
	private List<Business> businesses;

	/** The injector. */
	private Injector injector;
	/** The find callback. */
	protected static FindCallback<Business> findCallback = new AbstractFindCallback<Business>() {

		@Override
		protected void doFindType(ResolverUtils<Business> resolverUtils, Class<Business> entityClass) {
			resolverUtils.findImplementations(entityClass, StringUtils.EMPTY);
		}

	};

	/**
	 * Instantiates a new inject initialization.
	 */
	protected InjectInitialization() {
		super();
		this.businesses = ClassResolverUtils.find(findCallback);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Initialization#init()
	 */
	@Override
	public void init() throws RebirthException {
		List<AbstractBusiness> abstractBusinesses = Lists.newArrayList();
		ModulesBuilder modulesBuilder = new ModulesBuilder();
		for (Business business : businesses) {
			business.toModules(modulesBuilder);
			if (business instanceof AbstractBusiness) {
				abstractBusinesses.add((AbstractBusiness) business);
			}
		}
		this.injector = modulesBuilder.createInjector();
		for (AbstractBusiness business : abstractBusinesses) {
			business.toAfterConfigure(getInjector());
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Initialization#stop()
	 */
	@Override
	public void stop() throws RebirthException {
	}

	/**
	 * Gets the injector.
	 *
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * Sets the injector.
	 *
	 * @param injector the new injector
	 */
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	/**
	 * Injector.
	 *
	 * @return the injector
	 */
	public static Injector injector() {
		return RebirthContainer.getInstance().get(InjectInitialization.class).getInjector();
	}
}
