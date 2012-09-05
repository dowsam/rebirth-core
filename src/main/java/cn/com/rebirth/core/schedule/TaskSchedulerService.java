/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-core TaskSchedulerService.java 2012-8-1 10:07:05 l.xue.nong$$
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
 * The Class TaskSchedulerService.
 *
 * @author l.xue.nong
 */
public final class TaskSchedulerService implements SchedulerService {

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

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#pauseTask(java.lang.String)
	 */
	@Override
	public void pauseTask(String taskName) {
		try {
			this.theScheduler.pauseJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#deleteTask(java.lang.String)
	 */
	@Override
	public void deleteTask(String taskName) {
		try {
			this.theScheduler.deleteJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#resumeTask(java.lang.String)
	 */
	@Override
	public void resumeTask(String taskName) {
		try {
			this.theScheduler.resumeJob(new JobKey(taskName, Scheduler.DEFAULT_GROUP));
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#schedule(java.lang.Class, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public <T extends AbstractAppTask> void schedule(Class<T> appTaskClass, String taskName, String description,
			String timeExpression) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		CronTrigger ct = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(CronScheduleBuilder.cronSchedule(timeExpression)).build();
		try {
			this.theScheduler.scheduleJob(jd, ct);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleDialy(java.lang.Class, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleDialy(Class<T> appTaskClass, String taskName, String description,
			int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.repeatSecondlyForever().withIntervalInHours(hour)
								.withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleWeekly(java.lang.Class, java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleWeekly(Class<T> appTaskClass, String taskName, String description,
			int dayOfWeek, int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().onDaysOfTheWeek(dayOfWeek)
								.withIntervalInHours(hour).withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleMonthly(java.lang.Class, java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleMonthly(Class<T> appTaskClass, String taskName, String description,
			int dayOfMonth, int hour, int minute) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(dayOfMonth)
								.withIntervalInHours(hour).withIntervalInMinutes(minute)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleImmediate(java.lang.Class, java.lang.String, java.lang.String, int, long)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleImmediate(Class<T> appTaskClass, String taskName,
			String description, int repeatCount, long repeatInterval) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInSeconds((int) repeatInterval)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleHourly(java.lang.Class, java.lang.String, java.lang.String, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName, String description,
			int intervalInHours) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(intervalInHours)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleHourly(java.lang.Class, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleHourly(Class<T> appTaskClass, String taskName, String description,
			int intervalInHours, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInHours(intervalInHours)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleMinutely(java.lang.Class, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInMinutes(intervalInMinutes)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleMinutely(java.lang.Class, java.lang.String, java.lang.String, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleMinutely(Class<T> appTaskClass, String taskName,
			String description, int intervalInMinutes) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder.newTrigger().forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(intervalInMinutes)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleSecondly(java.lang.Class, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds, int repeatCount) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
								.withIntervalInMilliseconds(intervalInSeconds * 1000l)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#scheduleSecondly(java.lang.Class, java.lang.String, java.lang.String, int)
	 */
	@Override
	public <T extends AbstractAppTask> void scheduleSecondly(Class<T> appTaskClass, String taskName,
			String description, int intervalInSeconds) {
		JobDetail jd = JobBuilder.newJob(appTaskClass).withIdentity(new JobKey(taskName, Scheduler.DEFAULT_GROUP))
				.withDescription(description).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.forJob(taskName, Scheduler.DEFAULT_GROUP)
				.withIdentity(IdentitiesUtils.randomBase62())
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withRepeatCount(-1)
								.withIntervalInMilliseconds(intervalInSeconds * 1000l)).build();
		try {
			this.theScheduler.scheduleJob(jd, trigger);
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#start()
	 */
	@Override
	public void start() {
		try {
			this.theScheduler.start();
		} catch (SchedulerException ex) {
			throw ExceptionUtils.unchecked(ex);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#pauseAll()
	 */
	@Override
	public void pauseAll() {
		try {
			this.theScheduler.pauseAll();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#resumeAll()
	 */
	@Override
	public void resumeAll() {
		try {
			this.theScheduler.resumeAll();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#shutdown(boolean)
	 */
	@Override
	public void shutdown(boolean waitForJobsToComplete) {
		try {
			this.theScheduler.shutdown(waitForJobsToComplete);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#isShutdown()
	 */
	@Override
	public boolean isShutdown() {
		try {
			return this.theScheduler.isShutdown();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.schedule.SchedulerService#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			this.theScheduler.shutdown();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}
}
