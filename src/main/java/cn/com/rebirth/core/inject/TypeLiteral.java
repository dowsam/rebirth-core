/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeLiteral.java 2012-7-6 10:23:41 l.xue.nong$$
 */

package cn.com.rebirth.core.inject;

import static cn.com.rebirth.commons.Preconditions.checkArgument;
import static cn.com.rebirth.commons.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import cn.com.rebirth.core.inject.internal.MoreTypes;
import cn.com.rebirth.core.inject.util.Types;

import com.google.common.collect.ImmutableList;

/**
 * The Class TypeLiteral.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class TypeLiteral<T> {

	/** The raw type. */
	final Class<? super T> rawType;

	/** The type. */
	final Type type;

	/** The hash code. */
	final int hashCode;

	/**
	 * Instantiates a new type literal.
	 */
	@SuppressWarnings("unchecked")
	protected TypeLiteral() {
		this.type = getSuperclassTypeParameter(getClass());
		this.rawType = (Class<? super T>) MoreTypes.getRawType(type);
		this.hashCode = MoreTypes.hashCode(type);
	}

	/**
	 * Instantiates a new type literal.
	 *
	 * @param type the type
	 */
	@SuppressWarnings("unchecked")
	TypeLiteral(Type type) {
		this.type = MoreTypes.canonicalize(checkNotNull(type, "type"));
		this.rawType = (Class<? super T>) MoreTypes.getRawType(this.type);
		this.hashCode = MoreTypes.hashCode(this.type);
	}

	/**
	 * Gets the superclass type parameter.
	 *
	 * @param subclass the subclass
	 * @return the superclass type parameter
	 */
	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return MoreTypes.canonicalize(parameterized.getActualTypeArguments()[0]);
	}

	/**
	 * From superclass type parameter.
	 *
	 * @param subclass the subclass
	 * @return the type literal
	 */
	static TypeLiteral<?> fromSuperclassTypeParameter(Class<?> subclass) {
		return new TypeLiteral<Object>(getSuperclassTypeParameter(subclass));
	}

	/**
	 * Gets the raw type.
	 *
	 * @return the raw type
	 */
	public final Class<? super T> getRawType() {
		return rawType;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public final Type getType() {
		return type;
	}

	/**
	 * Provider type.
	 *
	 * @return the type literal
	 */
	@SuppressWarnings("unchecked")
	final TypeLiteral<Provider<T>> providerType() {

		return (TypeLiteral<Provider<T>>) get(Types.providerOf(getType()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final boolean equals(Object o) {
		return o instanceof TypeLiteral<?> && MoreTypes.equals(type, ((TypeLiteral) o).type);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return MoreTypes.toString(type);
	}

	/**
	 * Gets the.
	 *
	 * @param type the type
	 * @return the type literal
	 */
	public static TypeLiteral<?> get(Type type) {
		return new TypeLiteral<Object>(type);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the type literal
	 */
	public static <T> TypeLiteral<T> get(Class<T> type) {
		return new TypeLiteral<T>(type);
	}

	/**
	 * Resolve all.
	 *
	 * @param types the types
	 * @return the list
	 */
	private List<TypeLiteral<?>> resolveAll(Type[] types) {
		TypeLiteral<?>[] result = new TypeLiteral<?>[types.length];
		for (int t = 0; t < types.length; t++) {
			result[t] = resolve(types[t]);
		}
		return ImmutableList.copyOf(result);
	}

	/**
	 * Resolve.
	 *
	 * @param toResolve the to resolve
	 * @return the type literal
	 */
	TypeLiteral<?> resolve(Type toResolve) {
		return TypeLiteral.get(resolveType(toResolve));
	}

	/**
	 * Resolve type.
	 *
	 * @param toResolve the to resolve
	 * @return the type
	 */
	@SuppressWarnings("rawtypes")
	Type resolveType(Type toResolve) {

		while (true) {
			if (toResolve instanceof TypeVariable) {
				TypeVariable original = (TypeVariable) toResolve;
				toResolve = MoreTypes.resolveTypeVariable(type, rawType, original);
				if (toResolve == original) {
					return toResolve;
				}

			} else if (toResolve instanceof GenericArrayType) {
				GenericArrayType original = (GenericArrayType) toResolve;
				Type componentType = original.getGenericComponentType();
				Type newComponentType = resolveType(componentType);
				return componentType == newComponentType ? original : Types.arrayOf(newComponentType);

			} else if (toResolve instanceof ParameterizedType) {
				ParameterizedType original = (ParameterizedType) toResolve;
				Type ownerType = original.getOwnerType();
				Type newOwnerType = resolveType(ownerType);
				boolean changed = newOwnerType != ownerType;

				Type[] args = original.getActualTypeArguments();
				for (int t = 0, length = args.length; t < length; t++) {
					Type resolvedTypeArgument = resolveType(args[t]);
					if (resolvedTypeArgument != args[t]) {
						if (!changed) {
							args = args.clone();
							changed = true;
						}
						args[t] = resolvedTypeArgument;
					}
				}

				return changed ? Types.newParameterizedTypeWithOwner(newOwnerType, original.getRawType(), args)
						: original;

			} else if (toResolve instanceof WildcardType) {
				WildcardType original = (WildcardType) toResolve;
				Type[] originalLowerBound = original.getLowerBounds();
				Type[] originalUpperBound = original.getUpperBounds();

				if (originalLowerBound.length == 1) {
					Type lowerBound = resolveType(originalLowerBound[0]);
					if (lowerBound != originalLowerBound[0]) {
						return Types.supertypeOf(lowerBound);
					}
				} else if (originalUpperBound.length == 1) {
					Type upperBound = resolveType(originalUpperBound[0]);
					if (upperBound != originalUpperBound[0]) {
						return Types.subtypeOf(upperBound);
					}
				}
				return original;

			} else {
				return toResolve;
			}
		}
	}

	/**
	 * Gets the supertype.
	 *
	 * @param supertype the supertype
	 * @return the supertype
	 */
	public TypeLiteral<?> getSupertype(Class<?> supertype) {
		checkArgument(supertype.isAssignableFrom(rawType), "%s is not a supertype of %s", supertype, this.type);
		return resolve(MoreTypes.getGenericSupertype(type, rawType, supertype));
	}

	/**
	 * Gets the field type.
	 *
	 * @param field the field
	 * @return the field type
	 */
	public TypeLiteral<?> getFieldType(Field field) {
		checkArgument(field.getDeclaringClass().isAssignableFrom(rawType), "%s is not defined by a supertype of %s",
				field, type);
		return resolve(field.getGenericType());
	}

	/**
	 * Gets the parameter types.
	 *
	 * @param methodOrConstructor the method or constructor
	 * @return the parameter types
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TypeLiteral<?>> getParameterTypes(Member methodOrConstructor) {
		Type[] genericParameterTypes;

		if (methodOrConstructor instanceof Method) {
			Method method = (Method) methodOrConstructor;
			checkArgument(method.getDeclaringClass().isAssignableFrom(rawType),
					"%s is not defined by a supertype of %s", method, type);
			genericParameterTypes = method.getGenericParameterTypes();

		} else if (methodOrConstructor instanceof Constructor) {
			Constructor constructor = (Constructor) methodOrConstructor;
			checkArgument(constructor.getDeclaringClass().isAssignableFrom(rawType),
					"%s does not construct a supertype of %s", constructor, type);
			genericParameterTypes = constructor.getGenericParameterTypes();

		} else {
			throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
		}

		return resolveAll(genericParameterTypes);
	}

	/**
	 * Gets the exception types.
	 *
	 * @param methodOrConstructor the method or constructor
	 * @return the exception types
	 */
	public List<TypeLiteral<?>> getExceptionTypes(Member methodOrConstructor) {
		Type[] genericExceptionTypes;

		if (methodOrConstructor instanceof Method) {
			Method method = (Method) methodOrConstructor;
			checkArgument(method.getDeclaringClass().isAssignableFrom(rawType),
					"%s is not defined by a supertype of %s", method, type);
			genericExceptionTypes = method.getGenericExceptionTypes();

		} else if (methodOrConstructor instanceof Constructor) {
			Constructor<?> constructor = (Constructor<?>) methodOrConstructor;
			checkArgument(constructor.getDeclaringClass().isAssignableFrom(rawType),
					"%s does not construct a supertype of %s", constructor, type);
			genericExceptionTypes = constructor.getGenericExceptionTypes();

		} else {
			throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
		}

		return resolveAll(genericExceptionTypes);
	}

	/**
	 * Gets the return type.
	 *
	 * @param method the method
	 * @return the return type
	 */
	public TypeLiteral<?> getReturnType(Method method) {
		checkArgument(method.getDeclaringClass().isAssignableFrom(rawType), "%s is not defined by a supertype of %s",
				method, type);
		return resolve(method.getGenericReturnType());
	}
}
