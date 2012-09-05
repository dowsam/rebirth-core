/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FactoryProvider.java 2012-7-6 10:23:42 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.assistedinject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.HasDependencies;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The Class FactoryProvider.
 *
 * @param <F> the generic type
 * @author l.xue.nong
 */
public class FactoryProvider<F> implements Provider<F>, HasDependencies {

	

	
	/** The injector. */
	private Injector injector;

	
	/** The factory type. */
	private final TypeLiteral<F> factoryType;

	
	/** The factory method to constructor. */
	private final Map<Method, AssistedConstructor<?>> factoryMethodToConstructor;

	
	/**
	 * New factory.
	 *
	 * @param <F> the generic type
	 * @param factoryType the factory type
	 * @param implementationType the implementation type
	 * @return the provider
	 */
	public static <F> Provider<F> newFactory(Class<F> factoryType, Class<?> implementationType) {
		return newFactory(TypeLiteral.get(factoryType), TypeLiteral.get(implementationType));
	}

	
	/**
	 * New factory.
	 *
	 * @param <F> the generic type
	 * @param factoryType the factory type
	 * @param implementationType the implementation type
	 * @return the provider
	 */
	public static <F> Provider<F> newFactory(TypeLiteral<F> factoryType, TypeLiteral<?> implementationType) {
		Map<Method, AssistedConstructor<?>> factoryMethodToConstructor = createMethodMapping(factoryType,
				implementationType);

		if (!factoryMethodToConstructor.isEmpty()) {
			return new FactoryProvider<F>(factoryType, factoryMethodToConstructor);
		} else {
			return new FactoryProvider2<F>(factoryType, Key.get(implementationType));
		}
	}

	
	/**
	 * Instantiates a new factory provider.
	 *
	 * @param factoryType the factory type
	 * @param factoryMethodToConstructor the factory method to constructor
	 */
	private FactoryProvider(TypeLiteral<F> factoryType, Map<Method, AssistedConstructor<?>> factoryMethodToConstructor) {
		this.factoryType = factoryType;
		this.factoryMethodToConstructor = factoryMethodToConstructor;
		checkDeclaredExceptionsMatch();
	}

	
	/**
	 * Sets the injector and check unbound parameters are injectable.
	 *
	 * @param injector the new injector and check unbound parameters are injectable
	 */
	@Inject
	void setInjectorAndCheckUnboundParametersAreInjectable(Injector injector) {
		this.injector = injector;
		for (AssistedConstructor<?> c : factoryMethodToConstructor.values()) {
			for (Parameter p : c.getAllParameters()) {
				if (!p.isProvidedByFactory() && !paramCanBeInjected(p, injector)) {
					
					
					
					throw newConfigurationException("Parameter of type '%s' is not injectable or annotated "
							+ "with @Assisted for Constructor '%s'", p, c);
				}
			}
		}
	}

	
	/**
	 * Check declared exceptions match.
	 */
	private void checkDeclaredExceptionsMatch() {
		for (Map.Entry<Method, AssistedConstructor<?>> entry : factoryMethodToConstructor.entrySet()) {
			for (Class<?> constructorException : entry.getValue().getDeclaredExceptions()) {
				if (!isConstructorExceptionCompatibleWithFactoryExeception(constructorException, entry.getKey()
						.getExceptionTypes())) {
					throw newConfigurationException("Constructor %s declares an exception, but no compatible "
							+ "exception is thrown by the factory method %s", entry.getValue(), entry.getKey());
				}
			}
		}
	}

	
	/**
	 * Checks if is constructor exception compatible with factory exeception.
	 *
	 * @param constructorException the constructor exception
	 * @param factoryExceptions the factory exceptions
	 * @return true, if is constructor exception compatible with factory exeception
	 */
	private boolean isConstructorExceptionCompatibleWithFactoryExeception(Class<?> constructorException,
			Class<?>[] factoryExceptions) {
		for (Class<?> factoryException : factoryExceptions) {
			if (factoryException.isAssignableFrom(constructorException)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Param can be injected.
	 *
	 * @param parameter the parameter
	 * @param injector the injector
	 * @return true, if successful
	 */
	private boolean paramCanBeInjected(Parameter parameter, Injector injector) {
		return parameter.isBound(injector);
	}

	
	/**
	 * Creates the method mapping.
	 *
	 * @param factoryType the factory type
	 * @param implementationType the implementation type
	 * @return the map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<Method, AssistedConstructor<?>> createMethodMapping(TypeLiteral<?> factoryType,
			TypeLiteral<?> implementationType) {
		List<AssistedConstructor<?>> constructors = Lists.newArrayList();

		for (Constructor<?> constructor : implementationType.getRawType().getDeclaredConstructors()) {
			if (constructor.getAnnotation(AssistedInject.class) != null) {
				
				AssistedConstructor assistedConstructor = new AssistedConstructor(constructor,
						implementationType.getParameterTypes(constructor));
				constructors.add(assistedConstructor);
			}
		}

		if (constructors.isEmpty()) {
			return ImmutableMap.of();
		}

		Method[] factoryMethods = factoryType.getRawType().getMethods();

		if (constructors.size() != factoryMethods.length) {
			throw newConfigurationException("Constructor mismatch: %s has %s @AssistedInject "
					+ "constructors, factory %s has %s creation methods", implementationType, constructors.size(),
					factoryType, factoryMethods.length);
		}

		Map<ParameterListKey, AssistedConstructor> paramsToConstructor = Maps.newHashMap();

		for (AssistedConstructor c : constructors) {
			if (paramsToConstructor.containsKey(c.getAssistedParameters())) {
				throw new RuntimeException("Duplicate constructor, " + c);
			}
			paramsToConstructor.put(c.getAssistedParameters(), c);
		}

		Map<Method, AssistedConstructor<?>> result = Maps.newHashMap();
		for (Method method : factoryMethods) {
			if (!method.getReturnType().isAssignableFrom(implementationType.getRawType())) {
				throw newConfigurationException("Return type of method %s is not assignable from %s", method,
						implementationType);
			}

			List<Type> parameterTypes = Lists.newArrayList();
			for (TypeLiteral<?> parameterType : factoryType.getParameterTypes(method)) {
				parameterTypes.add(parameterType.getType());
			}
			ParameterListKey methodParams = new ParameterListKey(parameterTypes);

			if (!paramsToConstructor.containsKey(methodParams)) {
				throw newConfigurationException("%s has no @AssistInject constructor that takes the "
						+ "@Assisted parameters %s in that order. @AssistInject constructors are %s",
						implementationType, methodParams, paramsToConstructor.values());
			}

			method.getParameterAnnotations();
			for (Annotation[] parameterAnnotations : method.getParameterAnnotations()) {
				for (Annotation parameterAnnotation : parameterAnnotations) {
					if (parameterAnnotation.annotationType() == Assisted.class) {
						throw newConfigurationException("Factory method %s has an @Assisted parameter, which "
								+ "is incompatible with the deprecated @AssistedInject annotation. Please replace "
								+ "@AssistedInject with @Inject on the %s constructor.", method, implementationType);
					}
				}
			}

			AssistedConstructor matchingConstructor = paramsToConstructor.remove(methodParams);

			result.put(method, matchingConstructor);
		}
		return result;
	}

	
	public Set<Dependency<?>> getDependencies() {
		List<Dependency<?>> dependencies = Lists.newArrayList();
		for (AssistedConstructor<?> constructor : factoryMethodToConstructor.values()) {
			for (Parameter parameter : constructor.getAllParameters()) {
				if (!parameter.isProvidedByFactory()) {
					dependencies.add(Dependency.get(parameter.getPrimaryBindingKey()));
				}
			}
		}
		return ImmutableSet.copyOf(dependencies);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public F get() {
		InvocationHandler invocationHandler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] creationArgs) throws Throwable {
				
				if (method.getDeclaringClass().equals(Object.class)) {
					return method.invoke(this, creationArgs);
				}

				AssistedConstructor<?> constructor = factoryMethodToConstructor.get(method);
				Object[] constructorArgs = gatherArgsForConstructor(constructor, creationArgs);
				Object objectToReturn = constructor.newInstance(constructorArgs);
				injector.injectMembers(objectToReturn);
				return objectToReturn;
			}

			public Object[] gatherArgsForConstructor(AssistedConstructor<?> constructor, Object[] factoryArgs) {
				int numParams = constructor.getAllParameters().size();
				int argPosition = 0;
				Object[] result = new Object[numParams];

				for (int i = 0; i < numParams; i++) {
					Parameter parameter = constructor.getAllParameters().get(i);
					if (parameter.isProvidedByFactory()) {
						result[i] = factoryArgs[argPosition];
						argPosition++;
					} else {
						result[i] = parameter.getValue(injector);
					}
				}
				return result;
			}
		};

		
		Class<F> factoryRawType = (Class) factoryType.getRawType();
		return factoryRawType.cast(Proxy.newProxyInstance(factoryRawType.getClassLoader(),
				new Class[] { factoryRawType }, invocationHandler));
	}

	
	/**
	 * New configuration exception.
	 *
	 * @param format the format
	 * @param args the args
	 * @return the configuration exception
	 */
	private static ConfigurationException newConfigurationException(String format, Object... args) {
		return new ConfigurationException(ImmutableSet.of(new Message(Errors.format(format, args))));
	}
}
