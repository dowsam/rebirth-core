/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FailableCache.java 2012-7-6 10:23:45 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

import java.util.Map;


/**
 * The Class FailableCache.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author l.xue.nong
 */
public abstract class FailableCache<K, V> {

	
	/** The delegate. */
	@SuppressWarnings("deprecation")
	private final Map<K, Object> delegate = new MapMaker().makeComputingMap(new Function<K, Object>() {
		public Object apply(@Nullable K key) {
			Errors errors = new Errors();
			V result = null;
			try {
				result = FailableCache.this.create(key, errors);
			} catch (ErrorsException e) {
				errors.merge(e.getErrors());
			}
			return errors.hasErrors() ? errors : result;
		}
	});

	
	/**
	 * Creates the.
	 *
	 * @param key the key
	 * @param errors the errors
	 * @return the v
	 * @throws ErrorsException the errors exception
	 */
	protected abstract V create(K key, Errors errors) throws ErrorsException;

	
	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @param errors the errors
	 * @return the v
	 * @throws ErrorsException the errors exception
	 */
	public V get(K key, Errors errors) throws ErrorsException {
		Object resultOrError = delegate.get(key);
		if (resultOrError instanceof Errors) {
			errors.merge((Errors) resultOrError);
			throw errors.toException();
		} else {
			@SuppressWarnings("unchecked")
			
			V result = (V) resultOrError;
			return result;
		}
	}
}
