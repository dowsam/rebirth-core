/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons RealElement.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.multibindings;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The Class RealElement.
 *
 * @author l.xue.nong
 */
@SuppressWarnings("all")
class RealElement implements Element {
	
	
	/** The Constant nextUniqueId. */
	private static final AtomicInteger nextUniqueId = new AtomicInteger(1);

	
	/** The unique id. */
	private final int uniqueId;
	
	
	/** The set name. */
	private final String setName;

	
	/**
	 * Instantiates a new real element.
	 *
	 * @param setName the set name
	 */
	RealElement(String setName) {
		uniqueId = nextUniqueId.getAndIncrement();
		this.setName = setName;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.multibindings.Element#setName()
	 */
	public String setName() {
		return setName;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.multibindings.Element#uniqueId()
	 */
	public int uniqueId() {
		return uniqueId;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	public Class<? extends Annotation> annotationType() {
		return Element.class;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "@" + Element.class.getName() + "(setName=" + setName + ",uniqueId=" + uniqueId + ")";
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Element && ((Element) o).setName().equals(setName())
				&& ((Element) o).uniqueId() == uniqueId();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 127 * ("setName".hashCode() ^ setName.hashCode()) + 127 * ("uniqueId".hashCode() ^ uniqueId);
	}
}
