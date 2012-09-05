/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectionPoint.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.internal.Annotations;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.MoreTypes;
import cn.com.rebirth.core.inject.internal.Nullability;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * The Class InjectionPoint.
 *
 * @author l.xue.nong
 */
public final class InjectionPoint {

	
	/** The optional. */
	private final boolean optional;

	
	/** The member. */
	private final Member member;

	
	/** The dependencies. */
	private final ImmutableList<Dependency<?>> dependencies;

	
	/**
	 * Instantiates a new injection point.
	 *
	 * @param member the member
	 * @param dependencies the dependencies
	 * @param optional the optional
	 */
	@SuppressWarnings("unused")
	private InjectionPoint(Member member, ImmutableList<Dependency<?>> dependencies, boolean optional) {
		this.member = member;
		this.dependencies = dependencies;
		this.optional = optional;
	}

	
	/**
	 * Instantiates a new injection point.
	 *
	 * @param type the type
	 * @param method the method
	 */
	InjectionPoint(TypeLiteral<?> type, Method method) {
		this.member = method;

		Inject inject = method.getAnnotation(Inject.class);
		this.optional = inject.optional();

		this.dependencies = forMember(method, type, method.getParameterAnnotations());
	}

	
	/**
	 * Instantiates a new injection point.
	 *
	 * @param type the type
	 * @param constructor the constructor
	 */
	InjectionPoint(TypeLiteral<?> type, Constructor<?> constructor) {
		this.member = constructor;
		this.optional = false;
		this.dependencies = forMember(constructor, type, constructor.getParameterAnnotations());
	}

	
	/**
	 * Instantiates a new injection point.
	 *
	 * @param type the type
	 * @param field the field
	 */
	InjectionPoint(TypeLiteral<?> type, Field field) {
		this.member = field;

		Inject inject = field.getAnnotation(Inject.class);
		this.optional = inject.optional();

		Annotation[] annotations = field.getAnnotations();

		Errors errors = new Errors(field);
		Key<?> key = null;
		try {
			key = Annotations.getKey(type.getFieldType(field), field, annotations, errors);
		} catch (ErrorsException e) {
			errors.merge(e.getErrors());
		}
		errors.throwConfigurationExceptionIfErrorsExist();

		this.dependencies = ImmutableList
				.<Dependency<?>> of(newDependency(key, Nullability.allowsNull(annotations), -1));
	}

	
	/**
	 * For member.
	 *
	 * @param member the member
	 * @param type the type
	 * @param paramterAnnotations the paramter annotations
	 * @return the immutable list
	 */
	private ImmutableList<Dependency<?>> forMember(Member member, TypeLiteral<?> type,
			Annotation[][] paramterAnnotations) {
		Errors errors = new Errors(member);
		Iterator<Annotation[]> annotationsIterator = Arrays.asList(paramterAnnotations).iterator();

		List<Dependency<?>> dependencies = Lists.newArrayList();
		int index = 0;

		for (TypeLiteral<?> parameterType : type.getParameterTypes(member)) {
			try {
				Annotation[] parameterAnnotations = annotationsIterator.next();
				Key<?> key = Annotations.getKey(parameterType, member, parameterAnnotations, errors);
				dependencies.add(newDependency(key, Nullability.allowsNull(parameterAnnotations), index));
				index++;
			} catch (ErrorsException e) {
				errors.merge(e.getErrors());
			}
		}

		errors.throwConfigurationExceptionIfErrorsExist();
		return ImmutableList.copyOf(dependencies);
	}

	
	
