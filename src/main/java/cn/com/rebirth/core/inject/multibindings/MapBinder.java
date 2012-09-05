/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons MapBinder.java 2012-7-6 10:23:49 l.xue.nong$$
 */

package cn.com.rebirth.core.inject.multibindings;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Module;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.binder.LinkedBindingBuilder;
import cn.com.rebirth.core.inject.multibindings.Multibinder.RealMultibinder;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.ProviderWithDependencies;
import cn.com.rebirth.core.inject.util.Types;

import com.google.common.collect.ImmutableSet;


/**
 * The Class MapBinder.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author l.xue.nong
 */
public abstract class MapBinder<K, V> {

	
	/**
	 * Instantiates a new map binder.
	 */
	private MapBinder() {
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
		binder = binder.skipSources(MapBinder.class, RealMapBinder.class);
		return newMapBinder(binder, valueType, Key.get(mapOf(keyType, valueType)),
				Key.get(mapOfProviderOf(keyType, valueType)),
				Multibinder.newSetBinder(binder, entryOfProviderOf(keyType, valueType)));
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType) {
		return newMapBinder(binder, TypeLiteral.get(keyType), TypeLiteral.get(valueType));
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @param annotation the annotation
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType,
			Annotation annotation) {
		binder = binder.skipSources(MapBinder.class, RealMapBinder.class);
		return newMapBinder(binder, valueType, Key.get(mapOf(keyType, valueType), annotation),
				Key.get(mapOfProviderOf(keyType, valueType), annotation),
				Multibinder.newSetBinder(binder, entryOfProviderOf(keyType, valueType), annotation));
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @param annotation the annotation
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType,
			Annotation annotation) {
		return newMapBinder(binder, TypeLiteral.get(keyType), TypeLiteral.get(valueType), annotation);
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @param annotationType the annotation type
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<K> keyType, TypeLiteral<V> valueType,
			Class<? extends Annotation> annotationType) {
		binder = binder.skipSources(MapBinder.class, RealMapBinder.class);
		return newMapBinder(binder, valueType, Key.get(mapOf(keyType, valueType), annotationType),
				Key.get(mapOfProviderOf(keyType, valueType), annotationType),
				Multibinder.newSetBinder(binder, entryOfProviderOf(keyType, valueType), annotationType));
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param keyType the key type
	 * @param valueType the value type
	 * @param annotationType the annotation type
	 * @return the map binder
	 */
	public static <K, V> MapBinder<K, V> newMapBinder(Binder binder, Class<K> keyType, Class<V> valueType,
			Class<? extends Annotation> annotationType) {
		return newMapBinder(binder, TypeLiteral.get(keyType), TypeLiteral.get(valueType), annotationType);
	}

	
	/**
	 * Map of.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the type literal
	 */
	@SuppressWarnings("unchecked")
	
	private static <K, V> TypeLiteral<Map<K, V>> mapOf(TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
		return (TypeLiteral<Map<K, V>>) TypeLiteral.get(Types.mapOf(keyType.getType(), valueType.getType()));
	}

	
	/**
	 * Map of provider of.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the type literal
	 */
	@SuppressWarnings("unchecked")
	
	private static <K, V> TypeLiteral<Map<K, Provider<V>>> mapOfProviderOf(TypeLiteral<K> keyType,
			TypeLiteral<V> valueType) {
		return (TypeLiteral<Map<K, Provider<V>>>) TypeLiteral.get(Types.mapOf(keyType.getType(),
				Types.newParameterizedType(Provider.class, valueType.getType())));
	}

	
	/**
	 * Entry of provider of.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the type literal
	 */
	@SuppressWarnings("unchecked")
	
	private static <K, V> TypeLiteral<Map.Entry<K, Provider<V>>> entryOfProviderOf(TypeLiteral<K> keyType,
			TypeLiteral<V> valueType) {
		return (TypeLiteral<Entry<K, Provider<V>>>) TypeLiteral.get(Types.newParameterizedTypeWithOwner(Map.class,
				Entry.class, keyType.getType(), Types.providerOf(valueType.getType())));
	}

	
	/**
	 * New map binder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param binder the binder
	 * @param valueType the value type
	 * @param mapKey the map key
	 * @param providerMapKey the provider map key
	 * @param entrySetBinder the entry set binder
	 * @return the map binder
	 */
	private static <K, V> MapBinder<K, V> newMapBinder(Binder binder, TypeLiteral<V> valueType, Key<Map<K, V>> mapKey,
			Key<Map<K, Provider<V>>> providerMapKey, Multibinder<Entry<K, Provider<V>>> entrySetBinder) {
		RealMapBinder<K, V> mapBinder = new RealMapBinder<K, V>(binder, valueType, mapKey, providerMapKey,
				entrySetBinder);
		binder.install(mapBinder);
		return mapBinder;
	}

	
	/**
	 * Adds the binding.
	 *
	 * @param key the key
	 * @return the linked binding builder
	 */
	public abstract LinkedBindingBuilder<V> addBinding(K key);

	
	/**
	 * The Class RealMapBinder.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @author l.xue.nong
	 */
	private static final class RealMapBinder<K, V> extends MapBinder<K, V> implements Module {

		
		/** The value type. */
		private final TypeLiteral<V> valueType;

		
		/** The map key. */
		private final Key<Map<K, V>> mapKey;

		
		/** The provider map key. */
		private final Key<Map<K, Provider<V>>> providerMapKey;

		
		/** The entry set binder. */
		private final RealMultibinder<Map.Entry<K, Provider<V>>> entrySetBinder;

		
		
		/** The binder. */
		private Binder binder;

		
		/**
		 * Instantiates a new real map binder.
		 *
		 * @param binder the binder
		 * @param valueType the value type
		 * @param mapKey the map key
		 * @param providerMapKey the provider map key
		 * @param entrySetBinder the entry set binder
		 */
		private RealMapBinder(Binder binder, TypeLiteral<V> valueType, Key<Map<K, V>> mapKey,
				Key<Map<K, Provider<V>>> providerMapKey, Multibinder<Map.Entry<K, Provider<V>>> entrySetBinder) {
			this.valueType = valueType;
			this.mapKey = mapKey;
			this.providerMapKey = providerMapKey;
			this.entrySetBinder = (RealMultibinder<Entry<K, Provider<V>>>) entrySetBinder;
			this.binder = binder;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.multibindings.MapBinder#addBinding(java.lang.Object)
		 */
		@Override
		public LinkedBindingBuilder<V> addBinding(K key) {
			Multibinder.checkNotNull(key, "key");
			Multibinder.checkConfiguration(!isInitialized(), "MapBinder was already initialized");

			Key<V> valueKey = Key.get(valueType, new RealElement(entrySetBinder.getSetName()));
			entrySetBinder.addBinding().toInstance(new MapEntry<K, Provider<V>>(key, binder.getProvider(valueKey)));
			return binder.bind(valueKey);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.Module#configure(cn.com.rebirth.search.commons.inject.Binder)
		 */
		public void configure(Binder binder) {
			Multibinder.checkConfiguration(!isInitialized(), "MapBinder was already initialized");

			final ImmutableSet<Dependency<?>> dependencies = ImmutableSet.<Dependency<?>> of(Dependency
					.get(entrySetBinder.getSetKey()));

			
			final Provider<Set<Entry<K, Provider<V>>>> entrySetProvider = binder
					.getProvider(entrySetBinder.getSetKey());
			binder.bind(providerMapKey).toProvider(new ProviderWithDependencies<Map<K, Provider<V>>>() {
				private Map<K, Provider<V>> providerMap;

				@SuppressWarnings("unused")
				@Inject
				void initialize() {
					RealMapBinder.this.binder = null;

					Map<K, Provider<V>> providerMapMutable = new LinkedHashMap<K, Provider<V>>();
					for (Entry<K, Provider<V>> entry : entrySetProvider.get()) {
						Multibinder.checkConfiguration(
								providerMapMutable.put(entry.getKey(), entry.getValue()) == null,
								"Map injection failed due to duplicated key \"%s\"", entry.getKey());
					}

					providerMap = Collections.unmodifiableMap(providerMapMutable);
				}

				public Map<K, Provider<V>> get() {
					return providerMap;
				}

				public Set<Dependency<?>> getDependencies() {
					return dependencies;
				}
			});

			final Provider<Map<K, Provider<V>>> mapProvider = binder.getProvider(providerMapKey);
			binder.bind(mapKey).toProvider(new ProviderWithDependencies<Map<K, V>>() {
				public Map<K, V> get() {
					Map<K, V> map = new LinkedHashMap<K, V>();
					for (Entry<K, Provider<V>> entry : mapProvider.get().entrySet()) {
						V value = entry.getValue().get();
						K key = entry.getKey();
						Multibinder.checkConfiguration(value != null,
								"Map injection failed due to null value for key \"%s\"", key);
						map.put(key, value);
					}
					return Collections.unmodifiableMap(map);
				}

				public Set<Dependency<?>> getDependencies() {
					return dependencies;
				}
			});
		}

		
		/**
		 * Checks if is initialized.
		 *
		 * @return true, if is initialized
		 */
		private boolean isInitialized() {
			return binder == null;
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			return o instanceof RealMapBinder && ((RealMapBinder<?, ?>) o).mapKey.equals(mapKey);
		}

		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return mapKey.hashCode();
		}

		
		/**
		 * The Class MapEntry.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @author l.xue.nong
		 */
		private static final class MapEntry<K, V> implements Map.Entry<K, V> {

			
			/** The key. */
			private final K key;

			
			/** The value. */
			private final V value;

			
			/**
			 * Instantiates a new map entry.
			 *
			 * @param key the key
			 * @param value the value
			 */
			private MapEntry(K key, V value) {
				this.key = key;
				this.value = value;
			}

			
			/* (non-Javadoc)
			 * @see java.util.Map.Entry#getKey()
			 */
			public K getKey() {
				return key;
			}

			
			/* (non-Javadoc)
			 * @see java.util.Map.Entry#getValue()
			 */
			public V getValue() {
				return value;
			}

			
			/* (non-Javadoc)
			 * @see java.util.Map.Entry#setValue(java.lang.Object)
			 */
			public V setValue(V value) {
				throw new UnsupportedOperationException();
			}

			
			/* (non-Javadoc)
			 * @see java.lang.Object#equals(java.lang.Object)
			 */
			@Override
			public boolean equals(Object obj) {
				return obj instanceof Map.Entry && key.equals(((Map.Entry<?, ?>) obj).getKey())
						&& value.equals(((Map.Entry<?, ?>) obj).getValue());
			}

			
			/* (non-Javadoc)
			 * @see java.lang.Object#hashCode()
			 */
			@Override
			public int hashCode() {
				return 127 * ("key".hashCode() ^ key.hashCode()) + 127 * ("value".hashCode() ^ value.hashCode());
			}

			
			/* (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "MapEntry(" + key + ", " + value + ")";
			}
		}
	}
}
