/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons TypeConverterBindingProcessor.java 2012-7-6 10:23:54 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import cn.com.rebirth.commons.Strings;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.MatcherAndConverter;
import cn.com.rebirth.core.inject.internal.SourceProvider;
import cn.com.rebirth.core.inject.matcher.AbstractMatcher;
import cn.com.rebirth.core.inject.matcher.Matcher;
import cn.com.rebirth.core.inject.matcher.Matchers;
import cn.com.rebirth.core.inject.spi.TypeConverter;
import cn.com.rebirth.core.inject.spi.TypeConverterBinding;


/**
 * The Class TypeConverterBindingProcessor.
 *
 * @author l.xue.nong
 */
class TypeConverterBindingProcessor extends AbstractProcessor {

	
	/**
	 * Instantiates a new type converter binding processor.
	 *
	 * @param errors the errors
	 */
	TypeConverterBindingProcessor(Errors errors) {
		super(errors);
	}

	
	/**
	 * Prepare built in converters.
	 *
	 * @param injector the injector
	 */
	public void prepareBuiltInConverters(InjectorImpl injector) {
		this.injector = injector;
		try {
			
			convertToPrimitiveType(int.class, Integer.class);
			convertToPrimitiveType(long.class, Long.class);
			convertToPrimitiveType(boolean.class, Boolean.class);
			convertToPrimitiveType(byte.class, Byte.class);
			convertToPrimitiveType(short.class, Short.class);
			convertToPrimitiveType(float.class, Float.class);
			convertToPrimitiveType(double.class, Double.class);

			convertToClass(Character.class, new TypeConverter() {
				public Object convert(String value, TypeLiteral<?> toType) {
					value = value.trim();
					if (value.length() != 1) {
						throw new RuntimeException("Length != 1.");
					}
					return value.charAt(0);
				}

				@Override
				public String toString() {
					return "TypeConverter<Character>";
				}
			});

			convertToClasses(Matchers.subclassesOf(Enum.class), new TypeConverter() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Object convert(String value, TypeLiteral<?> toType) {
					return Enum.valueOf((Class) toType.getRawType(), value);
				}

				@Override
				public String toString() {
					return "TypeConverter<E extends Enum<E>>";
				}
			});

			internalConvertToTypes(new AbstractMatcher<TypeLiteral<?>>() {
				public boolean matches(TypeLiteral<?> typeLiteral) {
					return typeLiteral.getRawType() == Class.class;
				}

				@Override
				public String toString() {
					return "Class<?>";
				}
			}, new TypeConverter() {
				public Object convert(String value, TypeLiteral<?> toType) {
					try {
						return Class.forName(value);
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e.getMessage());
					}
				}

				@Override
				public String toString() {
					return "TypeConverter<Class<?>>";
				}
			});
		} finally {
			this.injector = null;
		}
	}

	
	/**
	 * Convert to primitive type.
	 *
	 * @param <T> the generic type
	 * @param primitiveType the primitive type
	 * @param wrapperType the wrapper type
	 */
	private <T> void convertToPrimitiveType(Class<T> primitiveType, final Class<T> wrapperType) {
		try {
			final Method parser = wrapperType.getMethod("parse" + Strings.capitalize(primitiveType.getName()),
					String.class);

			TypeConverter typeConverter = new TypeConverter() {
				public Object convert(String value, TypeLiteral<?> toType) {
					try {
						return parser.invoke(null, value);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getTargetException().getMessage());
					}
				}

				@Override
				public String toString() {
					return "TypeConverter<" + wrapperType.getSimpleName() + ">";
				}
			};

			convertToClass(wrapperType, typeConverter);
		} catch (NoSuchMethodException e) {
			throw new AssertionError(e);
		}
	}

	
	/**
	 * Convert to class.
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param converter the converter
	 */
	private <T> void convertToClass(Class<T> type, TypeConverter converter) {
		convertToClasses(Matchers.identicalTo(type), converter);
	}

	
	/**
	 * Convert to classes.
	 *
	 * @param typeMatcher the type matcher
	 * @param converter the converter
	 */
	private void convertToClasses(final Matcher<? super Class<?>> typeMatcher, TypeConverter converter) {
		internalConvertToTypes(new AbstractMatcher<TypeLiteral<?>>() {
			public boolean matches(TypeLiteral<?> typeLiteral) {
				Type type = typeLiteral.getType();
				if (!(type instanceof Class)) {
					return false;
				}
				Class<?> clazz = (Class<?>) type;
				return typeMatcher.matches(clazz);
			}

			@Override
			public String toString() {
				return typeMatcher.toString();
			}
		}, converter);
	}

	
	/**
	 * Internal convert to types.
	 *
	 * @param typeMatcher the type matcher
	 * @param converter the converter
	 */
	private void internalConvertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
		injector.state.addConverter(new MatcherAndConverter(typeMatcher, converter, SourceProvider.UNKNOWN_SOURCE));
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.TypeConverterBinding)
	 */
	@Override
	public Boolean visit(TypeConverterBinding command) {
		injector.state.addConverter(new MatcherAndConverter(command.getTypeMatcher(), command.getTypeConverter(),
				command.getSource()));
		return true;
	}
}
