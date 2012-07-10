/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core TaskSchedulerService.java 2012-2-3 12:31:23 l.xue.nong$$
 */
package cn.com.rebirth.core.schedule;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import cn.com.rebirth.commons.utils.ExceptionUtils;
import cn.com.rebirth.commons.utils.IdentitiesUtils;

/**
 * 定时任务调度处理类 根据Spring 注入SchedulerFactory.
 *
 * @author l.xue.nong
 */
public final class TaskSchedulerService {

	/** The scheduler. */
	private Scheduler theScheduler;

	/**
	 * Sets the the scheduler.
	 *
	 * @param theScheduler the new the scheduler
	 */
	public void setTheScheduler(Scheduler theScheduler) {
		this.theScheduler = theScheduler;
	}

	/**
	 * Gets the the scheduler.
	 *
	 * @return the the scheduler
	 */
	public Scheduler getTheScheduler() {
		return theScheduler;
	}

	/**
	 * 暂停某个计划任务.
	 *
	 * @param taskName 任务名称
	 */
	public void pauseTask(String taskName) {
		try {
			this.theScheduler.pauseJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 删除一个计划任务.
	 *
	 * @param taskName 任务名称
	 */
	public void deleteTask(String taskName) {
		try {
			this.theScheduler.deleteJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 恢复某个计划任务.
	 *
	 * @param taskName 任务名称
	 */
	public void resumeTask(String taskName) {
		try {
			this.theScheduler.resumeJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 计划一个任务.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass AppTask类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * 
	 * <pre>
	 * A "Time-Expression" is a string comprised of 6 or 7 fields separated by white space. The 6 mandatory and 1 optional fields are as follows:
	 * Field Name   Allowed Values   Allowed Special Characters
	 * Seconds    0-59    , - * /
	 * Minutes    0-59    , - * /
	 * Hours    0-23    , - * /
	 * Day-of-month    1-31    , - * ? / L W C
	 * Month    1-12 or JAN-DEC    , - * /
	 * Day-of-Week    1-7 or SUN-SAT    , - * ? / L C #
	 * Year (Optional)    empty, 1970-2099    , - * /
	 * The '*' character is used to specify all values. For example, "*" in the minute field means "every minute".
	 * The '?' character is allowed for the day-of-month and day-of-week fields. It is used to specify 'no specific value'. This is useful when you need to specify something in one of the two fileds, but not the other. See the examples below for clarification.
	 * The '-' character is used to specify ranges For example "10-12" in the hour field means "the hours 10, 11 and 12".
	 * The ',' character is used to specify additional values. For example "MON,WED,FRI" in the day-of-week field means "the days Monday, Wednesday, and Friday".
	 * The '/' character is used to specify increments. For example "0/15" in the seconds field means "the seconds 0, 15, 30, and 45". And "5/15" in the seconds field means "the seconds 5, 20, 35, and 50". Specifying '*' before the '/' is equivalent to specifying 0 is the value to start with. Essentially, for each field in the expression, there is a set of numbers that can be turned on or off. For seconds and minutes, the numbers range from 0 to 59. For hours 0 to 23, for days of the month 0 to 31, and for months 1 to 12. The "/" character simply helps you turn on every "nth" value in the given set. Thus "7/6" in the month field only turns on month "7", it does NOT mean every 6th month, please note that subtlety.
	 * The 'L' character is allowed for the day-of-month and day-of-week fields. This character is short-hand for "last", but it has different meaning in each of the two fields. For example, the value "L" in the day-of-month field means "the last day of the month" - day 31 for January, day 28 for February on non-leap years. If used in the day-of-week field by itself, it simply means "7" or "SAT". But if used in the day-of-week field after another value, it means "the last xxx day of the month" - for example "6L" means "the last friday of the month". When using the 'L' option, it is important not to specify lists, or ranges of values, as you'll get confusing results.
	 * The 'W' character is allowed for the day-of-month field. This character is used to specify the weekday (Monday-Friday) nearest the given day. As an example, if you were to specify "15W" as the value for the day-of-month field, the meaning is: "the nearest weekday to the 15th of the month". So if the 15th is a Saturday, the trigger will fire on Friday the 14th. If the 15th is a Sunday, the trigger will fire on Monday the 16th. If the 15th is a Tuesday, then it will fire on Tuesday the 15th. However if you specify "1W" as the value for day-of-month, and the 1st is a Saturday, the trigger will fire on Monday the 3rd, as it will not 'jump' over the boundary of a month's days. The 'W' character can only be specified when the day-of-month is a single day, not a range or list of days.
	 * The 'L' and 'W' characters can also be combined for the day-of-month expression to yield 'LW', which translates to "last weekday of the month".
	 * The '#' character is allowed for the day-of-week field. This character is used to specify "the nth" XXX day of the month. For example, the value of "6#3" in the day-of-week field means the third Friday of the month (day 6 = Friday and "#3" = the 3rd one in the month). Other examples: "2#1" = the first Monday of the month and "4#5" = the fifth Wednesday of the month. Note that if you specify "#5" and there is not 5 of the given day-of-week in the month, then no firing will occur that month.
	 * The 'C' character is allowed for the day-of-month and day-of-week fields. This character is short-hand for "calendar". This means values are calculated against the associated calendar, if any. If no calendar is associated, then it is equivalent to having an all-inclusive calendar. A value of "5C" in the day-of-month field means "the first day included by the calendar on or after the 5th". A value of "1C" in the day-of-week field means "the first day included by the calendar on or after sunday".
	 * The legal characters and the names of months and days of the week are not case sensitive.
	 * Here are some full examples:
	 * Expression   Meaning
	 * "0 0 12 * * ?"    Fire at 12pm (noon) every day
	 * "0 15 10 ? * *"    Fire at 10:15am every day
	 * "0 15 10 * * ?"    Fire at 10:15am every day
	 * "0 15 10 * * ? *"    Fire at 10:15am every day
	 * "0 15 10 * * ? 2005"    Fire at 10:15am every day during the year 2005
	 * "0 * 14 * * ?"    Fire every minute starting at 2pm and ending at 2:59pm, every day
	 * "0 0/5 14 * * ?"    Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day
	 * "0 0/5 14,18 * * ?"    Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day
	 * "0 0-5 14 * * ?"    Fire every minute starting at 2pm and ending at 2:05pm, every day
	 * "0 10,44 14 ? 3 WED"    Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.
	 * "0 15 10 ? * MON-FRI"    Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday
	 * "0 15 10 15 * ?"    Fire at 10:15am on the 15th day of every month
	 * "0 15 10 L * ?"    Fire at 10:15am on the last day of every month
	 * "0 15 10 ? * 6L"    Fire at 10:15am on the last Friday of every month
	 * "0 15 10 ? * 6L"    Fire at 10:15am on the last Friday of every month
	 * "0 15 10 ? * 6L 2002-2005"    Fire at 10:15am on every last friday of every month during the years 2002, 2003, 2004 and 2005
	 * "0 15 10 ? * 6#3"    Fire at 10:15am on the third Friday of every month
	 * Pay attention to the effects of '?' and '*' in the day-of-week and day-of-month fields!
	 * </pre>
	 * @param timeExpression 计划时间表达式
	 */
	public <T extends AbstractAppTask> void schedule(Class<T> appTaskClass, String taskName, String description,
			String timeExpression) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		CronTrigger ct = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2()).withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
				.build();
		try {
			this.theScheduler.scheduleJob(jd, ct);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 每日定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass AppTask任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param hour （0－23）小时
	 * @param minute （0－59）分钟
	 */
	public <T extends AbstractAppTask> void scheduleDialy(Class<T> appTaskClass, String taskName, String description,
			int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.repeatSecondlyForever().withIntervalInHours(hour)
								.withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 每周定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称 description 任务描述
	 * @param description the description
	 * @param dayOfWeek （1－7）星期
	 * @param hour （0－23）小时
	 * @param minute （0－59）分钟
	 */
	public <T extends AbstractAppTask> void scheduleWeekly(Class<T> appTaskClass, String taskName, String description,
			int dayOfWeek, int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().onDaysOfTheWeek(dayOfWeek)
								.withIntervalInHours(hour).withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 每月定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param dayOfMonth (1-31, or -1)月内某日
	 * @param hour （0－23）小时
	 * @param minute （0－59）分钟
	 */
	public <T extends AbstractAppTask> void scheduleMonthly(Class<T> appTaskClass, String taskName, String description,
			int dayOfMonth, int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(dayOfMonth)
								.withIntervalInHours(hour).withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 立即执行，并可以按重复次数和间隔执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名
	 * @param description 任务描述
	 * @param repeatCount 重复次数
	 * @param repeatInterval 执行间隔 ms毫秒
	 */
	public <T extends AbstractAppTask> void scheduleImmediate(Class<T> appTaskClass, String taskName,
			String description, int repeatCount, long repeatInterval) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInSeconds((int) repeatInterval)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以小时为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInHours 时间间隔，单位小时，整数
	 */
	public <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName, String description,
			int intervalInHours) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(intervalInHours)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以小时为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInHours 时间间隔，单位小时，整数
	 * @param repeatCount 重复次数
	 */
	public <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName, String description,
			int intervalInHours, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInHours(intervalInHours)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以分钟为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInMinutes the interval in minutes
	 * @param repeatCount 重复次数
	 */
	public <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInMinutes(intervalInMinutes)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以分钟为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInMinutes the interval in minutes
	 */
	public <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(intervalInMinutes)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以秒为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInSeconds the interval in seconds
	 * @param repeatCount 重复次数
	 */
	public <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInMilliseconds(intervalInSeconds * 1000l)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 以秒为单位定时执行.
	 *
	 * @param <T> the generic type
	 * @param appTaskClass 任务类
	 * @param taskName 任务名称
	 * @param description 任务描述
	 * @param intervalInSeconds the interval in seconds
	 */
	public <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.uuid2())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(-1)
								.withIntervalInMilliseconds(intervalInSeconds * 1000l)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * Start.
	 */
	public void start() {
		try {
			this.theScheduler.start();
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/**
	 * 暂停所以的计划执行的任务.
	 */
	public void pauseAll() {
		try {
			this.theScheduler.pauseAll();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 恢复所有被暂停的计划任务.
	 */
	public void resumeAll() {
		try {
			this.theScheduler.resumeAll();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Shutdown.
	 *
	 * @param waitForJobsToComplete the wait for jobs to complete
	 */
	public void shutdown(boolean waitForJobsToComplete) {
		try {
			this.theScheduler.shutdown(waitForJobsToComplete);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Checks if is shutdown.
	 *
	 * @return true, if is shutdown
	 */
	public boolean isShutdown() {
		try {
			return this.theScheduler.isShutdown();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		try {
			this.theScheduler.shutdown();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}
}
