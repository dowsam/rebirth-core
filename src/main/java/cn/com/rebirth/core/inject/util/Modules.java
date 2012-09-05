/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Modules.java 2012-7-6 10:23:43 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.rebirth.core.inject.AbstractModule;
import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.Binding;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.Module;
import cn.com.rebirth.core.inject.PrivateBinder;
import cn.com.rebirth.core.inject.Scope;
import cn.com.rebirth.core.inject.spi.DefaultBindingScopingVisitor;
import cn.com.rebirth.core.inject.spi.DefaultElementVisitor;
import cn.com.rebirth.core.inject.spi.Element;
import cn.com.rebirth.core.inject.spi.Elements;
import cn.com.rebirth.core.inject.spi.PrivateElements;
import cn.com.rebirth.core.inject.spi.ScopeBinding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * The Class Modules.
 *
 * @author l.xue.nong
 */
public final class Modules {

	
	/**
	 * Instantiates a new modules.
	 */
	private Modules() {
	}

	
	/** The Constant EMPTY_MODULE. */
	public static final Module EMPTY_MODULE = new Module() {
		public void configure(Binder binder) {
		}
	};

	
	/**
	 * Override.
	 *
	 * @param modules the modules
	 * @return the overridden module builder
	 */
	public static OverriddenModuleBuilder override(Module... modules) {
		return new RealOverriddenModuleBuilder(Arrays.asList(modules));
	}

	
	/**
	 * Override.
	 *
	 * @param modules the modules
	 * @return the overridden module builder
	 */
	public static OverriddenModuleBuilder override(Iterable<? extends Module> modules) {
		return new RealOverriddenModuleBuilder(modules);
	}

	
	/**
	 * Combine.
	 *
	 * @param modules the modules
	 * @return the module
	 */
	public static Module combine(Module... modules) {
		return combine(ImmutableSet.copyOf(modules));
	}

	
	/**
	 * Combine.
	 *
	 * @param modules the modules
	 * @return the module
	 */
	public static Module combine(Iterable<? extends Module> modules) {
		final Set<Module> modulesSet = ImmutableSet.copyOf(modules);
		return new Module() {
			public void configure(Binder binder) {
				binder = binder.skipSources(getClass());
				for (Module module : modulesSet) {
					binder.install(module);
				}
			}
		};
	}

	
	/**
	 * The Interface OverriddenModuleBuilder.
	 *
	 * @author l.xue.nong
	 */
	public interface OverriddenModuleBuilder {

		
		/**
		 * With.
		 *
		 * @param overrides the overrides
		 * @return the module
		 */
		Module with(Module... overrides);

		
		/**
		 * With.
		 *
		 * @param overrides the overrides
		 * @return the module
		 */
		Module with(Iterable<? extends Module> overrides);
	}

	
	/**
	 * The Class RealOverriddenModuleBuilder.
	 *
	 * @author l.xue.nong
	 */
	private static final class RealOverriddenModuleBuilder implements OverriddenModuleBuilder {

		
		/** The base modules. */
		private final ImmutableSet<Module> baseModules;

		
		/**
		 * Instantiates a new real overridden module builder.
		 *
		 * @param baseModules the base modules
		 */
		private RealOverriddenModuleBuilder(Iterable<? extends Module> baseModules) {
			this.baseModules = ImmutableSet.copyOf(baseModules);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.util.Modules.OverriddenModuleBuilder#with(cn.com.rebirth.search.commons.inject.Module[])
		 */
		public Module with(Module... overrides) {
			return with(Arrays.asList(overrides));
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.util.Modules.OverriddenModuleBuilder#with(java.lang.Iterable)
		 */
		public Module with(final Iterable<? extends Module> overrides) {
			return new AbstractModule() {
				@SuppressWarnings("rawtypes")
				@Override
				public void configure() {
					final List<Element> elements = Elements.getElements(baseModules);
					final List<Element> overrideElements = Elements.getElements(overrides);

					final Set<Key> overriddenKeys = Sets.newHashSet();
					final Set<Class<? extends Annotation>> overridesScopeAnnotations = Sets.newHashSet();

					
					new ModuleWriter(binder()) {
						@Override
						public <T> Void visit(Binding<T> binding) {
							overriddenKeys.add(binding.getKey());
							return super.visit(binding);
						}

						@Override
						public Void visit(ScopeBinding scopeBinding) {
							overridesScopeAnnotations.add(scopeBinding.getAnnotationType());
							return super.visit(scopeBinding);
						}

						@Override
						public Void visit(PrivateElements privateElements) {
							overriddenKeys.addAll(privateElements.getExposedKeys());
							return super.visit(privateElements);
						}
					}.writeAll(overrideElements);

					
					
					
					final Map<Scope, Object> scopeInstancesInUse = Maps.newHashMap();
					final List<ScopeBinding> scopeBindings = Lists.newArrayList();
					new ModuleWriter(binder()) {
						@Override
						public <T> Void visit(Binding<T> binding) {
							if (!overriddenKeys.remove(binding.getKey())) {
								super.visit(binding);

								
								Scope scope = getScopeInstanceOrNull(binding);
								if (scope != null) {
									scopeInstancesInUse.put(scope, binding.getSource());
								}
							}

							return null;
						}

						@Override
						public Void visit(PrivateElements privateElements) {
							PrivateBinder privateBinder = binder.withSource(privateElements.getSource())
									.newPrivateBinder();

							Set<Key<?>> skippedExposes = Sets.newHashSet();

							for (Key<?> key : privateElements.getExposedKeys()) {
								if (overriddenKeys.remove(key)) {
									skippedExposes.add(key);
								} else {
									privateBinder.withSource(privateElements.getExposedSource(key)).expose(key);
								}
							}

							
							
							for (Element element : privateElements.getElements()) {
								if (element instanceof Binding && skippedExposes.contains(((Binding) element).getKey())) {
									continue;
								}
								element.applyTo(privateBinder);
							}

							return null;
						}

						@Override
						public Void visit(ScopeBinding scopeBinding) {
							scopeBindings.add(scopeBinding);
							return null;
						}
					}.writeAll(elements);

					
					
					new ModuleWriter(binder()) {
						@Override
						public Void visit(ScopeBinding scopeBinding) {
							if (!overridesScopeAnnotations.remove(scopeBinding.getAnnotationType())) {
								super.visit(scopeBinding);
							} else {
								Object source = scopeInstancesInUse.get(scopeBinding.getScope());
								if (source != null) {
									binder().withSource(source).addError(
											"The scope for @%s is bound directly and cannot be overridden.",
											scopeBinding.getAnnotationType().getSimpleName());
								}
							}
							return null;
						}
					}.writeAll(scopeBindings);

				}

				private Scope getScopeInstanceOrNull(Binding<?> binding) {
					return binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Scope>() {
						public Scope visitScope(Scope scope) {
							return scope;
						}
					});
				}
			};
		}
	}

	
	/**
	 * The Class ModuleWriter.
	 *
	 * @author l.xue.nong
	 */
	private static class ModuleWriter extends DefaultElementVisitor<Void> {

		
		/** The binder. */
		protected final Binder binder;

		
		/**
		 * Instantiates a new module writer.
		 *
		 * @param binder the binder
		 */
		ModuleWriter(Binder binder) {
			this.binder = binder;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.inject.spi.DefaultElementVisitor#visitOther(cn.com.rebirth.search.commons.inject.spi.Element)
		 */
		@Override
		protected Void visitOther(Element element) {
			element.applyTo(binder);
			return null;
		}

		
		/**
		 * Write all.
		 *
		 * @param elements the elements
		 */
		void writeAll(Iterable<? extends Element> elements) {
			for (Element element : elements) {
				element.acceptVisitor(this);
			}
		}
	}
}
