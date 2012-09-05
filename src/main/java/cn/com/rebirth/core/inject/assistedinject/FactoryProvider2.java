/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FactoryProvider2.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.assistedinject;

import static com.google.common.base.Preconditions.checkState;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import cn.com.rebirth.core.inject.AbstractModule;
import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Module;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.ProvisionException;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.internal.Annotations;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.spi.Message;
import cn.com.rebirth.core.inject.util.Providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * The Class FactoryProvider2.
 *
 * @param <F> the generic type
 * @author l.xue.nong
 */
final class FactoryProvider2<F> implements InvocationHandler, Provider<F> {

	
	/** The Constant DEFAULT_ANNOTATION. */
	static final Assisted DEFAULT_ANNOTATION = new Assisted() {
		public String value() {
			return "";
		}

		public Class<? extends Annotation> annotationType() {
			return Assisted.class;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Assisted && ((Assisted) o).value().equals("");
		}

		@Override
		public int hashCode() {
			return 127 * "value".hashCode() ^ "".hashCode();
		}

		@Override
		public String toString() {
			return "@" + Assisted.class.getName() + "(value=)";
		}
	};

	
	/** The produced type. */
	private final Key<?> producedType;

	
	/** The return types by method. */
	private final ImmutableMap<Method, Key<?>> returnTypesByMethod;

	
	/** The param types. */
	private final ImmutableMap<Method, ImmutableList<Key<?>>> paramTypes;

	
	/** The injector. */
	private Injector injector;

	
	/** The factory. */
	private final F factory;

	
	/**
	 * Instantiates a new factory provider2.
	 *
	 * @param factoryType the factory type
	 * @param producedType the produced type
	 */
	@SuppressWarnings("rawtypes")
	FactoryProvider2(TypeLiteral<F> factoryType, Key<?> producedType) {
		this.producedType = producedType;

		Errors errors = new Errors();

		@SuppressWarnings("unchecked")
		
		Class<F> factoryRawType = (Class) factoryType.getRawType();

		try {
			ImmutableMap.Builder<Method, Key<?>> returnTypesBuilder = ImmutableMap.builder();
			ImmutableMap.Builder<Method, ImmutableList<Key<?>>> paramTypesBuilder = ImmutableMap.builder();
			for (Method method : factoryRawType.getMethods()) {
				Key<?> returnType = Annotations.getKey(factoryType.getReturnType(method), method,
						method.getAnnotations(), errors);
				returnTypesBuilder.put(method, returnType);
				List<TypeLiteral<?>> params = factoryType.getParameterTypes(method);
				Annotation[][] paramAnnotations = method.getParameterAnnotations();
				int p = 0;
				List<Key<?>> keys = Lists.newArrayList();
				for (TypeLiteral<?> param : params) {
					Key<?> paramKey = Annotations.getKey(param, method, paramAnnotations[p++], errors);
					keys.add(assistKey(method, paramKey, errors));
				}
				paramTypesBuilder.put(method, ImmutableList.copyOf(keys));
			}
			returnTypesByMethod = returnTypesBuilder.build();
			paramTypes = paramTypesBuilder.build();
		} catch (ErrorsException e) {
			throw new ConfigurationException(e.getErrors().getMessages());
		}

		factory = factoryRawType.cast(Proxy.newProxyInstance(factoryRawType.getClassLoader(),
				new Class[] { factoryRawType }, this));
	}

	
	public F get() {
		return factory;
	}

	
	/**
	 * Assist key.
	 *
	 * @param <T> the generic type
	 * @param method the method
	 * @param key the key
	 * @param errors the errors
	 * @return the key
	 * @throws ErrorsException the errors exception
	 */
	private <T> Key<T> assistKey(Method method, Key<T> key, Errors errors) throws ErrorsException {
		if (key.getAnnotationType() == null) {
			return Key.get(key.getTypeLiteral(), DEFAULT_ANNOTATION);
		} else if (key.getAnnotationType() == Assisted.class) {
			return key;
		} else {
			errors.withSource(method).addMessage("Only @Assisted is allowed for factory parameters, but found @%s",
					key.getAnnotationType());
			throw errors.toException();
		}
	}

	
	/**
	 * Initialize.
	 *
	 * @param injector the injector
	 */
	@Inject
	void initialize(Injector injector) {
		if (this.injector != null) {
			throw new ConfigurationException(ImmutableList.of(new Message(FactoryProvider2.class,
					"Factories.create() factories may only be used in one Injector!")));
		}

		this.injector = injector;

		for (Method method : returnTypesByMethod.keySet()) {
			Object[] args = new Object[method.getParameterTypes().length];
			Arrays.fill(args, "dummy object for validating Factories");
			getBindingFromNewInjector(method, args); 
		}
	}

	
	/**
	 * Gets the binding from new injector.
	 *
	 * @param method the method
	 * @param args the args
	 * @return the binding from new injector
	 */
	public Binding<?> getBindingFromNewInjector(final Method method, final Object[] args) {
		checkState(injector != null, "Factories.create() factories cannot be used until they're initialized by Guice.");

		final Key<?> returnType = returnTypesByMethod.get(method);

		Module assistedModule = new AbstractModule() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			
			protected void configure() {
				Binder binder = binder().withSource(method);

				int p = 0;
				for (Key<?> paramKey : paramTypes.get(method)) {
					
					binder.bind((Key) paramKey).toProvider(Providers.of(args[p++]));
				}

				if (producedType != null && !returnType.equals(producedType)) {
					binder.bind(returnType).to((Key) producedType);
				} else {
					binder.bind(returnType);
				}
			}
		};

		Injector forCreate = injector.createChildInjector(assistedModule);
		return forCreate.getBinding(returnType);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		if (method.getDeclaringClass() == Object.class) {
			return method.invoke(this, args);
		}

		Provider<?> provider = getBindingFromNewInjector(method, args).getProvider();
		try {
			return provider.get();
		} catch (ProvisionException e) {
			
			if (e.getErrorMessages().size() == 1) {
				Message onlyError = Iterables.getOnlyElement(e.getErrorMessages());
				Throwable cause = onlyError.getCause();
				if (cause != null && canRethrow(method, cause)) {
					throw cause;
				}
			}
			throw e;
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return factory.getClass().getInterfaces()[0].getName() + " for " + producedType.getTypeLiteral();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o == this || o == factory;
	}

	
	/**
	 * Can rethrow.
	 *
	 * @param invoked the invoked
	 * @param thrown the thrown
	 * @return true, if successful
	 */
	static boolean canRethrow(Method invoked, Throwable thrown) {
		if (thrown instanceof Error || thrown instanceof RuntimeException) {
			return true;
		}

		for (Class<?> declared : invoked.getExceptionTypes()) {
			if (declared.isInstance(thrown)) {
				return true;
			}
		}

		return false;
	}
}
