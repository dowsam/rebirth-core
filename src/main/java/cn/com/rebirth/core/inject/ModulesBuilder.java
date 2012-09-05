/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ModulesBuilder.java 2012-7-6 10:23:50 l.xue.nong$$
 */

package cn.com.rebirth.core.inject;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * The Class ModulesBuilder.
 *
 * @author l.xue.nong
 */
public class ModulesBuilder implements Iterable<Module> {

	/** The modules. */
	private final List<Module> modules = Lists.newArrayList();

	/**
	 * Adds the.
	 *
	 * @param modules the modules
	 * @return the modules builder
	 */
	public ModulesBuilder add(Module... modules) {
		for (Module module : modules) {
			add(module);
		}
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param module the module
	 * @return the modules builder
	 */
	public ModulesBuilder add(Module module) {
		modules.add(module);
		if (module instanceof SpawnModules) {
			Iterable<? extends Module> spawned = ((SpawnModules) module).spawnModules();
			for (Module spawn : spawned) {
				add(spawn);
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Module> iterator() {
		return modules.iterator();
	}

	/**
	 * Creates the injector.
	 *
	 * @return the injector
	 */
	public Injector createInjector() {
		Modules.processModules(modules);
		Injector injector = Guice.createInjector(modules);
		Injectors.cleanCaches(injector);
		return injector;
	}

	/**
	 * Creates the child injector.
	 *
	 * @param injector the injector
	 * @return the injector
	 */
	public Injector createChildInjector(Injector injector) {
		Modules.processModules(modules);
		Injector childInjector = injector.createChildInjector(modules);
		Injectors.cleanCaches(childInjector);
		return childInjector;
	}
}
