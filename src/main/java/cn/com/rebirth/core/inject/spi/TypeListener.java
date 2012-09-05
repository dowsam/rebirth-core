/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeListener.java 2012-7-6 10:23:44 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import cn.com.rebirth.core.inject.TypeLiteral;


/**
 * The listener interface for receiving type events.
 * The class that is interested in processing a type
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTypeListener<code> method. When
 * the type event occurs, that object's appropriate
 * method is invoked.
 *
 * @see TypeEvent
 */
public interface TypeListener {

	
	/**
	 * Hear.
	 *
	 * @param <I> the generic type
	 * @param type the type
	 * @param encounter the encounter
	 */
	<I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter);

}
