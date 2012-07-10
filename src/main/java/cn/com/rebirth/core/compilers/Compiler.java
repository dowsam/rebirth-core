/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core Compiler.java 2012-2-8 16:59:07 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers;

import java.text.ParseException;

/**
 * The Interface Compiler.
 *
 * @author l.xue.nong
 */
public interface Compiler {

	/**
	 * Compile.
	 *
	 * @param code the code
	 * @return the class
	 * @throws ParseException the parse exception
	 */
	Class<?> compile(String code) throws ParseException;
}
