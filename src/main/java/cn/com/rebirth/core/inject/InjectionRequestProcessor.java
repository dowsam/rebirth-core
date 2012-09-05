/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InjectionRequestProcessor.java 2012-7-6 10:23:43 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import java.util.List;
import java.util.Set;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.ErrorsException;
import cn.com.rebirth.core.inject.internal.InternalContext;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.InjectionRequest;
import cn.com.rebirth.core.inject.spi.StaticInjectionRequest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**
 * The Class InjectionRequestProcessor.
 *
 * @author l.xue.nong
 */
class InjectionRequestProcessor extends AbstractProcessor {

	
	/** The static injections. */
	private final List<StaticInjection> staticInjections = Lists.newArrayList();

	
	/** The initializer. */
	private final Initializer initializer;

	
	/**
	 * Instantiates a new injection request processor.
	 *
	 * @param errors the errors
	 * @param initializer the initializer
	 */
	InjectionRequestProcessor(Errors errors, Initializer initializer) {
		super(errors);
		this.initializer = initializer;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.StaticInjectionRequest)
	 */
	@Override
	public Boolean visit(StaticInjectionRequest request) {
		staticInjections.add(new StaticInjection(injector, request));
		return true;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.AbstractProcessor#visit(cn.com.rebirth.search.commons.inject.spi.InjectionRequest)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Boolean visit(InjectionRequest request) {
		Set<InjectionPoint> injectionPoints;
		try {
			injectionPoints = request.getInjectionPoints();
		} catch (ConfigurationException e) {
			errors.merge(e.getErrorMessages());
			injectionPoints = e.getPartialValue();
		}

		initializer.requestInjection(injector, request.getInstance(), request.getSource(), injectionPoints);
		return true;
	}

	
	/**
	 * Validate.
	 */
	public void validate() {
		for (StaticInjection staticInjection : staticInjections) {
			staticInjection.validate();
		}
	}

	
	/**
	 * Inject members.
	 */
	public void injectMembers() {
		for (StaticInjection staticInjection : staticInjections) {
			staticInjection.injectMembers();
		}
	}

	
	/**
	 * The Class StaticInjection.
	 *
	 * @author l.xue.nong
	 */
	private class StaticInjection {

		
		/** The injector. */
		final InjectorImpl injector;

		
		/** The source. */
		final Object source;

		
		/** The request. */
		final StaticInjectionRequest request;

		
		/** The member injectors. */
		ImmutableList<SingleMemberInjector> memberInjectors;

		
		/**
		 * Instantiates a new static injection.
		 *
		 * @param injector the injector
		 * @param request the request
		 */
		public StaticInjection(InjectorImpl injector, StaticInjectionRequest request) {
			this.injector = injector;
			this.source = request.getSource();
			this.request = request;
		}

		
		/**
		 * Validate.
		 */
		void validate() {
			Errors errorsForMember = errors.withSource(source);
			Set<InjectionPoint> injectionPoints;
			try {
				injectionPoints = request.getInjectionPoints();
			} catch (ConfigurationException e) {
				errors.merge(e.getErrorMessages());
				injectionPoints = e.getPartialValue();
			}
			memberInjectors = injector.membersInjectorStore.getInjectors(injectionPoints, errorsForMember);
		}

		
		/**
		 * Inject members.
		 */
		void injectMembers() {
			try {
				injector.callInContext(new ContextualCallable<Void>() {
					public Void call(InternalContext context) {
						for (SingleMemberInjector injector : memberInjectors) {
							injector.inject(errors, context, null);
						}
						return null;
					}
				});
			} catch (ErrorsException e) {
				throw new AssertionError();
			}
		}
	}
}
