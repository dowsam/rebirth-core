/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ParameterListKey.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The Class ParameterListKey.
 *
 * @author l.xue.nong
 */
class ParameterListKey {

	
	/** The param list. */
	private final List<Type> paramList;

	
	/**
	 * Instantiates a new parameter list key.
	 *
	 * @param paramList the param list
	 */
	public ParameterListKey(List<Type> paramList) {
		this.paramList = new ArrayList<Type>(paramList);
	}

	
	/**
	 * Instantiates a new parameter list key.
	 *
	 * @param types the types
	 */
	public ParameterListKey(Type[] types) {
		this(Arrays.asList(types));
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ParameterListKey)) {
			return false;
		}
		ParameterListKey other = (ParameterListKey) o;
		return paramList.equals(other.paramList);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return paramList.hashCode();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return paramList.toString();
	}
}