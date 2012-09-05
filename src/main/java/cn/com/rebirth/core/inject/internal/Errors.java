/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Errors.java 2012-7-6 10:23:40 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

import cn.com.rebirth.core.inject.ConfigurationException;
import cn.com.rebirth.core.inject.CreationException;
import cn.com.rebirth.core.inject.Key;
import cn.com.rebirth.core.inject.MembersInjector;
import cn.com.rebirth.core.inject.Provider;
import cn.com.rebirth.core.inject.ProvisionException;
import cn.com.rebirth.core.inject.Scope;
import cn.com.rebirth.core.inject.TypeLiteral;
import cn.com.rebirth.core.inject.spi.Dependency;
import cn.com.rebirth.core.inject.spi.InjectionListener;
import cn.com.rebirth.core.inject.spi.InjectionPoint;
import cn.com.rebirth.core.inject.spi.Message;
import cn.com.rebirth.core.inject.spi.TypeListenerBinding;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * The Class Errors.
 *
 * @author l.xue.nong
 */
public final class Errors implements Serializable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8334544761469367370L;

	
	/** The root. */
	private final Errors root;

	
	/** The parent. */
	private final Errors parent;

	
	/** The source. */
	private final Object source;

	
	/** The errors. */
	private List<Message> errors; 

	
	/**
	 * Instantiates a new errors.
	 */
	public Errors() {
		this.root = this;
		this.parent = null;
		this.source = SourceProvider.UNKNOWN_SOURCE;
	}

	
	/**
	 * Instantiates a new errors.
	 *
	 * @param source the source
	 */
	public Errors(Object source) {
		this.root = this;
		this.parent = null;
		this.source = source;
	}

	
	/**
	 * Instantiates a new errors.
	 *
	 * @param parent the parent
	 * @param source the source
	 */
	private Errors(Errors parent, Object source) {
		this.root = parent.root;
		this.parent = parent;
		this.source = source;
	}

	
	/**
	 * With source.
	 *
	 * @param source the source
	 * @return the errors
	 */
	public Errors withSource(Object source) {
		return source == SourceProvider.UNKNOWN_SOURCE ? this : new Errors(this, source);
	}

	
	/**
	 * Missing implementation.
	 *
	 * @param key the key
	 * @return the errors
	 */
	public Errors missingImplementation(Key<?> key) {
		return addMessage("No implementation for %s was bound.", key);
	}

	
	/**
	 * Converter returned null.
	 *
	 * @param stringValue the string value
	 * @param source the source
	 * @param type the type
	 * @param matchingConverter the matching converter
	 * @return the errors
	 */
	public Errors converterReturnedNull(String stringValue, Object source, TypeLiteral<?> type,
			MatcherAndConverter matchingConverter) {
		return addMessage("Received null converting '%s' (bound at %s) to %s%n" + " using %s.", stringValue,
				convert(source), type, matchingConverter);
	}

	
	/**
	 * Conversion type error.
	 *
	 * @param stringValue the string value
	 * @param source the source
	 * @param type the type
	 * @param matchingConverter the matching converter
	 * @param converted the converted
	 * @return the errors
	 */
	public Errors conversionTypeError(String stringValue, Object source, TypeLiteral<?> type,
			MatcherAndConverter matchingConverter, Object converted) {
		return addMessage("Type mismatch converting '%s' (bound at %s) to %s%n" + " using %s.%n"
				+ " Converter returned %s.", stringValue, convert(source), type, matchingConverter, converted);
	}

	
	/**
	 * Conversion error.
	 *
	 * @param stringValue the string value
	 * @param source the source
	 * @param type the type
	 * @param matchingConverter the matching converter
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors conversionError(String stringValue, Object source, TypeLiteral<?> type,
			MatcherAndConverter matchingConverter, RuntimeException cause) {
		return errorInUserCode(cause, "Error converting '%s' (bound at %s) to %s%n" + " using %s.%n" + " Reason: %s",
				stringValue, convert(source), type, matchingConverter, cause);
	}

	
	/**
	 * Ambiguous type conversion.
	 *
	 * @param stringValue the string value
	 * @param source the source
	 * @param type the type
	 * @param a the a
	 * @param b the b
	 * @return the errors
	 */
	public Errors ambiguousTypeConversion(String stringValue, Object source, TypeLiteral<?> type,
			MatcherAndConverter a, MatcherAndConverter b) {
		return addMessage("Multiple converters can convert '%s' (bound at %s) to %s:%n" + " %s and%n" + " %s.%n"
				+ " Please adjust your type converter configuration to avoid overlapping matches.", stringValue,
				convert(source), type, a, b);
	}

	
	/**
	 * Binding to provider.
	 *
	 * @return the errors
	 */
	public Errors bindingToProvider() {
		return addMessage("Binding to Provider is not allowed.");
	}

	
	/**
	 * Subtype not provided.
	 *
	 * @param providerType the provider type
	 * @param type the type
	 * @return the errors
	 */
	public Errors subtypeNotProvided(Class<? extends Provider<?>> providerType, Class<?> type) {
		return addMessage("%s doesn't provide instances of %s.", providerType, type);
	}

	
	/**
	 * Not a subtype.
	 *
	 * @param implementationType the implementation type
	 * @param type the type
	 * @return the errors
	 */
	public Errors notASubtype(Class<?> implementationType, Class<?> type) {
		return addMessage("%s doesn't extend %s.", implementationType, type);
	}

	
	/**
	 * Recursive implementation type.
	 *
	 * @return the errors
	 */
	public Errors recursiveImplementationType() {
		return addMessage("@ImplementedBy points to the same class it annotates.");
	}

	
	/**
	 * Recursive provider type.
	 *
	 * @return the errors
	 */
	public Errors recursiveProviderType() {
		return addMessage("@ProvidedBy points to the same class it annotates.");
	}

	
	/**
	 * Missing runtime retention.
	 *
	 * @param source the source
	 * @return the errors
	 */
	public Errors missingRuntimeRetention(Object source) {
		return addMessage("Please annotate with @Retention(RUNTIME).%n" + " Bound at %s.", convert(source));
	}

	
	/**
	 * Missing scope annotation.
	 *
	 * @return the errors
	 */
	public Errors missingScopeAnnotation() {
		return addMessage("Please annotate with @ScopeAnnotation.");
	}

	
	/**
	 * Optional constructor.
	 *
	 * @param constructor the constructor
	 * @return the errors
	 */
	public Errors optionalConstructor(Constructor<?> constructor) {
		return addMessage("%s is annotated @Inject(optional=true), " + "but constructors cannot be optional.",
				constructor);
	}

	
	/**
	 * Cannot bind to guice type.
	 *
	 * @param simpleName the simple name
	 * @return the errors
	 */
	public Errors cannotBindToGuiceType(String simpleName) {
		return addMessage("Binding to core guice framework type is not allowed: %s.", simpleName);
	}

	
	/**
	 * Scope not found.
	 *
	 * @param scopeAnnotation the scope annotation
	 * @return the errors
	 */
	public Errors scopeNotFound(Class<? extends Annotation> scopeAnnotation) {
		return addMessage("No scope is bound to %s.", scopeAnnotation);
	}

	
	/**
	 * Scope annotation on abstract type.
	 *
	 * @param scopeAnnotation the scope annotation
	 * @param type the type
	 * @param source the source
	 * @return the errors
	 */
	public Errors scopeAnnotationOnAbstractType(Class<? extends Annotation> scopeAnnotation, Class<?> type,
			Object source) {
		return addMessage("%s is annotated with %s, but scope annotations are not supported "
				+ "for abstract types.%n Bound at %s.", type, scopeAnnotation, convert(source));
	}

	
	/**
	 * Misplaced binding annotation.
	 *
	 * @param member the member
	 * @param bindingAnnotation the binding annotation
	 * @return the errors
	 */
	public Errors misplacedBindingAnnotation(Member member, Annotation bindingAnnotation) {
		return addMessage("%s is annotated with %s, but binding annotations should be applied "
				+ "to its parameters instead.", member, bindingAnnotation);
	}

	
	/** The Constant CONSTRUCTOR_RULES. */
	private static final String CONSTRUCTOR_RULES = "Classes must have either one (and only one) constructor "
			+ "annotated with @Inject or a zero-argument constructor that is not private.";

	
	/**
	 * Missing constructor.
	 *
	 * @param implementation the implementation
	 * @return the errors
	 */
	public Errors missingConstructor(Class<?> implementation) {
		return addMessage("Could not find a suitable constructor in %s. " + CONSTRUCTOR_RULES, implementation);
	}

	
	/**
	 * Too many constructors.
	 *
	 * @param implementation the implementation
	 * @return the errors
	 */
	public Errors tooManyConstructors(Class<?> implementation) {
		return addMessage("%s has more than one constructor annotated with @Inject. " + CONSTRUCTOR_RULES,
				implementation);
	}

	
	/**
	 * Duplicate scopes.
	 *
	 * @param existing the existing
	 * @param annotationType the annotation type
	 * @param scope the scope
	 * @return the errors
	 */
	public Errors duplicateScopes(Scope existing, Class<? extends Annotation> annotationType, Scope scope) {
		return addMessage("Scope %s is already bound to %s. Cannot bind %s.", existing, annotationType, scope);
	}

	
	/**
	 * Void provider method.
	 *
	 * @return the errors
	 */
	public Errors voidProviderMethod() {
		return addMessage("Provider methods must return a value. Do not return void.");
	}

	
	/**
	 * Missing constant values.
	 *
	 * @return the errors
	 */
	public Errors missingConstantValues() {
		return addMessage("Missing constant value. Please call to(...).");
	}

	
	/**
	 * Cannot inject inner class.
	 *
	 * @param type the type
	 * @return the errors
	 */
	public Errors cannotInjectInnerClass(Class<?> type) {
		return addMessage("Injecting into inner classes is not supported.  "
				+ "Please use a 'static' class (top-level or nested) instead of %s.", type);
	}

	
	/**
	 * Duplicate binding annotations.
	 *
	 * @param member the member
	 * @param a the a
	 * @param b the b
	 * @return the errors
	 */
	public Errors duplicateBindingAnnotations(Member member, Class<? extends Annotation> a,
			Class<? extends Annotation> b) {
		return addMessage("%s has more than one annotation annotated with @BindingAnnotation: " + "%s and %s", member,
				a, b);
	}

	
	/**
	 * Duplicate scope annotations.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the errors
	 */
	public Errors duplicateScopeAnnotations(Class<? extends Annotation> a, Class<? extends Annotation> b) {
		return addMessage("More than one scope annotation was found: %s and %s.", a, b);
	}

	
	/**
	 * Recursive binding.
	 *
	 * @return the errors
	 */
	public Errors recursiveBinding() {
		return addMessage("Binding points to itself.");
	}

	
	/**
	 * Binding already set.
	 *
	 * @param key the key
	 * @param source the source
	 * @return the errors
	 */
	public Errors bindingAlreadySet(Key<?> key, Object source) {
		return addMessage("A binding to %s was already configured at %s.", key, convert(source));
	}

	
	/**
	 * Child binding already set.
	 *
	 * @param key the key
	 * @return the errors
	 */
	public Errors childBindingAlreadySet(Key<?> key) {
		return addMessage("A binding to %s already exists on a child injector.", key);
	}

	
	/**
	 * Error injecting method.
	 *
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors errorInjectingMethod(Throwable cause) {
		return errorInUserCode(cause, "Error injecting method, %s", cause);
	}

	
	/**
	 * Error notifying type listener.
	 *
	 * @param listener the listener
	 * @param type the type
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors errorNotifyingTypeListener(TypeListenerBinding listener, TypeLiteral<?> type, Throwable cause) {
		return errorInUserCode(cause, "Error notifying TypeListener %s (bound at %s) of %s.%n" + " Reason: %s",
				listener.getListener(), convert(listener.getSource()), type, cause);
	}

	
	/**
	 * Error injecting constructor.
	 *
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors errorInjectingConstructor(Throwable cause) {
		return errorInUserCode(cause, "Error injecting constructor, %s", cause);
	}

	
	/**
	 * Error in provider.
	 *
	 * @param runtimeException the runtime exception
	 * @return the errors
	 */
	public Errors errorInProvider(RuntimeException runtimeException) {
		return errorInUserCode(runtimeException, "Error in custom provider, %s", runtimeException);
	}

	
	/**
	 * Error in user injector.
	 *
	 * @param listener the listener
	 * @param type the type
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors errorInUserInjector(MembersInjector<?> listener, TypeLiteral<?> type, RuntimeException cause) {
		return errorInUserCode(cause, "Error injecting %s using %s.%n" + " Reason: %s", type, listener, cause);
	}

	
	/**
	 * Error notifying injection listener.
	 *
	 * @param listener the listener
	 * @param type the type
	 * @param cause the cause
	 * @return the errors
	 */
	public Errors errorNotifyingInjectionListener(InjectionListener<?> listener, TypeLiteral<?> type,
			RuntimeException cause) {
		return errorInUserCode(cause, "Error notifying InjectionListener %s of %s.%n" + " Reason: %s", listener, type,
				cause);
	}

	
	/**
	 * Exposed but not bound.
	 *
	 * @param key the key
	 */
	public void exposedButNotBound(Key<?> key) {
		addMessage("Could not expose() %s, it must be explicitly bound.", key);
	}

	
	/**
	 * Gets the messages from throwable.
	 *
	 * @param throwable the throwable
	 * @return the messages from throwable
	 */
	public static Collection<Message> getMessagesFromThrowable(Throwable throwable) {
		if (throwable instanceof ProvisionException) {
			return ((ProvisionException) throwable).getErrorMessages();
		} else if (throwable instanceof ConfigurationException) {
			return ((ConfigurationException) throwable).getErrorMessages();
		} else if (throwable instanceof CreationException) {
			return ((CreationException) throwable).getErrorMessages();
		} else {
			return ImmutableSet.of();
		}
	}

	
	/**
	 * Error in user code.
	 *
	 * @param cause the cause
	 * @param messageFormat the message format
	 * @param arguments the arguments
	 * @return the errors
	 */
	public Errors errorInUserCode(Throwable cause, String messageFormat, Object... arguments) {
		Collection<Message> messages = getMessagesFromThrowable(cause);

		if (!messages.isEmpty()) {
			return merge(messages);
		} else {
			return addMessage(cause, messageFormat, arguments);
		}
	}

	
	/**
	 * Cannot inject raw provider.
	 *
	 * @return the errors
	 */
	public Errors cannotInjectRawProvider() {
		return addMessage("Cannot inject a Provider that has no type parameter");
	}

	
	/**
	 * Cannot inject raw members injector.
	 *
	 * @return the errors
	 */
	public Errors cannotInjectRawMembersInjector() {
		return addMessage("Cannot inject a MembersInjector that has no type parameter");
	}

	
	/**
	 * Cannot inject type literal of.
	 *
	 * @param unsupportedType the unsupported type
	 * @return the errors
	 */
	public Errors cannotInjectTypeLiteralOf(Type unsupportedType) {
		return addMessage("Cannot inject a TypeLiteral of %s", unsupportedType);
	}

	
	/**
	 * Cannot inject raw type literal.
	 *
	 * @return the errors
	 */
	public Errors cannotInjectRawTypeLiteral() {
		return addMessage("Cannot inject a TypeLiteral that has no type parameter");
	}

	
	/**
	 * Cannot satisfy circular dependency.
	 *
	 * @param expectedType the expected type
	 * @return the errors
	 */
	public Errors cannotSatisfyCircularDependency(Class<?> expectedType) {
		return addMessage("Tried proxying %s to support a circular dependency, but it is not an interface.",
				expectedType);
	}

	
	/**
	 * Throw creation exception if errors exist.
	 */
	public void throwCreationExceptionIfErrorsExist() {
		if (!hasErrors()) {
			return;
		}

		throw new CreationException(getMessages());
	}

	
	/**
	 * Throw configuration exception if errors exist.
	 */
	public void throwConfigurationExceptionIfErrorsExist() {
		if (!hasErrors()) {
			return;
		}

		throw new ConfigurationException(getMessages());
	}

	
	/**
	 * Throw provision exception if errors exist.
	 */
	public void throwProvisionExceptionIfErrorsExist() {
		if (!hasErrors()) {
			return;
		}

		throw new ProvisionException(getMessages());
	}

	
	/**
	 * Merge.
	 *
	 * @param message the message
	 * @return the message
	 */
	private Message merge(Message message) {
		List<Object> sources = Lists.newArrayList();
		sources.addAll(getSources());
		sources.addAll(message.getSources());
		return new Message(sources, message.getMessage(), message.getCause());
	}

	
	/**
	 * Merge.
	 *
	 * @param messages the messages
	 * @return the errors
	 */
	public Errors merge(Collection<Message> messages) {
		for (Message message : messages) {
			addMessage(merge(message));
		}
		return this;
	}

	
	/**
	 * Merge.
	 *
	 * @param moreErrors the more errors
	 * @return the errors
	 */
	public Errors merge(Errors moreErrors) {
		if (moreErrors.root == root || moreErrors.root.errors == null) {
			return this;
		}

		merge(moreErrors.root.errors);
		return this;
	}

	
	/**
	 * Gets the sources.
	 *
	 * @return the sources
	 */
	public List<Object> getSources() {
		List<Object> sources = Lists.newArrayList();
		for (Errors e = this; e != null; e = e.parent) {
			if (e.source != SourceProvider.UNKNOWN_SOURCE) {
				sources.add(0, e.source);
			}
		}
		return sources;
	}

	
	/**
	 * Throw if new errors.
	 *
	 * @param expectedSize the expected size
	 * @throws ErrorsException the errors exception
	 */
	public void throwIfNewErrors(int expectedSize) throws ErrorsException {
		if (size() == expectedSize) {
			return;
		}

		throw toException();
	}

	
	/**
	 * To exception.
	 *
	 * @return the errors exception
	 */
	public ErrorsException toException() {
		return new ErrorsException(this);
	}

	
	/**
	 * Checks for errors.
	 *
	 * @return true, if successful
	 */
	public boolean hasErrors() {
		return root.errors != null;
	}

	
	/**
	 * Adds the message.
	 *
	 * @param messageFormat the message format
	 * @param arguments the arguments
	 * @return the errors
	 */
	public Errors addMessage(String messageFormat, Object... arguments) {
		return addMessage(null, messageFormat, arguments);
	}

	
	/**
	 * Adds the message.
	 *
	 * @param cause the cause
	 * @param messageFormat the message format
	 * @param arguments the arguments
	 * @return the errors
	 */
	private Errors addMessage(Throwable cause, String messageFormat, Object... arguments) {
		String message = format(messageFormat, arguments);
		addMessage(new Message(getSources(), message, cause));
		return this;
	}

	
	/**
	 * Adds the message.
	 *
	 * @param message the message
	 * @return the errors
	 */
	public Errors addMessage(Message message) {
		if (root.errors == null) {
			root.errors = Lists.newArrayList();
		}
		root.errors.add(message);
		return this;
	}

	
	/**
	 * Format.
	 *
	 * @param messageFormat the message format
	 * @param arguments the arguments
	 * @return the string
	 */
	public static String format(String messageFormat, Object... arguments) {
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = Errors.convert(arguments[i]);
		}
		return String.format(messageFormat, arguments);
	}

	
	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public List<Message> getMessages() {
		if (root.errors == null) {
			return ImmutableList.of();
		}

		List<Message> result = Lists.newArrayList(root.errors);
		Collections.sort(result, new Comparator<Message>() {
			public int compare(Message a, Message b) {
				return a.getSource().compareTo(b.getSource());
			}
		});

		return result;
	}

	
	/**
	 * Format.
	 *
	 * @param heading the heading
	 * @param errorMessages the error messages
	 * @return the string
	 */
	public static String format(String heading, Collection<Message> errorMessages) {
		Formatter fmt = new Formatter().format(heading).format(":%n%n");
		int index = 1;
		boolean displayCauses = getOnlyCause(errorMessages) == null;

		for (Message errorMessage : errorMessages) {
			fmt.format("%s) %s%n", index++, errorMessage.getMessage());

			List<Object> dependencies = errorMessage.getSources();
			for (int i = dependencies.size() - 1; i >= 0; i--) {
				Object source = dependencies.get(i);
				formatSource(fmt, source);
			}

			Throwable cause = errorMessage.getCause();
			if (displayCauses && cause != null) {
				StringWriter writer = new StringWriter();
				cause.printStackTrace(new PrintWriter(writer));
				fmt.format("Caused by: %s", writer.getBuffer());
			}

			fmt.format("%n");
		}

		if (errorMessages.size() == 1) {
			fmt.format("1 error");
		} else {
			fmt.format("%s errors", errorMessages.size());
		}

		return fmt.toString();
	}

	
	/**
	 * Check for null.
	 *
	 * @param <T> the generic type
	 * @param value the value
	 * @param source the source
	 * @param dependency the dependency
	 * @return the t
	 * @throws ErrorsException the errors exception
	 */
	public <T> T checkForNull(T value, Object source, Dependency<?> dependency) throws ErrorsException {
		if (value != null || dependency.isNullable()) {
			return value;
		}

		int parameterIndex = dependency.getParameterIndex();
		String parameterName = (parameterIndex != -1) ? "parameter " + parameterIndex + " of " : "";
		addMessage("null returned by binding at %s%n but %s%s is not @Nullable", source, parameterName, dependency
				.getInjectionPoint().getMember());

		throw toException();
	}

	
	/**
	 * Gets the only cause.
	 *
	 * @param messages the messages
	 * @return the only cause
	 */
	public static Throwable getOnlyCause(Collection<Message> messages) {
		Throwable onlyCause = null;
		for (Message message : messages) {
			Throwable messageCause = message.getCause();
			if (messageCause == null) {
				continue;
			}

			if (onlyCause != null) {
				return null;
			}

			onlyCause = messageCause;
		}

		return onlyCause;
	}

	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return root.errors == null ? 0 : root.errors.size();
	}

	
	/**
	 * The Class Converter.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	private static abstract class Converter<T> {

		
		/** The type. */
		final Class<T> type;

		
		/**
		 * Instantiates a new converter.
		 *
		 * @param type the type
		 */
		Converter(Class<T> type) {
			this.type = type;
		}

		
		/**
		 * Applies to.
		 *
		 * @param o the o
		 * @return true, if successful
		 */
		boolean appliesTo(Object o) {
			return type.isAssignableFrom(o.getClass());
		}

		
		/**
		 * Convert.
		 *
		 * @param o the o
		 * @return the string
		 */
		String convert(Object o) {
			return toString(type.cast(o));
		}

		
		/**
		 * To string.
		 *
		 * @param t the t
		 * @return the string
		 */
		abstract String toString(T t);
	}

	
	/** The Constant converters. */
	@SuppressWarnings("rawtypes")
	private static final Collection<Converter<?>> converters = ImmutableList.of(new Converter<Class>(Class.class) {
		public String toString(Class c) {
			return c.getName();
		}
	}, new Converter<Member>(Member.class) {
		public String toString(Member member) {
			return MoreTypes.toString(member);
		}
	}, new Converter<Key>(Key.class) {
		public String toString(Key key) {
			if (key.getAnnotationType() != null) {
				return key.getTypeLiteral() + " annotated with "
						+ (key.getAnnotation() != null ? key.getAnnotation() : key.getAnnotationType());
			} else {
				return key.getTypeLiteral().toString();
			}
		}
	});

	
	/**
	 * Convert.
	 *
	 * @param o the o
	 * @return the object
	 */
	public static Object convert(Object o) {
		for (Converter<?> converter : converters) {
			if (converter.appliesTo(o)) {
				return converter.convert(o);
			}
		}
		return o;
	}

	
	/**
	 * Format source.
	 *
	 * @param formatter the formatter
	 * @param source the source
	 */
	public static void formatSource(Formatter formatter, Object source) {
		if (source instanceof Dependency) {
			Dependency<?> dependency = (Dependency<?>) source;
			InjectionPoint injectionPoint = dependency.getInjectionPoint();
			if (injectionPoint != null) {
				formatInjectionPoint(formatter, dependency, injectionPoint);
			} else {
				formatSource(formatter, dependency.getKey());
			}

		} else if (source instanceof InjectionPoint) {
			formatInjectionPoint(formatter, null, (InjectionPoint) source);

		} else if (source instanceof Class) {
			formatter.format("  at %s%n", StackTraceElements.forType((Class<?>) source));

		} else if (source instanceof Member) {
			formatter.format("  at %s%n", StackTraceElements.forMember((Member) source));

		} else if (source instanceof TypeLiteral) {
			formatter.format("  while locating %s%n", source);

		} else if (source instanceof Key) {
			Key<?> key = (Key<?>) source;
			formatter.format("  while locating %s%n", convert(key));

		} else {
			formatter.format("  at %s%n", source);
		}
	}

	
	/**
	 * Format injection point.
	 *
	 * @param formatter the formatter
	 * @param dependency the dependency
	 * @param injectionPoint the injection point
	 */
	public static void formatInjectionPoint(Formatter formatter, Dependency<?> dependency, InjectionPoint injectionPoint) {
		Member member = injectionPoint.getMember();
		Class<? extends Member> memberType = MoreTypes.memberType(member);

		if (memberType == Field.class) {
			dependency = injectionPoint.getDependencies().get(0);
			formatter.format("  while locating %s%n", convert(dependency.getKey()));
			formatter.format("    for field at %s%n", StackTraceElements.forMember(member));

		} else if (dependency != null) {
			formatter.format("  while locating %s%n", convert(dependency.getKey()));
			formatter.format("    for parameter %s at %s%n", dependency.getParameterIndex(),
					StackTraceElements.forMember(member));

		} else {
			formatSource(formatter, injectionPoint.getMember());
		}
	}
}
