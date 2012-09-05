/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PrivateElementsImpl.java 2012-7-6 10:23:40 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.PrivateBinder;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.ElementVisitor;
import cn.com.rebirth.core.inject.spi.PrivateElements;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The Class PrivateElementsImpl.
 *
 * @author l.xue.nong
 */
public final class PrivateElementsImpl implements PrivateElements {

	
	/** The source. */
	private final Object source;

	
	/** The elements mutable. */
	private List<Element> elementsMutable = Lists.newArrayList();

	
	/** The exposure builders. */
	private List<ExposureBuilder<?>> exposureBuilders = Lists.newArrayList();

	
	/** The elements. */
	private ImmutableList<Element> elements;

	
	/** The exposed keys to sources. */
	private ImmutableMap<Key<?>, Object> exposedKeysToSources;

	
	/** The injector. */
	private Injector injector;

	
	/**
	 * Instantiates a new private elements impl.
	 *
	 * @param source the source
	 */
	public PrivateElementsImpl(Object source) {
		this.source = checkNotNull(source, "source");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public Object getSource() {
		return source;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.PrivateElements#getElements()
	 */
	public List<Element> getElements() {
		if (elements == null) {
			elements = ImmutableList.copyOf(elementsMutable);
			elementsMutable = null;
		}

		return elements;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.PrivateElements#getInjector()
	 */
	public Injector getInjector() {
		return injector;
	}

	
	/**
	 * Inits the injector.
	 *
	 * @param injector the injector
	 */
	public void initInjector(Injector injector) {
		checkState(this.injector == null, "injector already initialized");
		this.injector = checkNotNull(injector, "injector");
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.PrivateElements#getExposedKeys()
	 */
	public Set<Key<?>> getExposedKeys() {
		if (exposedKeysToSources == null) {
			Map<Key<?>, Object> exposedKeysToSourcesMutable = Maps.newLinkedHashMap();
			for (ExposureBuilder<?> exposureBuilder : exposureBuilders) {
				exposedKeysToSourcesMutable.put(exposureBuilder.getKey(), exposureBuilder.getSource());
			}
			exposedKeysToSources = ImmutableMap.copyOf(exposedKeysToSourcesMutable);
			exposureBuilders = null;
		}

		return exposedKeysToSources.keySet();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#acceptVisitor(cn.com.rebirth.search.commons.inject.spi.ElementVisitor)
	 */
	public <T> T acceptVisitor(ElementVisitor<T> visitor) {
		return visitor.visit(this);
	}

	
	/**
	 * Gets the elements mutable.
	 *
	 * @return the elements mutable
	 */
	public List<Element> getElementsMutable() {
		return elementsMutable;
	}

	
	/**
	 * Adds the exposure builder.
	 *
	 * @param exposureBuilder the exposure builder
	 */
	public void addExposureBuilder(ExposureBuilder<?> exposureBuilder) {
		exposureBuilders.add(exposureBuilder);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		PrivateBinder privateBinder = binder.withSource(source).newPrivateBinder();

		for (Element element : getElements()) {
			element.applyTo(privateBinder);
		}

		getExposedKeys(); 
		for (Map.Entry<Key<?>, Object> entry : exposedKeysToSources.entrySet()) {
			privateBinder.withSource(entry.getValue()).expose(entry.getKey());
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.PrivateElements#getExposedSource(cn.com.rebirth.search.commons.inject.Key)
	 */
	public Object getExposedSource(Key<?> key) {
		getExposedKeys(); 
		Object source = exposedKeysToSources.get(key);
		checkArgument(source != null, "%s not exposed by %s.", key, this);
		return source;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(PrivateElements.class).add("exposedKeys", getExposedKeys())
				.add("source", getSource()).toString();
	}
}
