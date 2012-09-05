/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Initializer.java 2012-7-6 10:23:53 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.spi.InjectionPoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The Class Initializer.
 *
 * @author l.xue.nong
 */
class Initializer {
	
	
	/** The creating thread. */
	private final Thread creatingThread = Thread.currentThread();

	
	/** The ready. */
	private final CountDownLatch ready = new CountDownLatch(1);

	
	/** The pending injection. */
	private final Map<Object, InjectableReference<?>> pendingInjection = Maps.newIdentityHashMap();

	
	/**
	 * Request injection.
	 *
	 * @param <T> the generic type
	 * @param injector the injector
	 * @param instance the instance
	 * @param source the source
	 * @param injectionPoints the injection points
	 * @return the initializable
	 */
	public <T> Initializable<T> requestInjection(InjectorImpl injector, T instance, Object source,
			Set<InjectionPoint> injectionPoints) {
		checkNotNull(source);

		
		if (instance == null || (injectionPoints.isEmpty() && !injector.membersInjectorStore.hasTypeListeners())) {
			return Initializables.of(instance);
		}

		InjectableReference<T> initializable = new InjectableReference<T>(injector, instance, source);
		pendingInjection.put(instance, initializable);
		return initializable;
	}

	
	/**
	 * Validate oustanding injections.
	 *
	 * @param errors the errors
	 */
	void validateOustandingInjections(Errors errors) {
		for (InjectableReference<?> reference : pendingInjection.values()) {
			try {
				reference.validate(errors);
			} catch (ErrorsException e) {
				errors.merge(e.getErrors());
			}
		}
	}

	
	/**
	 * Inject all.
	 *
	 * @param errors the errors
	 */
	void injectAll(final Errors errors) {
		
		
		for (InjectableReference<?> reference : Lists.newArrayList(pendingInjection.values())) {
			try {
				reference.get(errors);
			} catch (ErrorsException e) {
				errors.merge(e.getErrors());
			}
		}

		if (!pendingInjection.isEmpty()) {
			throw new AssertionError("Failed to satisfy " + pendingInjection);
		}

		ready.countDown();
	}

	
	/**
	 * The Class InjectableReference.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private class InjectableReference<T> implements Initializable<T> {

		
		/** The injector. */
		private final InjectorImpl injector;

		
		/** The instance. */
		private final T instance;

		
		/** The source. */
		private final Object source;

		
		/** The members injector. */
		private MembersInjectorImpl<T> membersInjector;

		
		/**
		 * Instantiates a new injectable reference.
		 *
		 * @param injector the injector
		 * @param instance the instance
		 * @param source the source
		 */
		public InjectableReference(InjectorImpl injector, T instance, Object source) {
			this.injector = injector;
			this.instance = checkNotNull(instance, "instance");
			this.source = checkNotNull(source, "source");
		}

		
		/**
		 * Validate.
		 *
		 * @param errors the errors
		 * @throws ErrorsException the errors exception
		 */
		public void validate(Errors errors) throws ErrorsException {
			@SuppressWarnings("unchecked")
			
			TypeLiteral<T> type = TypeLiteral.get((Class<T>) instance.getClass());
			membersInjector = injector.membersInjectorStore.get(type, errors.withSource(source));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Initializable#get(cn.com.rebirth.search.commons.inject.internal.Errors)
		 */
		public T get(Errors errors) throws ErrorsException {
			if (ready.getCount() == 0) {
				return instance;
			}

			
			if (Thread.currentThread() != creatingThread) {
				try {
					ready.await();
					return instance;
				} catch (InterruptedException e) {
					
					throw new RuntimeException(e);
				}
			}

			
			if (pendingInjection.remove(instance) != null) {
				membersInjector.injectAndNotify(instance, errors.withSource(source));
			}

			return instance;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return instance.toString();
		}
	}
}