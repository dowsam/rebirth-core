/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons StackTraceElements.java 2012-7-6 10:23:42 l.xue.nong$$
 */


package cn.com.rebirth.core.inject.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;


/**
 * The Class StackTraceElements.
 *
 * @author l.xue.nong
 */
public class StackTraceElements {

    
    /**
     * For member.
     *
     * @param member the member
     * @return the object
     */
    public static Object forMember(Member member) {
        if (member == null) {
            return SourceProvider.UNKNOWN_SOURCE;
        }

        Class<?> declaringClass = member.getDeclaringClass();

        String fileName = null;
        int lineNumber = -1;

        Class<? extends Member> memberType = MoreTypes.memberType(member);
        String memberName = memberType == Constructor.class ? "<init>" : member.getName();
        return new StackTraceElement(declaringClass.getName(), memberName, fileName, lineNumber);
    }

    
    /**
     * For type.
     *
     * @param implementation the implementation
     * @return the object
     */
    public static Object forType(Class<?> implementation) {
        String fileName = null;
        int lineNumber = -1;

        return new StackTraceElement(implementation.getName(), "class", fileName, lineNumber);
    }
}
