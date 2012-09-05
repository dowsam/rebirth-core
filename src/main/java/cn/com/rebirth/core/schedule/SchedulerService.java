/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core SchedulerService.java 2012-8-1 9:09:52 l.xue.nong$$
 */
package cn.com.rebirth.core.schedule;

/**
 * The Interface SchedulerService.
 *
 * @author l.xue.nong
 */
public interface SchedulerService {

	/**
	 * Pause task.
	 *
	 * @param taskName the task name
	 */
	public abstract void pauseTask(String taskName);

	/**
	 * Delete task.
	 *
	 * @param taskName the task name
	 */
	public abstract void deleteTask(String taskName);

	/**
	 * Resume task.
	 *
	 * @param taskName the task name
	 */
	public abstract void resumeTask(String taskName);

	/**
	 * Schedule.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param timeExpression the time expression
	 */
	public abstract <T extends AbstractAppTask> void schedule(Class<T> appTaskClass, String taskName,
			String description, String timeExpression);

	/**
	 * Schedule dialy.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param hour the hour
	 * @param minute the minute
	 */
	public abstract <T extends AbstractAppTask> void scheduleDialy(Class<T> appTaskClass, String taskName,
			String description, int hour, int minute);

	/**
	 * Schedule weekly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param dayOfWeek the day of week
	 * @param hour the hour
	 * @param minute the minute
	 */
	public abstract <T extends AbstractAppTask> void scheduleWeekly(Class<T> appTaskClass, String taskName,
			String description, int dayOfWeek, int hour, int minute);

	/**
	 * Schedule monthly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param dayOfMonth the day of month
	 * @param hour the hour
	 * @param minute the minute
	 */
	public abstract <T extends AbstractAppTask> void scheduleMonthly(Class<T> appTaskClass, String taskName,
			String description, int dayOfMonth, int hour, int minute);

	/**
	 * Schedule immediate.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param repeatCount the repeat count
	 * @param repeatInterval the repeat interval
	 */
	public abstract <T extends AbstractAppTask> void scheduleImmediate(Class<T> appTaskClass, String taskName,
			String description, int repeatCount, long repeatInterval);

	/**
	 * Schedule hourly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInHours the interval in hours
	 */
	public abstract <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName,
			String description, int intervalInHours);

	/**
	 * Schedule hourly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInHours the interval in hours
	 * @param repeatCount the repeat count
	 */
	public abstract <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName,
			String description, int intervalInHours, int repeatCount);

	/**
	 * Schedule minutely.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInMinutes the interval in minutes
	 * @param repeatCount the repeat count
	 */
	public abstract <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes, int repeatCount);

	/**
	 * Schedule minutely.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInMinutes the interval in minutes
	 */
	public abstract <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes);

	/**
	 * Schedule secondly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInSeconds the interval in seconds
	 * @param repeatCount the repeat count
	 */
	public abstract <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds, int repeatCount);

	/**
	 * Schedule secondly.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass the app task class
	 * @param taskName the task name
	 * @param description the description
	 * @param intervalInSeconds the interval in seconds
	 */
	public abstract <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds);

	/**
	 * Start.
	 */
	public abstract void start();

	/**
	 * Pause all.
	 */
	public abstract void pauseAll();

	/**
	 * Resume all.
	 */
	public abstract void resumeAll();

	/**
	 * Shutdown.
	 *
	 * @param waitForJobsToComplete the wait for jobs to complete
	 */
	public abstract void shutdown(boolean waitForJobsToComplete);

	/**
	 * Checks if is shutdown.
	 *
	 * @return true, if is shutdown
	 */
	public abstract boolean isShutdown();

	/**
	 * Shutdown.
	 */
	public abstract void shutdown();

}