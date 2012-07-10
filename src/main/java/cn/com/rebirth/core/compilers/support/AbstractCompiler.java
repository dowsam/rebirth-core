/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AbstractCompiler.java 2012-2-8 17:04:37 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * The Class AbstractCompiler.
 *
 * @author l.xue.nong
 */
public abstract class AbstractCompiler implements cn.com.rebirth.core.compilers.Compiler {

	/** The Constant PACKAGE_PATTERN. */
	private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([_a-zA-Z][_a-zA-Z0-9\\.]*);");

	/** The Constant CLASS_PATTERN. */
	private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([_a-zA-Z][_a-zA-Z0-9]*)\\s+");

	/** The Constant CLASS_EXTENSION. */
	public static final String CLASS_EXTENSION = ".class";

	/** The Constant JAVA_EXTENSION. */
	public static final String JAVA_EXTENSION = ".java";

	/** The compile directory. */
	private final File compileDirectory;

	/**
	 * Instantiates a new abstract compiler.
	 *
	 * @param compileDirectory the compile directory
	 */
	public AbstractCompiler(File compileDirectory) {
		super();
		this.compileDirectory = compileDirectory;
	}

	/**
	 * Save bytecode.
	 *
	 * @param name the name
	 * @param bytecode the bytecode
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void saveBytecode(String name, byte[] bytecode) throws IOException {
		// ClassUtils.checkBytecode(name, bytecode);
		if (this.compileDirectory != null) {
			FileUtils.forceMkdir(compileDirectory);
		}
		if (compileDirectory != null && compileDirectory.exists()) {
			File file = new File(compileDirectory, name.replace('.', '/') + ".class");
			File dir = file.getParentFile();
			if (!dir.exists()) {
				boolean ok = dir.mkdirs();
				if (!ok) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			FileOutputStream out = new FileOutputStream(file);
			try {
				out.write(bytecode);
				out.flush();
			} finally {
				out.close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.compilers.Compiler#compile(java.lang.String)
	 */
	public Class<?> compile(String code) throws ParseException {
		code = code.trim();
		Matcher matcher = PACKAGE_PATTERN.matcher(code);
		String pkg;
		if (matcher.find()) {
			pkg = matcher.group(1);
		} else {
			pkg = "";
		}
		matcher = CLASS_PATTERN.matcher(code);
		String cls;
		if (matcher.find()) {
			cls = matcher.group(1);
		} else {
			throw new IllegalArgumentException("No such class name in " + code);
		}
		String className = pkg != null && pkg.length() > 0 ? pkg + "." + cls : cls;
		try {
			return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			if (!code.endsWith("}")) {
				throw new ParseException("The java code not endsWith \"}\", code: \n" + code + "\n", code.length() - 1);
			}
			try {
				return doCompile(className, code);
			} catch (ParseException t) {
				throw t;
			} catch (Throwable t) {
				throw new ParseException("Failed to compile class, cause: " + t.getMessage() + ", class: " + className
						+ ", code: \n" + code + "\n, stack: " + toString(t), 0);
			}
		}
	}

	/**
	 * Do compile.
	 *
	 * @param name the name
	 * @param source the source
	 * @return the class
	 * @throws Throwable the throwable
	 */
	protected abstract Class<?> doCompile(String name, String source) throws Throwable;

	/**
	 * To string.
	 *
	 * @param e the e
	 * @return the string
	 */
	public static String toString(Throwable e) {
		StringWriter w = new StringWriter();
		PrintWriter p = new PrintWriter(w);
		p.print(e.getClass().getName() + ": ");
		if (e.getMessage() != null) {
			p.print(e.getMessage() + "\n");
		}
		p.println();
		try {
			e.printStackTrace(p);
			return w.toString();
		} finally {
			p.close();
		}
	}

	/**
	 * To uri.
	 *
	 * @param name the name
	 * @return the uRI
	 */
	public static URI toURI(String name) {
		try {
			return new URI(name);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * For name.
	 *
	 * @param packages the packages
	 * @param className the class name
	 * @return the class
	 */
	public static Class<?> forName(String[] packages, String className) {
		try {
			return _forName(className);
		} catch (ClassNotFoundException e) {
			if (packages != null && packages.length > 0) {
				for (String pkg : packages) {
					try {
						return _forName(pkg + "." + className);
					} catch (ClassNotFoundException e2) {
					}
				}
			}
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * For name.
	 *
	 * @param className the class name
	 * @return the class
	 */
	public static Class<?> forName(String className) {
		try {
			return _forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * _for name.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Class<?> _forName(String className) throws ClassNotFoundException {
		if ("boolean".equals(className))
			return boolean.class;
		if ("byte".equals(className))
			return byte.class;
		if ("char".equals(className))
			return char.class;
		if ("short".equals(className))
			return short.class;
		if ("int".equals(className))
			return int.class;
		if ("long".equals(className))
			return long.class;
		if ("float".equals(className))
			return float.class;
		if ("double".equals(className))
			return double.class;
		if ("boolean[]".equals(className))
			return boolean[].class;
		if ("byte[]".equals(className))
			return byte[].class;
		if ("char[]".equals(className))
			return char[].class;
		if ("short[]".equals(className))
			return short[].class;
		if ("int[]".equals(className))
			return int[].class;
		if ("long[]".equals(className))
			return long[].class;
		if ("float[]".equals(className))
			return float[].class;
		if ("double[]".equals(className))
			return double[].class;
		try {
			return arrayForName(className);
		} catch (ClassNotFoundException e) {
			if (className.indexOf('.') == -1) { // 尝试java.lang包
				try {
					return arrayForName("java.lang." + className);
				} catch (ClassNotFoundException e2) {
					// 忽略尝试异常, 抛出原始异常
				}
			}
			throw e;
		}
	}

	/**
	 * Array for name.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	private static Class<?> arrayForName(String className) throws ClassNotFoundException {
		return Class.forName(className.endsWith("[]") ? "[L" + className.substring(0, className.length() - 2) + ";"
				: className, true, Thread.currentThread().getContextClassLoader());
	}
}
