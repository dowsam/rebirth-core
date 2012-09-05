/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MembersInjectorImpl.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.spi.InjectionListener;
import cn.com.rebirth.core.inject.spi.InjectionPoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


/**
 * The Class MembersInjectorImpl.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
class MembersInjectorImpl<T> implements MembersInjector<T> {

	
	/** The type literal. */
	private final TypeLiteral<T> typeLiteral;

	
	/** The injector. */
	private final InjectorImpl injector;

	
	/** The member injectors. */
	private final ImmutableList<SingleMemberInjector> memberInjectors;

	
	/** The user members injectors. */
	private final ImmutableList<MembersInjector<? super T>> userMembersInjectors;

	
	/** The injection listeners. */
	private final ImmutableList<InjectionListener<? super T>> injectionListeners;

	
	/**
	 * Instantiates a new members injector impl.
	 *
	 * @param injector the injector
	 * @param typeLiteral the type literal
	 * @param encounter the encounter
	 * @param memberInjectors the member injectors
	 */
	MembersInjectorImpl(InjectorImpl injector, TypeLiteral<T> typeLiteral, EncounterImpl<T> encounter,
			ImmutableList<SingleMemberInjector> memberInjectors) {
		this.injector = injector;
		this.typeLiteral = typeLiteral;
		this.memberInjectors = memberInjectors;
		this.userMembersInjectors = encounter.getMembersInjectors();
		this.injectionListeners = encounter.getInjectionListeners();
	}

	
	/**
	 * Gets the member injectors.
	 *
	 * @return the member injectors
	 */
	public ImmutableList<SingleMemberInjector> getMemberInjectors() {
		return memberInjectors;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.MembersInjector#injectMembers(java.lang.Object)
	 */
	public void injectMembers(T instance) {
		Errors errors = new Errors(typeLiteral);
		try {
			injectAndNotify(instance, errors);
		} catch (ErrorsException e) {
			errors.merge(e.getErrors());
		}

		errors.throwProvisionExceptionIfErrorsExist();
	}

	
	/**
	 * Inject and notify.
	 *
	 * @param instance the instance
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	void injectAndNotify(final T instance, final Errors errors) throws ErrorsException {
		if (instance == null) {
			return;
		}

		injector.callInContext(new ContextualCallable<Void>() {
			public Void call(InternalContext context) throws ErrorsException {
				injectMembers(instance, errors, context);
				return null;
			}
		});

		notifyListeners(instance, errors);
	}

	
	/**
	 * Notify listeners.
	 *
	 * @param instance the instance
	 * @param errors the errors
	 * @throws ErrorsException the errors exception
	 */
	void notifyListeners(T instance, Errors errors) throws ErrorsException {
		int numErrorsBefore = errors.size();
		for (InjectionListener<? super T> injectionListener : injectionListeners) {
			try {
				injectionListener.afterInjection(instance);
			} catch (RuntimeException e) {
				errors.errorNotifyingInjectionListener(injectionListener, typeLiteral, e);
			}
		}
		errors.throwIfNewErrors(numErrorsBefore);
	}

	
	/**
	 * Inject members.
	 *
	 * @param t the t
	 * @param errors the errors
	 * @param context the context
	 */
	void injectMembers(T t, Errors errors, InternalContext context) {
		
		for (int i = 0, size = memberInjectors.size(); i < size; i++) {
			memberInjectors.get(i).inject(errors, context, t);
		}

		
		for (int i = 0, size = userMembersInjectors.size(); i < size; i++) {
			MembersInjector<? super T> userMembersInjector = userMembersInjectors.get(i);
			try {
				userMembersInjector.injectMembers(t);
			} catch (RuntimeException e) {
				errors.errorInUserInjector(userMembersInjector, typeLiteral, e);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MembersInjector<" + typeLiteral + ">";
	}

	
	/**
	 * Gets the injection points.
	 *
	 * @return the injection points
	 */
	public ImmutableSet<InjectionPoint> getInjectionPoints() {
		ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
		for (SingleMemberInjector memberInjector : memberInjectors) {
			builder.add(memberInjector.getInjectionPoint());
		}
		return builder.build();
	}
}
