/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons CreationException.java 2012-7-6 10:23:51 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableSet;


/**
 * The Class CreationException.
 *
 * @author l.xue.nong
 */
public class CreationException extends RuntimeException {

	
	/** The messages. */
	private final ImmutableSet<Message> messages;

	
	/**
	 * Instantiates a new creation exception.
	 *
	 * @param messages the messages
	 */
	public CreationException(Collection<Message> messages) {
		this.messages = ImmutableSet.copyOf(messages);
		checkArgument(!this.messages.isEmpty());
		initCause(Errors.getOnlyCause(this.messages));
	}

	
	/**
	 * Gets the error messages.
	 *
	 * @return the error messages
	 */
	public Collection<Message> getErrorMessages() {
		return messages;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return Errors.format("Guice creation errors", messages);
	}

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0;
}
