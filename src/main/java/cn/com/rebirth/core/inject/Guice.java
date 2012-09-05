/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Guice.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import java.util.Arrays;


/**
 * The Class Guice.
 *
 * @author l.xue.nong
 */
public final class Guice {

    
    /**
     * Instantiates a new guice.
     */
    private Guice() {
    }

    
    /**
     * Creates the injector.
     *
     * @param modules the modules
     * @return the injector
     */
    public static Injector createInjector(Module... modules) {
        return createInjector(Arrays.asList(modules));
    }

    
    /**
     * Creates the injector.
     *
     * @param modules the modules
     * @return the injector
     */
    public static Injector createInjector(Iterable<? extends Module> modules) {
        return createInjector(Stage.DEVELOPMENT, modules);
    }

    
    /**
     * Creates the injector.
     *
     * @param stage the stage
     * @param modules the modules
     * @return the injector
     */
    public static Injector createInjector(Stage stage, Module... modules) {
        return createInjector(stage, Arrays.asList(modules));
    }

    
    /**
     * Creates the injector.
     *
     * @param stage the stage
     * @param modules the modules
     * @return the injector
     */
    public static Injector createInjector(Stage stage,
                                          Iterable<? extends Module> modules) {
        return new InjectorBuilder()
                .stage(stage)
                .addModules(modules)
                .build();
    }
}
