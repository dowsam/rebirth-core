/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Join.java 2012-7-6 10:23:48 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The Class Join.
 *
 * @author l.xue.nong
 */
public final class Join {
    
    
    /**
     * Instantiates a new join.
     */
    private Join() {
    }

    
    /**
     * Join.
     *
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the string
     */
    public static String join(String delimiter, Iterable<?> tokens) {
        return join(delimiter, tokens.iterator());
    }

    
    /**
     * Join.
     *
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the string
     */
    public static String join(String delimiter, Object[] tokens) {
        return join(delimiter, Arrays.asList(tokens));
    }

    
    /**
     * Join.
     *
     * @param delimiter the delimiter
     * @param firstToken the first token
     * @param otherTokens the other tokens
     * @return the string
     */
    public static String join(
            String delimiter, @Nullable Object firstToken, Object... otherTokens) {
        checkNotNull(otherTokens);
        return join(delimiter, Lists.newArrayList(firstToken, otherTokens));
    }

    
    /**
     * Join.
     *
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the string
     */
    public static String join(String delimiter, Iterator<?> tokens) {
        StringBuilder sb = new StringBuilder();
        join(sb, delimiter, tokens);
        return sb.toString();
    }

    
    /**
     * Join.
     *
     * @param keyValueSeparator the key value separator
     * @param entryDelimiter the entry delimiter
     * @param map the map
     * @return the string
     */
    public static String join(
            String keyValueSeparator, String entryDelimiter, Map<?, ?> map) {
        return join(new StringBuilder(), keyValueSeparator, entryDelimiter, map)
                .toString();
    }

    
    /**
     * Join.
     *
     * @param <T> the generic type
     * @param appendable the appendable
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the t
     */
    public static <T extends Appendable> T join(
            T appendable, String delimiter, Iterable<?> tokens) {
        return join(appendable, delimiter, tokens.iterator());
    }

    
    /**
     * Join.
     *
     * @param <T> the generic type
     * @param appendable the appendable
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the t
     */
    public static <T extends Appendable> T join(
            T appendable, String delimiter, Object[] tokens) {
        return join(appendable, delimiter, Arrays.asList(tokens));
    }

    
    /**
     * Join.
     *
     * @param <T> the generic type
     * @param appendable the appendable
     * @param delimiter the delimiter
     * @param firstToken the first token
     * @param otherTokens the other tokens
     * @return the t
     */
    public static <T extends Appendable> T join(T appendable, String delimiter,
                                                @Nullable Object firstToken, Object... otherTokens) {
        checkNotNull(otherTokens);
        return join(appendable, delimiter, Lists.newArrayList(firstToken, otherTokens));
    }

    
    /**
     * Join.
     *
     * @param <T> the generic type
     * @param appendable the appendable
     * @param delimiter the delimiter
     * @param tokens the tokens
     * @return the t
     */
    public static <T extends Appendable> T join(
            T appendable, String delimiter, Iterator<?> tokens) {

        

        checkNotNull(appendable);
        checkNotNull(delimiter);
        if (tokens.hasNext()) {
            try {
                appendOneToken(appendable, tokens.next());
                while (tokens.hasNext()) {
                    appendable.append(delimiter);
                    appendOneToken(appendable, tokens.next());
                }
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }
        return appendable;
    }

    
    /**
     * Join.
     *
     * @param <T> the generic type
     * @param appendable the appendable
     * @param keyValueSeparator the key value separator
     * @param entryDelimiter the entry delimiter
     * @param map the map
     * @return the t
     */
    public static <T extends Appendable> T join(T appendable,
                                                String keyValueSeparator, String entryDelimiter, Map<?, ?> map) {
        checkNotNull(appendable);
        checkNotNull(keyValueSeparator);
        checkNotNull(entryDelimiter);
        Iterator<? extends Map.Entry<?, ?>> entries = map.entrySet().iterator();
        if (entries.hasNext()) {
            try {
                appendOneEntry(appendable, keyValueSeparator, entries.next());
                while (entries.hasNext()) {
                    appendable.append(entryDelimiter);
                    appendOneEntry(appendable, keyValueSeparator, entries.next());
                }
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }
        return appendable;
    }

    
    /**
     * Append one entry.
     *
     * @param appendable the appendable
     * @param keyValueSeparator the key value separator
     * @param entry the entry
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void appendOneEntry(
            Appendable appendable, String keyValueSeparator, Map.Entry<?, ?> entry)
            throws IOException {
        appendOneToken(appendable, entry.getKey());
        appendable.append(keyValueSeparator);
        appendOneToken(appendable, entry.getValue());
    }

    
    /**
     * Append one token.
     *
     * @param appendable the appendable
     * @param token the token
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void appendOneToken(Appendable appendable, Object token)
            throws IOException {
        appendable.append(toCharSequence(token));
    }

    
    /**
     * To char sequence.
     *
     * @param token the token
     * @return the char sequence
     */
    private static CharSequence toCharSequence(Object token) {
        return (token instanceof CharSequence)
                ? (CharSequence) token
                : String.valueOf(token);
    }

    
    /**
     * The Class JoinException.
     *
     * @author l.xue.nong
     */
    public static class JoinException extends RuntimeException {
        
        
        /**
         * Instantiates a new join exception.
         *
         * @param cause the cause
         */
        private JoinException(IOException cause) {
            super(cause);
        }

        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;
    }
}