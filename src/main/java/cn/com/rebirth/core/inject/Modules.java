/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Modules.java 2012-7-6 10:23:44 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.Constructor;

import cn.com.rebirth.commons.Nullable;
import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.settings.Settings;


/**
 * The Class Modules.
 *
 * @author l.xue.nong
 */
public class Modules {

	
	/**
	 * Creates the module.
	 *
	 * @param moduleClass the module class
	 * @param settings the settings
	 * @return the module
	 * @throws ClassNotFoundException the class not found exception
	 */
	@SuppressWarnings("unchecked")
	public static Module createModule(String moduleClass, Settings settings) throws ClassNotFoundException {
		return createModule((Class<? extends Module>) settings.getClassLoader().loadClass(moduleClass), settings);
	}

	
	/**
	 * Creates the module.
	 *
	 * @param moduleClass the module class
	 * @param settings the settings
	 * @return the module
	 */
	public static Module createModule(Class<? extends Module> moduleClass, @Nullable Settings settings) {
		Constructor<? extends Module> constructor;
		try {
			constructor = moduleClass.getConstructor(Settings.class);
			try {
				return constructor.newInstance(settings);
			} catch (Exception e) {
				throw new RebirthException("Failed to create module [" + moduleClass + "]", e);
			}
		} catch (NoSuchMethodException e) {
			try {
				constructor = moduleClass.getConstructor();
				try {
					return constructor.newInstance();
				} catch (Exception e1) {
					throw new RebirthException("Failed to create module [" + moduleClass + "]", e);
				}
			} catch (NoSuchMethodException e1) {
				throw new RebirthException("No constructor for [" + moduleClass + "]");
			}
		}
	}

	
	/**
	 * Process modules.
	 *
	 * @param modules the modules
	 */
	public static void processModules(Iterable<Module> modules) {
		for (Module module : modules) {
			if (module instanceof PreProcessModule) {
				for (Module module1 : modules) {
					((PreProcessModule) module).processModule(module1);
				}
			}
		}
	}
}
