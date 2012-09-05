/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ConfigurationException.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.core.inject;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import cn.com.rebirth.core.inject.internal.Errors;
import cn.com.rebirth.core.inject.spi.Message;

import com.google.common.collect.ImmutableSet;


/**
 * The Class ConfigurationException.
 *
 * @author l.xue.nong
 */
public final class ConfigurationException extends RuntimeException {

    
    /** The messages. */
    private final ImmutableSet<Message> messages;
    
    
    /** The partial value. */
    private Object partialValue = null;

    
    /**
     * Instantiates a new configuration exception.
     *
     * @param messages the messages
     */
    public ConfigurationException(Iterable<Message> messages) {
        this.messages = ImmutableSet.copyOf(messages);
        initCause(Errors.getOnlyCause(this.messages));
    }

    
    /**
     * With partial value.
     *
     * @param partialValue the partial value
     * @return the configuration exception
     */
    public ConfigurationException withPartialValue(Object partialValue) {
        checkState(this.partialValue == null,
                "Can't clobber existing partial value %s with %s", this.partialValue, partialValue);
        ConfigurationException result = new ConfigurationException(messages);
        result.partialValue = partialValue;
        return result;
    }

    
    /**
     * Gets the error messages.
     *
     * @return the error messages
     */
    public Collection<Message> getErrorMessages() {
        return messages;
    }

    
    /**
     * Gets the partial value.
     *
     * @param <E> the element type
     * @return the partial value
     */
    @SuppressWarnings("unchecked") 
    public <E> E getPartialValue() {
        return (E) partialValue;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return Errors.format("Guice configuration errors", messages);
    }

    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0;
}