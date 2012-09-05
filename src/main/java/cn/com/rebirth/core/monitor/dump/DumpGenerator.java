/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core DumpGenerator.java 2012-7-6 14:29:06 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.dump;

import java.io.File;
import java.util.Map;

import cn.com.rebirth.commons.Nullable;

/**
 * The Interface DumpGenerator.
 *
 * @author l.xue.nong
 */
public interface DumpGenerator {

	/**
	 * Generate dump.
	 *
	 * @param cause the cause
	 * @param context the context
	 * @return the result
	 * @throws DumpGenerationFailedException the dump generation failed exception
	 */
	Result generateDump(String cause, @Nullable Map<String, Object> context) throws DumpGenerationFailedException;

	/**
	 * Generate dump.
	 *
	 * @param cause the cause
	 * @param context the context
	 * @param contributors the contributors
	 * @return the result
	 * @throws DumpGenerationFailedException the dump generation failed exception
	 */
	Result generateDump(String cause, @Nullable Map<String, Object> context, String... contributors)
			throws DumpGenerationFailedException;

	/**
	 * The Class Result.
	 *
	 * @author l.xue.nong
	 */
	static class Result {

		/** The location. */
		private final File location;

		/** The failed contributors. */
		private Iterable<DumpContributionFailedException> failedContributors;

		/**
		 * Instantiates a new result.
		 *
		 * @param location the location
		 * @param failedContributors the failed contributors
		 */
		public Result(File location, Iterable<DumpContributionFailedException> failedContributors) {
			this.location = location;
			this.failedContributors = failedContributors;
		}

		/**
		 * Location.
		 *
		 * @return the string
		 */
		public String location() {
			return location.toString();
		}

		/**
		 * Failed contributors.
		 *
		 * @return the iterable
		 */
		public Iterable<DumpContributionFailedException> failedContributors() {
			return failedContributors;
		}
	}
}
