/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MoreTypes.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


/**
 * The Class MoreTypes.
 *
 * @author l.xue.nong
 */
public class MoreTypes {

	
	/** The Constant EMPTY_TYPE_ARRAY. */
	public static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};

	
	/**
	 * Instantiates a new more types.
	 */
	private MoreTypes() {
	}

	
	/** The Constant PRIMITIVE_TO_WRAPPER. */
	private static final Map<TypeLiteral<?>, TypeLiteral<?>> PRIMITIVE_TO_WRAPPER = new ImmutableMap.Builder<TypeLiteral<?>, TypeLiteral<?>>()
			.put(TypeLiteral.get(boolean.class), TypeLiteral.get(Boolean.class))
			.put(TypeLiteral.get(byte.class), TypeLiteral.get(Byte.class))
			.put(TypeLiteral.get(short.class), TypeLiteral.get(Short.class))
			.put(TypeLiteral.get(int.class), TypeLiteral.get(Integer.class))
			.put(TypeLiteral.get(long.class), TypeLiteral.get(Long.class))
			.put(TypeLiteral.get(float.class), TypeLiteral.get(Float.class))
			.put(TypeLiteral.get(double.class), TypeLiteral.get(Double.class))
			.put(TypeLiteral.get(char.class), TypeLiteral.get(Character.class))
			.put(TypeLiteral.get(void.class), TypeLiteral.get(Void.class)).build();

	
	/**
	 * Make key safe.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @return the type literal
	 */
	public static <T> TypeLiteral<T> makeKeySafe(TypeLiteral<T> type) {
		if (!isFullySpecified(type.getType())) {
			String message = type + " cannot be used as a key; It is not fully specified.";
			throw new ConfigurationException(ImmutableSet.of(new Message(message)));
		}

		@SuppressWarnings("unchecked")
		TypeLiteral<T> wrappedPrimitives = (TypeLiteral<T>) PRIMITIVE_TO_WRAPPER.get(type);
		return wrappedPrimitives != null ? wrappedPrimitives : type;
	}

	
	/**
	 * Checks if is fully specified.
	 *
	 * @param type the type
	 * @return true, if is fully specified
	 */
	private static boolean isFullySpecified(Type type) {
		if (type instanceof Class) {
			return true;

		} else if (type instanceof CompositeType) {
			return ((CompositeType) type).isFullySpecified();

		} else if (type instanceof TypeVariable) {
			return false;

		} else {
			return ((CompositeType) canonicalize(type)).isFullySpecified();
		}
	}

	
	/**
	 * Canonicalize.
	 *
	 * @param type the type
	 * @return the type
	 */
	public static Type canonicalize(Type type) {
		if (type instanceof ParameterizedTypeImpl || type instanceof GenericArrayTypeImpl
				|| type instanceof WildcardTypeImpl) {
			return type;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());

		} else if (type instanceof GenericArrayType) {
			GenericArrayType g = (GenericArrayType) type;
			return new GenericArrayTypeImpl(g.getGenericComponentType());

		} else if (type instanceof Class && ((Class<?>) type).isArray()) {
			Class<?> c = (Class<?>) type;
			return new GenericArrayTypeImpl(c.getComponentType());

		} else if (type instanceof WildcardType) {
			WildcardType w = (WildcardType) type;
			return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

		} else {
			
			return type;
		}
	}

	
	/**
	 * Serializable copy.
	 *
	 * @param member the member
	 * @return the member
	 */
	public static Member serializableCopy(Member member) {
		return member instanceof MemberImpl ? member : new MemberImpl(member);
	}

	
	/**
	 * Gets the raw type.
	 *
	 * @param type the type
	 * @return the raw type
	 */
	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			
			return (Class<?>) type;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			
			
			
			Type rawType = parameterizedType.getRawType();
			checkArgument(rawType instanceof Class, "Expected a Class, but <%s> is of type %s", type, type.getClass()
					.getName());
			return (Class<?>) rawType;

		} else if (type instanceof GenericArrayType) {
			return Object[].class;

		} else if (type instanceof TypeVariable) {
			
			
			return Object.class;

		} else {
			throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <"
					+ type + "> is of type " + type.getClass().getName());
		}
	}

	
	/**
	 * Equals.
	 *
	 * @param a the a
	 * @param b the b
	 * @return true, if successful
	 */
	@SuppressWarnings("rawtypes")
	public static boolean equals(Type a, Type b) {
		if (a == b) {
			
			return true;

		} else if (a instanceof Class) {
			
			return a.equals(b);

		} else if (a instanceof ParameterizedType) {
			if (!(b instanceof ParameterizedType)) {
				return false;
			}

			ParameterizedType pa = (ParameterizedType) a;
			ParameterizedType pb = (ParameterizedType) b;
			return Objects.equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType())
					&& Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

		} else if (a instanceof GenericArrayType) {
			if (!(b instanceof GenericArrayType)) {
				return false;
			}

			GenericArrayType ga = (GenericArrayType) a;
			GenericArrayType gb = (GenericArrayType) b;
			return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

		} else if (a instanceof WildcardType) {
			if (!(b instanceof WildcardType)) {
				return false;
			}

			WildcardType wa = (WildcardType) a;
			WildcardType wb = (WildcardType) b;
			return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
					&& Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

		} else if (a instanceof TypeVariable) {
			if (!(b instanceof TypeVariable)) {
				return false;
			}
			TypeVariable<?> va = (TypeVariable) a;
			TypeVariable<?> vb = (TypeVariable) b;
			return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());

		} else {
			
			return false;
		}
	}

	
	/**
	 * Hash code.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int hashCode(Type type) {
		if (type instanceof Class) {
			
			return type.hashCode();

		} else if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			return Arrays.hashCode(p.getActualTypeArguments()) ^ p.getRawType().hashCode()
					^ hashCodeOrZero(p.getOwnerType());

		} else if (type instanceof GenericArrayType) {
			return hashCode(((GenericArrayType) type).getGenericComponentType());

		} else if (type instanceof WildcardType) {
			WildcardType w = (WildcardType) type;
			return Arrays.hashCode(w.getLowerBounds()) ^ Arrays.hashCode(w.getUpperBounds());

		} else {
			
			return hashCodeOrZero(type);
		}
	}

	
	/**
	 * Hash code or zero.
	 *
	 * @param o the o
	 * @return the int
	 */
	private static int hashCodeOrZero(Object o) {
		return o != null ? o.hashCode() : 0;
	}

	
	/**
	 * To string.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String toString(Type type) {
		if (type instanceof Class<?>) {
			return ((Class<?>) type).getName();

		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type[] arguments = parameterizedType.getActualTypeArguments();
			Type ownerType = parameterizedType.getOwnerType();
			StringBuilder stringBuilder = new StringBuilder();
			if (ownerType != null) {
				stringBuilder.append(toString(ownerType)).append(".");
			}
			stringBuilder.append(toString(parameterizedType.getRawType()));
			if (arguments.length > 0) {
				stringBuilder.append("<").append(toString(arguments[0]));
				for (int i = 1; i < arguments.length; i++) {
					stringBuilder.append(", ").append(toString(arguments[i]));
				}
			}
			return stringBuilder.append(">").toString();

		} else if (type instanceof GenericArrayType) {
			return toString(((GenericArrayType) type).getGenericComponentType()) + "[]";

		} else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Type[] lowerBounds = wildcardType.getLowerBounds();
			Type[] upperBounds = wildcardType.getUpperBounds();

			if (upperBounds.length != 1 || lowerBounds.length > 1) {
				throw new UnsupportedOperationException("Unsupported wildcard type " + type);
			}

			if (lowerBounds.length == 1) {
				if (upperBounds[0] != Object.class) {
					throw new UnsupportedOperationException("Unsupported wildcard type " + type);
				}
				return "? super " + toString(lowerBounds[0]);
			} else if (upperBounds[0] == Object.class) {
				return "?";
			} else {
				return "? extends " + toString(upperBounds[0]);
			}

		} else {
			return type.toString();
		}
	}

	
	/**
	 * Member type.
	 *
	 * @param member the member
	 * @return the class<? extends member>
	 */
	public static Class<? extends Member> memberType(Member member) {
		checkNotNull(member, "member");

		if (member instanceof MemberImpl) {
			return ((MemberImpl) member).memberType;

		} else if (member instanceof Field) {
			return Field.class;

		} else if (member instanceof Method) {
			return Method.class;

		} else if (member instanceof Constructor) {
			return Constructor.class;

		} else {
			throw new IllegalArgumentException("Unsupported implementation class for Member, " + member.getClass());
		}
	}

	
	/**
	 * To string.
	 *
	 * @param member the member
	 * @return the string
	 */
	public static String toString(Member member) {
		Class<? extends Member> memberType = memberType(member);

		if (memberType == Method.class) {
			return member.getDeclaringClass().getName() + "." + member.getName() + "()";
		} else if (memberType == Field.class) {
			return member.getDeclaringClass().getName() + "." + member.getName();
		} else if (memberType == Constructor.class) {
			return member.getDeclaringClass().getName() + ".<init>()";
		} else {
			throw new AssertionError();
		}
	}

	
	/**
	 * Member key.
	 *
	 * @param member the member
	 * @return the string
	 */
	public static String memberKey(Member member) {
		checkNotNull(member, "member");

		return "<NO_MEMBER_KEY>";
	}

	
	/**
	 * Gets the generic supertype.
	 *
	 * @param type the type
	 * @param rawType the raw type
	 * @param toResolve the to resolve
	 * @return the generic supertype
	 */
	public static Type getGenericSupertype(Type type, Class<?> rawType, Class<?> toResolve) {
		if (toResolve == rawType) {
			return type;
		}

		
		if (toResolve.isInterface()) {
			Class<?>[] interfaces = rawType.getInterfaces();
			for (int i = 0, length = interfaces.length; i < length; i++) {
				if (interfaces[i] == toResolve) {
					return rawType.getGenericInterfaces()[i];
				} else if (toResolve.isAssignableFrom(interfaces[i])) {
					return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
				}
			}
		}

		
		if (!rawType.isInterface()) {
			while (rawType != Object.class) {
				Class<?> rawSupertype = rawType.getSuperclass();
				if (rawSupertype == toResolve) {
					return rawType.getGenericSuperclass();
				} else if (toResolve.isAssignableFrom(rawSupertype)) {
					return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
				}
				rawType = rawSupertype;
			}
		}

		
		return toResolve;
	}

	
	/**
	 * Resolve type variable.
	 *
	 * @param type the type
	 * @param rawType the raw type
	 * @param unknown the unknown
	 * @return the type
	 */
	public static Type resolveTypeVariable(Type type, Class<?> rawType, TypeVariable<?> unknown) {
		Class<?> declaredByRaw = declaringClassOf(unknown);

		
		if (declaredByRaw == null) {
			return unknown;
		}

		Type declaredBy = getGenericSupertype(type, rawType, declaredByRaw);
		if (declaredBy instanceof ParameterizedType) {
			int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
			return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
		}

		return unknown;
	}

	
	/**
	 * Index of.
	 *
	 * @param array the array
	 * @param toFind the to find
	 * @return the int
	 */
	private static int indexOf(Object[] array, Object toFind) {
		for (int i = 0; i < array.length; i++) {
			if (toFind.equals(array[i])) {
				return i;
			}
		}
		throw new NoSuchElementException();
	}

	
	/**
	 * Declaring class of.
	 *
	 * @param typeVariable the type variable
	 * @return the class
	 */
	private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
		GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
		return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
	}

	
	/**
	 * The Class ParameterizedTypeImpl.
	 *
	 * @author l.xue.nong
	 */
	public static class ParameterizedTypeImpl implements ParameterizedType, Serializable, CompositeType {

		
		/** The owner type. */
		private final Type ownerType;

		
		/** The raw type. */
		private final Type rawType;

		
		/** The type arguments. */
		private final Type[] typeArguments;

		
		/**
		 * Instantiates a new parameterized type impl.
		 *
		 * @param ownerType the owner type
		 * @param rawType the raw type
		 * @param typeArguments the type arguments
		 */
		public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
			
			if (rawType instanceof Class<?>) {
				Class<?> rawTypeAsClass = (Class<?>) rawType;
				checkArgument(ownerType != null || rawTypeAsClass.getEnclosingClass() == null,
						"No owner type for enclosed %s", rawType);
				checkArgument(ownerType == null || rawTypeAsClass.getEnclosingClass() != null,
						"Owner type for unenclosed %s", rawType);
			}

			this.ownerType = ownerType == null ? null : canonicalize(ownerType);
			this.rawType = canonicalize(rawType);
			this.typeArguments = typeArguments.clone();
			for (int t = 0; t < this.typeArguments.length; t++) {
				checkNotNull(this.typeArguments[t], "type parameter");
				checkNotPrimitive(this.typeArguments[t], "type parameters");
				this.typeArguments[t] = canonicalize(this.typeArguments[t]);
			}
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
		 */
		public Type[] getActualTypeArguments() {
			return typeArguments.clone();
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.ParameterizedType#getRawType()
		 */
		public Type getRawType() {
			return rawType;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.ParameterizedType#getOwnerType()
		 */
		public Type getOwnerType() {
			return ownerType;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.MoreTypes.CompositeType#isFullySpecified()
		 */
		public boolean isFullySpecified() {
			if (ownerType != null && !MoreTypes.isFullySpecified(ownerType)) {
				return false;
			}

			if (!MoreTypes.isFullySpecified(rawType)) {
				return false;
			}

			for (Type type : typeArguments) {
				if (!MoreTypes.isFullySpecified(type)) {
					return false;
				}
			}

			return true;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof ParameterizedType && MoreTypes.equals(this, (ParameterizedType) other);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return MoreTypes.hashCode(this);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return MoreTypes.toString(this);
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * The Class GenericArrayTypeImpl.
	 *
	 * @author l.xue.nong
	 */
	public static class GenericArrayTypeImpl implements GenericArrayType, Serializable, CompositeType {

		
		/** The component type. */
		private final Type componentType;

		
		/**
		 * Instantiates a new generic array type impl.
		 *
		 * @param componentType the component type
		 */
		public GenericArrayTypeImpl(Type componentType) {
			this.componentType = canonicalize(componentType);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.GenericArrayType#getGenericComponentType()
		 */
		public Type getGenericComponentType() {
			return componentType;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.MoreTypes.CompositeType#isFullySpecified()
		 */
		public boolean isFullySpecified() {
			return MoreTypes.isFullySpecified(componentType);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			return o instanceof GenericArrayType && MoreTypes.equals(this, (GenericArrayType) o);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return MoreTypes.hashCode(this);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return MoreTypes.toString(this);
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * The Class WildcardTypeImpl.
	 *
	 * @author l.xue.nong
	 */
	public static class WildcardTypeImpl implements WildcardType, Serializable, CompositeType {

		
		/** The upper bound. */
		private final Type upperBound;

		
		/** The lower bound. */
		private final Type lowerBound;

		
		/**
		 * Instantiates a new wildcard type impl.
		 *
		 * @param upperBounds the upper bounds
		 * @param lowerBounds the lower bounds
		 */
		public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
			checkArgument(lowerBounds.length <= 1, "Must have at most one lower bound.");
			checkArgument(upperBounds.length == 1, "Must have exactly one upper bound.");

			if (lowerBounds.length == 1) {
				checkNotNull(lowerBounds[0], "lowerBound");
				checkNotPrimitive(lowerBounds[0], "wildcard bounds");
				checkArgument(upperBounds[0] == Object.class, "bounded both ways");
				this.lowerBound = canonicalize(lowerBounds[0]);
				this.upperBound = Object.class;

			} else {
				checkNotNull(upperBounds[0], "upperBound");
				checkNotPrimitive(upperBounds[0], "wildcard bounds");
				this.lowerBound = null;
				this.upperBound = canonicalize(upperBounds[0]);
			}
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.WildcardType#getUpperBounds()
		 */
		public Type[] getUpperBounds() {
			return new Type[] { upperBound };
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.WildcardType#getLowerBounds()
		 */
		public Type[] getLowerBounds() {
			return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.internal.MoreTypes.CompositeType#isFullySpecified()
		 */
		public boolean isFullySpecified() {
			return MoreTypes.isFullySpecified(upperBound)
					&& (lowerBound == null || MoreTypes.isFullySpecified(lowerBound));
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			return other instanceof WildcardType && MoreTypes.equals(this, (WildcardType) other);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return MoreTypes.hashCode(this);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return MoreTypes.toString(this);
		}

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 0;
	}

	
	/**
	 * Check not primitive.
	 *
	 * @param type the type
	 * @param use the use
	 */
	private static void checkNotPrimitive(Type type, String use) {
		checkArgument(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive(),
				"Primitive types are not allowed in %s: %s", use, type);
	}

	
	/**
	 * The Class MemberImpl.
	 *
	 * @author l.xue.nong
	 */
	public static class MemberImpl implements Member, Serializable {

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6473198860131398958L;

		
		/** The declaring class. */
		private final Class<?> declaringClass;

		
		/** The name. */
		private final String name;

		
		/** The modifiers. */
		private final int modifiers;

		
		/** The synthetic. */
		private final boolean synthetic;

		
		/** The member type. */
		private final Class<? extends Member> memberType;

		
		/**
		 * Instantiates a new member impl.
		 *
		 * @param member the member
		 */
		private MemberImpl(Member member) {
			this.declaringClass = member.getDeclaringClass();
			this.name = member.getName();
			this.modifiers = member.getModifiers();
			this.synthetic = member.isSynthetic();
			this.memberType = memberType(member);
			memberKey(member);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.Member#getDeclaringClass()
		 */
		public Class<?> getDeclaringClass() {
			return declaringClass;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.Member#getName()
		 */
		public String getName() {
			return name;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.Member#getModifiers()
		 */
		public int getModifiers() {
			return modifiers;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.reflect.Member#isSynthetic()
		 */
		public boolean isSynthetic() {
			return synthetic;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return MoreTypes.toString(this);
		}
	}

	
	/**
	 * The Interface CompositeType.
	 *
	 * @author l.xue.nong
	 */
	private interface CompositeType {

		
		/**
		 * Checks if is fully specified.
		 *
		 * @return true, if is fully specified
		 */
		boolean isFullySpecified();
	}
}
