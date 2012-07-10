/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core CompilerFactory.java 2012-2-8 17:55:41 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers;

import java.io.File;

import cn.com.rebirth.core.compilers.support.JavassistCompiler;
import cn.com.rebirth.core.compilers.support.JdkCompiler;

/**
 * A factory for creating Compiler objects.
 */
public abstract class CompilerFactory {

	/** The is javassist. */
	private static boolean isJavassist = true;

	/** The persistence path. */
	private static String compileDirectory = CompilerFactory.class.getResource("/").getPath();

	/** The jdk version. */
	private static String jdkVersion = System.getProperty("java.version");
	static {
		try {
			Class.forName("javassist.CtClass");
		} catch (ClassNotFoundException e) {
			isJavassist = false;
		}
	}

	/**
	 * Creates a new Compiler object.
	 *
	 * @return the compiler
	 */
	public static Compiler createCompiler() {
		return createCompiler(compileDirectory);
	}

	/**
	 * Creates a new Compiler object.
	 *
	 * @return the compiler
	 */
	public static Compiler createJdkCompiler() {
		return new JdkCompiler(new File(compileDirectory), jdkVersion.substring(0, 3));
	}

	/**
	 * Creates a new Compiler object.
	 * TODO not implements Java5 Annotation
	 * @return the compiler
	 */
	public static Compiler createJavassistCompiler() {
		return new JavassistCompiler(new File(compileDirectory));
	}

	/**
	 * Creates a new Compiler object.
	 *
	 * @param compileDirectory the compile directory
	 * @return the compiler
	 */
	public static Compiler createCompiler(String compileDirectory) {
		if (isJavassist) {
			return new JavassistCompiler(new File(compileDirectory));
		}
		return new JdkCompiler(new File(compileDirectory), jdkVersion.substring(0, 3));
	}
}
