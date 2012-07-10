/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AdaptiveCompiler.java 2012-2-8 17:20:21 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers.support;

import java.text.ParseException;

import cn.com.rebirth.core.compilers.Compiler;

/**
 * The Class AdaptiveCompiler.
 *
 * @author l.xue.nong
 */
public class AdaptiveCompiler implements Compiler {

	/** The compiler. */
	private final Compiler compiler;

	/**
	 * Instantiates a new adaptive compiler.
	 *
	 * @param compiler the compiler
	 */
	public AdaptiveCompiler(Compiler compiler) {
		super();
		this.compiler = compiler;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.compilers.Compiler#compile(java.lang.String)
	 */
	@Override
	public Class<?> compile(String code) throws ParseException {
		return compiler.compile(code);
	}

}
