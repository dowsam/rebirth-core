/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core JavassistCompiler.java 2012-2-8 17:13:56 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers.support;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

/**
 * The Class JavassistCompiler.
 *
 * @author l.xue.nong
 */
public class JavassistCompiler extends AbstractCompiler {

	/** The Constant IMPORT_PATTERN. */
	private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w\\.\\*]+);\r\n");

	/** The Constant EXTENDS_PATTERN. */
	private static final Pattern EXTENDS_PATTERN = Pattern.compile("\\s+extends\\s+([\\w\\.]+)[^\\{]*\\{\r\n");

	/** The Constant IMPLEMENTS_PATTERN. */
	private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile("\\s+implements\\s+([\\w\\.]+)\\s*\\{\r\n");

	/** The Constant METHODS_PATTERN. */
	private static final Pattern METHODS_PATTERN = Pattern.compile("(private|public|protected)\\s+");

	/** The Constant FIELD_PATTERN. */
	private static final Pattern FIELD_PATTERN = Pattern.compile("[^\n]+=[^\n]+;");

	/**
	 * Instantiates a new javassist compiler.
	 *
	 * @param compileDirectory the compile directory
	 */
	public JavassistCompiler(File compileDirectory) {
		super(compileDirectory);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.compilers.support.AbstractCompiler#doCompile(java.lang.String, java.lang.String)
	 */
	@Override
	public Class<?> doCompile(String name, String source) throws Throwable {
		int i = name.lastIndexOf('.');
		String className = i < 0 ? name : name.substring(i + 1);
		ClassPool pool = new ClassPool(true);
		pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
		Matcher matcher = IMPORT_PATTERN.matcher(source);
		List<String> importPackages = new ArrayList<String>();
		Map<String, String> fullNames = new HashMap<String, String>();
		while (matcher.find()) {
			String pkg = matcher.group(1);
			if (pkg.endsWith(".*")) {
				String pkgName = pkg.substring(0, pkg.length() - 2);
				pool.importPackage(pkgName);
				importPackages.add(pkgName);
			} else {
				pool.importPackage(pkg);
				int pi = pkg.lastIndexOf('.');
				fullNames.put(pi < 0 ? pkg : pkg.substring(pi + 1), pkg);
			}
		}
		String[] packages = importPackages.toArray(new String[0]);
		matcher = EXTENDS_PATTERN.matcher(source);
		CtClass cls;
		if (matcher.find()) {
			String extend = matcher.group(1).trim();
			String extendClass;
			if (extend.contains(".")) {
				extendClass = extend;
			} else if (fullNames.containsKey(extend)) {
				extendClass = fullNames.get(extend);
			} else {
				extendClass = forName(packages, extend).getName();
			}
			cls = pool.makeClass(name, pool.get(extendClass));
		} else {
			cls = pool.makeClass(name);
		}
		matcher = IMPLEMENTS_PATTERN.matcher(source);
		if (matcher.find()) {
			String[] ifaces = matcher.group(1).trim().split("\\,");
			for (String iface : ifaces) {
				iface = iface.trim();
				String ifaceClass;
				if (iface.contains(".")) {
					ifaceClass = iface;
				} else if (fullNames.containsKey(iface)) {
					ifaceClass = fullNames.get(iface);
				} else {
					ifaceClass = forName(packages, iface).getName();
				}
				cls.addInterface(pool.get(ifaceClass));
			}
		}
		String body = source.substring(source.indexOf("{") + 1, source.length() - 1);
		String[] methods = METHODS_PATTERN.split(body);
		for (String method : methods) {
			method = method.trim();
			if (method.length() > 0) {
				if (method.startsWith(className)) {
					cls.addConstructor(CtNewConstructor.make("public " + method, cls));
				} else if (FIELD_PATTERN.matcher(method).matches()) {
					cls.addField(CtField.make("private " + method, cls));
				} else {
					cls.addMethod(CtNewMethod.make("public " + method, cls));
				}
			}
		}
		saveBytecode(name, cls.toBytecode());
		return cls.toClass();
	}

}
