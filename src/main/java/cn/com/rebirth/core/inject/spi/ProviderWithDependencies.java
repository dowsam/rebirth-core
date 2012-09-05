/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProviderWithDependencies.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.Provider;


/**
 * The Interface ProviderWithDependencies.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ProviderWithDependencies<T> extends Provider<T>, HasDependencies {
}