	/**
	 * New dependency.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @param allowsNull the allows null
	 * @param parameterIndex the parameter index
	 * @return the dependency
	 */
	private <T> Dependency<T> newDependency(Key<T> key, boolean allowsNull, int parameterIndex) {
		return new Dependency<T>(this, key, allowsNull, parameterIndex);
	}

	
	/**
	 * Gets the member.
	 *
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	
	/**
	 * Gets the dependencies.
	 *
	 * @return the dependencies
	 */
	public List<Dependency<?>> getDependencies() {
		return dependencies;
	}

	
	/**
	 * Checks if is optional.
	 *
	 * @return true, if is optional
	 */
	public boolean isOptional() {
		return optional;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof InjectionPoint && member.equals(((InjectionPoint) o).member);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return member.hashCode();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreTypes.toString(member);
	}

	
	/**
	 * For constructor of.
	 *
	 * @param type the type
	 * @return the injection point
	 */
	public static InjectionPoint forConstructorOf(TypeLiteral<?> type) {
		Class<?> rawType = MoreTypes.getRawType(type.getType());
		Errors errors = new Errors(rawType);

		Constructor<?> injectableConstructor = null;
		for (Constructor<?> constructor : rawType.getDeclaredConstructors()) {
			Inject inject = constructor.getAnnotation(Inject.class);
			if (inject != null) {
				if (inject.optional()) {
					errors.optionalConstructor(constructor);
				}

				if (injectableConstructor != null) {
					errors.tooManyConstructors(rawType);
				}

				injectableConstructor = constructor;
				checkForMisplacedBindingAnnotations(injectableConstructor, errors);
			}
		}

		errors.throwConfigurationExceptionIfErrorsExist();

		if (injectableConstructor != null) {
			return new InjectionPoint(type, injectableConstructor);
		}

		
		try {
			Constructor<?> noArgConstructor = rawType.getDeclaredConstructor();

			
			if (Modifier.isPrivate(noArgConstructor.getModifiers()) && !Modifier.isPrivate(rawType.getModifiers())) {
				errors.missingConstructor(rawType);
				throw new ConfigurationException(errors.getMessages());
			}

			checkForMisplacedBindingAnnotations(noArgConstructor, errors);
			return new InjectionPoint(type, noArgConstructor);
		} catch (NoSuchMethodException e) {
			errors.missingConstructor(rawType);
			throw new ConfigurationException(errors.getMessages());
		}
	}

	
	/**
	 * For constructor of.
	 *
	 * @param type the type
	 * @return the injection point
	 */
	public static InjectionPoint forConstructorOf(Class<?> type) {
		return forConstructorOf(TypeLiteral.get(type));
	}

	
	/**
	 * For static methods and fields.
	 *
	 * @param type the type
	 * @return the sets the
	 */
	public static Set<InjectionPoint> forStaticMethodsAndFields(TypeLiteral<?> type) {
		List<InjectionPoint> sink = Lists.newArrayList();
		Errors errors = new Errors();

		addInjectionPoints(type, Factory.FIELDS, true, sink, errors);
		addInjectionPoints(type, Factory.METHODS, true, sink, errors);

		ImmutableSet<InjectionPoint> result = ImmutableSet.copyOf(sink);
		if (errors.hasErrors()) {
			throw new ConfigurationException(errors.getMessages()).withPartialValue(result);
		}
		return result;
	}

	
	/**
	 * For static methods and fields.
	 *
	 * @param type the type
	 * @return the sets the
	 */
	public static Set<InjectionPoint> forStaticMethodsAndFields(Class<?> type) {
		return forStaticMethodsAndFields(TypeLiteral.get(type));
	}

	
	/**
	 * For instance methods and fields.
	 *
	 * @param type the type
	 * @return the sets the
	 */
	public static Set<InjectionPoint> forInstanceMethodsAndFields(TypeLiteral<?> type) {
		List<InjectionPoint> sink = Lists.newArrayList();
		Errors errors = new Errors();

		addInjectionPoints(type, Factory.FIELDS, false, sink, errors);
		addInjectionPoints(type, Factory.METHODS, false, sink, errors);

		ImmutableSet<InjectionPoint> result = ImmutableSet.copyOf(sink);
		if (errors.hasErrors()) {
			throw new ConfigurationException(errors.getMessages()).withPartialValue(result);
		}
		return result;
	}

	
	/**
	 * For instance methods and fields.
	 *
	 * @param type the type
	 * @return the sets the
	 */
	public static Set<InjectionPoint> forInstanceMethodsAndFields(Class<?> type) {
		return forInstanceMethodsAndFields(TypeLiteral.get(type));
	}

	
	/**
	 * Check for misplaced binding annotations.
	 *
	 * @param member the member
	 * @param errors the errors
	 */
	private static void checkForMisplacedBindingAnnotations(Member member, Errors errors) {
		Annotation misplacedBindingAnnotation = Annotations.findBindingAnnotation(errors, member,
				((AnnotatedElement) member).getAnnotations());
		if (misplacedBindingAnnotation == null) {
			return;
		}

		
		
		if (member instanceof Method) {
			try {
				if (member.getDeclaringClass().getDeclaredField(member.getName()) != null) {
					return;
				}
			} catch (NoSuchFieldException ignore) {
			}
		}

		errors.misplacedBindingAnnotation(member, misplacedBindingAnnotation);
	}

	
	/**
	 * Adds the injection points.
	 *
	 * @param <M> the generic type
	 * @param type the type
	 * @param factory the factory
	 * @param statics the statics
	 * @param injectionPoints the injection points
	 * @param errors the errors
	 */
	private static <M extends Member & AnnotatedElement> void addInjectionPoints(TypeLiteral<?> type,
			Factory<M> factory, boolean statics, Collection<InjectionPoint> injectionPoints, Errors errors) {
		if (type.getType() == Object.class) {
			return;
		}

		
		TypeLiteral<?> superType = type.getSupertype(type.getRawType().getSuperclass());
		addInjectionPoints(superType, factory, statics, injectionPoints, errors);

		
		addInjectorsForMembers(type, factory, statics, injectionPoints, errors);
	}

	
	/**
	 * Adds the injectors for members.
	 *
	 * @param <M> the generic type
	 * @param typeLiteral the type literal
	 * @param factory the factory
	 * @param statics the statics
	 * @param injectionPoints the injection points
	 * @param errors the errors
	 */
	private static <M extends Member & AnnotatedElement> void addInjectorsForMembers(TypeLiteral<?> typeLiteral,
			Factory<M> factory, boolean statics, Collection<InjectionPoint> injectionPoints, Errors errors) {
		for (M member : factory.getMembers(MoreTypes.getRawType(typeLiteral.getType()))) {
			if (isStatic(member) != statics) {
				continue;
			}

			Inject inject = member.getAnnotation(Inject.class);
			if (inject == null) {
				continue;
			}

			try {
				injectionPoints.add(factory.create(typeLiteral, member, errors));
			} catch (ConfigurationException ignorable) {
				if (!inject.optional()) {
					errors.merge(ignorable.getErrorMessages());
				}
			}
		}
	}

	
	/**
	 * Checks if is static.
	 *
	 * @param member the member
	 * @return true, if is static
	 */
	private static boolean isStatic(Member member) {
		return Modifier.isStatic(member.getModifiers());
	}

	
	/**
	 * The Interface Factory.
	 *
	 * @param <M> the generic type
	 * @author l.xue.nong
	 */
	private interface Factory<M extends Member & AnnotatedElement> {

		
		/** The fields. */
		Factory<Field> FIELDS = new Factory<Field>() {
			public Field[] getMembers(Class<?> type) {
				return type.getDeclaredFields();
			}

			public InjectionPoint create(TypeLiteral<?> typeLiteral, Field member, Errors errors) {
				return new InjectionPoint(typeLiteral, member);
			}
		};

		
		/** The methods. */
		Factory<Method> METHODS = new Factory<Method>() {
			public Method[] getMembers(Class<?> type) {
				return type.getDeclaredMethods();
			}

			public InjectionPoint create(TypeLiteral<?> typeLiteral, Method member, Errors errors) {
				checkForMisplacedBindingAnnotations(member, errors);
				return new InjectionPoint(typeLiteral, member);
			}
		};

		
		/**
		 * Gets the members.
		 *
		 * @param type the type
		 * @return the members
		 */
		M[] getMembers(Class<?> type);

		
		/**
		 * Creates the.
		 *
		 * @param typeLiteral the type literal
		 * @param member the member
		 * @param errors the errors
		 * @return the injection point
		 */
		InjectionPoint create(TypeLiteral<?> typeLiteral, M member, Errors errors);
	}
}
