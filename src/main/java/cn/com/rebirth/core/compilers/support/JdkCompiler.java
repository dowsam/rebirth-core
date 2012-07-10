/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core JdkCompiler.java 2012-2-8 17:09:39 l.xue.nong$$
 */
package cn.com.rebirth.core.compilers.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * JdkCompiler. (SPI, Singleton, ThreadSafe)
 * 
 * @see com.TemplateEngine.httl.Engine#setCompiler(Compiler)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class JdkCompiler extends AbstractCompiler {

	/** The compiler. */
	private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	/** The diagnostic collector. */
	private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

	/** The class loader. */
	private final ClassLoaderImpl classLoader;

	/** The java file manager. */
	private final JavaFileManagerImpl javaFileManager;

	/** The options. */
	private volatile List<String> options;

	/**
	 * Instantiates a new jdk compiler.
	 *
	 * @param compileDirectory the compile directory
	 * @param jdkVersion the jdk version
	 */
	public JdkCompiler(File compileDirectory, String jdkVersion) {
		super(compileDirectory);
		options = new ArrayList<String>();
		if (jdkVersion != null && jdkVersion.trim().length() > 0) {
			options.add("-target");
			options.add(jdkVersion.trim());
		} else {
			options.add("-target");
			options.add("1.6");
		}
		StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, null, null);
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader instanceof URLClassLoader
				&& (!loader.getClass().getName().equals("sun.misc.Launcher$AppClassLoader"))) {
			try {
				URLClassLoader urlClassLoader = (URLClassLoader) loader;
				List<File> files = new ArrayList<File>();
				for (URL url : urlClassLoader.getURLs()) {
					files.add(new File(url.getFile()));
				}
				manager.setLocation(StandardLocation.CLASS_PATH, files);
			} catch (IOException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
		classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoaderImpl>() {
			public ClassLoaderImpl run() {
				return new ClassLoaderImpl(loader);
			}
		});
		javaFileManager = new JavaFileManagerImpl(manager, classLoader);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.compilers.support.AbstractCompiler#doCompile(java.lang.String, java.lang.String)
	 */
	@Override
	public Class<?> doCompile(String name, String sourceCode) throws Throwable {
		int i = name.lastIndexOf('.');
		String packageName = i < 0 ? "" : name.substring(0, i);
		String className = i < 0 ? name : name.substring(i + 1);
		JavaFileObjectImpl javaFileObject = new JavaFileObjectImpl(className, sourceCode);
		javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION,
				javaFileObject);
		Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, options, null,
				Arrays.asList(new JavaFileObject[] { javaFileObject })).call();
		if (result == null || !result.booleanValue()) {
			throw new IllegalStateException("Compilation failed. class: " + name + ", diagnostics: "
					+ diagnosticCollector);
		}
		return classLoader.loadClass(name);
	}

	/**
	 * The Class ClassLoaderImpl.
	 *
	 * @author l.xue.nong
	 */
	private final class ClassLoaderImpl extends ClassLoader {

		/** The classes. */
		private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

		/**
		 * Instantiates a new class loader impl.
		 *
		 * @param parentClassLoader the parent class loader
		 */
		ClassLoaderImpl(final ClassLoader parentClassLoader) {
			super(parentClassLoader);
		}

		/**
		 * Files.
		 *
		 * @return the collection
		 */
		Collection<JavaFileObject> files() {
			return Collections.unmodifiableCollection(classes.values());
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#findClass(java.lang.String)
		 */
		@Override
		protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
			JavaFileObject file = classes.get(qualifiedClassName);
			if (file != null) {
				byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
				try {
					saveBytecode(qualifiedClassName, bytes);
				} catch (IOException e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
				return defineClass(qualifiedClassName, bytes, 0, bytes.length);
			}
			try {
				return Class.forName(qualifiedClassName);
			} catch (ClassNotFoundException nf) {
				return super.findClass(qualifiedClassName);
			}
		}

		/**
		 * Adds the.
		 *
		 * @param qualifiedClassName the qualified class name
		 * @param javaFile the java file
		 */
		void add(final String qualifiedClassName, final JavaFileObject javaFile) {
			classes.put(qualifiedClassName, javaFile);
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
		 */
		@Override
		protected synchronized Class<?> loadClass(final String name, final boolean resolve)
				throws ClassNotFoundException {
			return super.loadClass(name, resolve);
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
		 */
		@Override
		public InputStream getResourceAsStream(final String name) {
			if (name.endsWith(CLASS_EXTENSION)) {
				String qualifiedClassName = name.substring(0, name.length() - CLASS_EXTENSION.length()).replace('/',
						'.');
				JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
				if (file != null) {
					return new ByteArrayInputStream(file.getByteCode());
				}
			}
			return super.getResourceAsStream(name);
		}
	}

	/**
	 * The Class JavaFileObjectImpl.
	 *
	 * @author l.xue.nong
	 */
	private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

		/** The bytecode. */
		private ByteArrayOutputStream bytecode;

		/** The source. */
		private final CharSequence source;

		/**
		 * Instantiates a new java file object impl.
		 *
		 * @param baseName the base name
		 * @param source the source
		 */
		public JavaFileObjectImpl(final String baseName, final CharSequence source) {
			super(toURI(baseName + JAVA_EXTENSION), Kind.SOURCE);
			this.source = source;
		}

		/**
		 * Instantiates a new java file object impl.
		 *
		 * @param name the name
		 * @param kind the kind
		 */
		JavaFileObjectImpl(final String name, final Kind kind) {
			super(toURI(name), kind);
			source = null;
		}

		/**
		 * Instantiates a new java file object impl.
		 *
		 * @param uri the uri
		 * @param kind the kind
		 */
		public JavaFileObjectImpl(URI uri, Kind kind) {
			super(uri, kind);
			source = null;
		}

		/* (non-Javadoc)
		 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
		 */
		@Override
		public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
			if (source == null) {
				throw new UnsupportedOperationException("source == null");
			}
			return source;
		}

		/* (non-Javadoc)
		 * @see javax.tools.SimpleJavaFileObject#openInputStream()
		 */
		@Override
		public InputStream openInputStream() {
			return new ByteArrayInputStream(getByteCode());
		}

		/* (non-Javadoc)
		 * @see javax.tools.SimpleJavaFileObject#openOutputStream()
		 */
		@Override
		public OutputStream openOutputStream() {
			return bytecode = new ByteArrayOutputStream();
		}

		/**
		 * Gets the byte code.
		 *
		 * @return the byte code
		 */
		public byte[] getByteCode() {
			return bytecode.toByteArray();
		}
	}

	/**
	 * The Class JavaFileManagerImpl.
	 *
	 * @author l.xue.nong
	 */
	private static final class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

		/** The class loader. */
		private final ClassLoaderImpl classLoader;

		/** The file objects. */
		private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

		/**
		 * Instantiates a new java file manager impl.
		 *
		 * @param fileManager the file manager
		 * @param classLoader the class loader
		 */
		public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
			super(fileManager);
			this.classLoader = classLoader;
		}

		/**
		 * Gets the file for input.
		 *
		 * @param location the location
		 * @param packageName the package name
		 * @param relativeName the relative name
		 * @return the file for input
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public FileObject getFileForInput(Location location, String packageName, String relativeName)
				throws IOException {
			FileObject o = fileObjects.get(uri(location, packageName, relativeName));
			if (o != null)
				return o;
			return super.getFileForInput(location, packageName, relativeName);
		}

		/**
		 * Put file for input.
		 *
		 * @param location the location
		 * @param packageName the package name
		 * @param relativeName the relative name
		 * @param file the file
		 */
		public void putFileForInput(StandardLocation location, String packageName, String relativeName,
				JavaFileObject file) {
			fileObjects.put(uri(location, packageName, relativeName), file);
		}

		/**
		 * Uri.
		 *
		 * @param location the location
		 * @param packageName the package name
		 * @param relativeName the relative name
		 * @return the uRI
		 */
		private URI uri(Location location, String packageName, String relativeName) {
			return toURI(location.getName() + '/' + packageName + '/' + relativeName);
		}

		/**
		 * Gets the java file for output.
		 *
		 * @param location the location
		 * @param qualifiedName the qualified name
		 * @param kind the kind
		 * @param outputFile the output file
		 * @return the java file for output
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind,
				FileObject outputFile) throws IOException {
			JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
			classLoader.add(qualifiedName, file);
			return file;
		}

		/**
		 * Gets the class loader.
		 *
		 * @param location the location
		 * @return the class loader
		 */
		@Override
		public ClassLoader getClassLoader(JavaFileManager.Location location) {
			return classLoader;
		}

		/**
		 * Infer binary name.
		 *
		 * @param loc the loc
		 * @param file the file
		 * @return the string
		 */
		@Override
		public String inferBinaryName(Location loc, JavaFileObject file) {
			if (file instanceof JavaFileObjectImpl)
				return file.getName();
			return super.inferBinaryName(loc, file);
		}

		/**
		 * List.
		 *
		 * @param location the location
		 * @param packageName the package name
		 * @param kinds the kinds
		 * @param recurse the recurse
		 * @return the iterable
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
				throws IOException {
			Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);

			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			List<URL> urlList = new ArrayList<URL>();
			Enumeration<URL> e = contextClassLoader.getResources("com");
			while (e.hasMoreElements()) {
				urlList.add(e.nextElement());
			}

			ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();

			if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
				for (JavaFileObject file : fileObjects.values()) {
					if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName)) {
						files.add(file);
					}
				}

				files.addAll(classLoader.files());
			} else if (location == StandardLocation.SOURCE_PATH && kinds.contains(JavaFileObject.Kind.SOURCE)) {
				for (JavaFileObject file : fileObjects.values()) {
					if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName)) {
						files.add(file);
					}
				}
			}

			for (JavaFileObject file : result) {
				files.add(file);
			}

			return files;
		}
	}

}
