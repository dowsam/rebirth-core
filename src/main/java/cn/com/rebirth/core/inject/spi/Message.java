/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Message.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

import cn.com.rebirth.core.inject.Binder;
import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.internal.Objects;
import cn.com.rebirth.core.inject.internal.SourceProvider;

import com.google.common.collect.ImmutableList;


/**
 * The Class Message.
 *
 * @author l.xue.nong
 */
public final class Message implements Serializable, Element {

	
	/** The message. */
	private final String message;

	
	/** The cause. */
	private final Throwable cause;

	
	/** The sources. */
	private final List<Object> sources;

	
	/**
	 * Instantiates a new message.
	 *
	 * @param sources the sources
	 * @param message the message
	 * @param cause the cause
	 */
	public Message(List<Object> sources, String message, Throwable cause) {
		this.sources = ImmutableList.copyOf(sources);
		this.message = checkNotNull(message, "message");
		this.cause = cause;
	}

	
	/**
	 * Instantiates a new message.
	 *
	 * @param source the source
	 * @param message the message
	 */
	public Message(Object source, String message) {
		this(ImmutableList.of(source), message, null);
	}

	
	/**
	 * Instantiates a new message.
	 *
	 * @param message the message
	 */
	public Message(String message) {
		this(ImmutableList.of(), message, null);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#getSource()
	 */
	public String getSource() {
		return sources.isEmpty() ? SourceProvider.UNKNOWN_SOURCE.toString() : Errors.convert(
				sources.get(sources.size() - 1)).toString();
	}

	
	/**
	 * Gets the sources.
	 *
	 * @return the sources
	 */
	public List<Object> getSources() {
		return sources;
	}

	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#acceptVisitor(cn.com.rebirth.search.commons.inject.spi.ElementVisitor)
	 */
	public <T> T acceptVisitor(ElementVisitor<T> visitor) {
		return visitor.visit(this);
	}

	
	/**
	 * Gets the cause.
	 *
	 * @return the cause
	 */
	public Throwable getCause() {
		return cause;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return message;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return message.hashCode();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Message)) {
			return false;
		}
		Message e = (Message) o;
		return message.equals(e.message) && Objects.equal(cause, e.cause) && sources.equals(e.sources);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.inject.spi.Element#applyTo(cn.com.rebirth.search.commons.inject.Binder)
	 */
	public void applyTo(Binder binder) {
		binder.withSource(getSource()).addError(this);
	}

	
	/**
	 * Write replace.
	 *
	 * @return the object
	 * @throws ObjectStreamException the object stream exception
	 */
	private Object writeReplace() throws ObjectStreamException {
		Object[] sourcesAsStrings = sources.toArray();
		for (int i = 0; i < sourcesAsStrings.length; i++) {
			sourcesAsStrings[i] = Errors.convert(sourcesAsStrings[i]).toString();
		}
		return new Message(ImmutableList.copyOf(sourcesAsStrings), message, cause);
	}

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0;
}