/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Types.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.internal.MoreTypes;
import cn.com.rebirth.core.inject.internal.MoreTypes.GenericArrayTypeImpl;
import cn.com.rebirth.core.inject.internal.MoreTypes.ParameterizedTypeImpl;
import cn.com.rebirth.core.inject.internal.MoreTypes.WildcardTypeImpl;


/**
 * The Class Types.
 *
 * @author l.xue.nong
 */
public final class Types {

	
	/**
	 * Instantiates a new types.
	 */
	private Types() {
	}

	
	/**
	 * New parameterized type.
	 *
	 * @param rawType the raw type
	 * @param typeArguments the type arguments
	 * @return the parameterized type
	 */
	public static ParameterizedType newParameterizedType(Type rawType, Type... typeArguments) {
		return newParameterizedTypeWithOwner(null, rawType, typeArguments);
	}

	
	/**
	 * New parameterized type with owner.
	 *
	 * @param ownerType the owner type
	 * @param rawType the raw type
	 * @param typeArguments the type arguments
	 * @return the parameterized type
	 */
	public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
		return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
	}

	
	/**
	 * Array of.
	 *
	 * @param componentType the component type
	 * @return the generic array type
	 */
	public static GenericArrayType arrayOf(Type componentType) {
		return new GenericArrayTypeImpl(componentType);
	}

	
	/**
	 * Subtype of.
	 *
	 * @param bound the bound
	 * @return the wildcard type
	 */
	public static WildcardType subtypeOf(Type bound) {
		return new WildcardTypeImpl(new Type[] { bound }, MoreTypes.EMPTY_TYPE_ARRAY);
	}

	
	/**
	 * Supertype of.
	 *
	 * @param bound the bound
	 * @return the wildcard type
	 */
	public static WildcardType supertypeOf(Type bound) {
		return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { bound });
	}

	
	/**
	 * List of.
	 *
	 * @param elementType the element type
	 * @return the parameterized type
	 */
	public static ParameterizedType listOf(Type elementType) {
		return newParameterizedType(List.class, elementType);
	}

	
	/**
	 * Sets the of.
	 *
	 * @param elementType the element type
	 * @return the parameterized type
	 */
	public static ParameterizedType setOf(Type elementType) {
		return newParameterizedType(Set.class, elementType);
	}

	
	/**
	 * Map of.
	 *
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the parameterized type
	 */
	public static ParameterizedType mapOf(Type keyType, Type valueType) {
		return newParameterizedType(Map.class, keyType, valueType);
	}

	

	
	/**
	 * Provider of.
	 *
	 * @param providedType the provided type
	 * @return the parameterized type
	 */
	public static ParameterizedType providerOf(Type providedType) {
		return newParameterizedType(Provider.class, providedType);
	}
}