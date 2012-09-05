/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ProvisionException.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


/**
 * The Class ProvisionException.
 *
 * @author l.xue.nong
 */
public final class ProvisionException extends RuntimeException {

    
    /** The messages. */
    private final ImmutableSet<Message> messages;

    
    /**
     * Instantiates a new provision exception.
     *
     * @param messages the messages
     */
    public ProvisionException(Iterable<Message> messages) {
        this.messages = ImmutableSet.copyOf(messages);
        checkArgument(!this.messages.isEmpty());
        initCause(Errors.getOnlyCause(this.messages));
    }

    
    /**
     * Instantiates a new provision exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ProvisionException(String message, Throwable cause) {
        super(cause);
        this.messages = ImmutableSet.of(new Message(ImmutableList.of(), message, cause));
    }

    
    /**
     * Instantiates a new provision exception.
     *
     * @param message the message
     */
    public ProvisionException(String message) {
        this.messages = ImmutableSet.of(new Message(message));
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
        return Errors.format("Guice provision errors", messages);
    }

    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0;
}
