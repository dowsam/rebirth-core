/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core AbstractAppTask.java 2012-2-3 11:29:40 l.xue.nong$$
 */
package cn.com.rebirth.core.schedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定义抽象的Task任务.
 *
 * @author l.xue.nong
 */
public abstract class AbstractAppTask implements Job {

	/** The job conetext. */
	protected JobExecutionContext jobConetext;

	/**
	 * Instantiates a new abstract app task.
	 */
	public AbstractAppTask() {
		super();
	}

	/**
	 * 获取上一次被执行的时间.
	 *
	 * @return Date
	 */
	public Date getPreviousFireTime() {
		return jobConetext.getPreviousFireTime();
	}

	/**
	 * 获取将被执行的时间.
	 *
	 * @return Date
	 */
	public Date getNextFireTime() {
		return jobConetext.getNextFireTime();
	}

	/**
	 * The actual time the trigger fired. For instance the scheduled time may
	 * have been 10:00:00 but the actual fire time may have been 10:00:03 if the
	 * scheduler was too busy.
	 * 
	 * @return Date
	 */
	public Date getFireTime() {
		return jobConetext.getFireTime();
	}

	/**
	 * doTask－－执行任务的抽象方法，在此方法内实现任务代码.
	 */
	public abstract void doTask();

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	/**
	 * Execute.
	 *
	 * @param jobExecutionContext the job execution context
	 * @throws JobExecutionException the job execution exception
	 */
	final public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		this.jobConetext = jobExecutionContext;
		this.doTask();
	}

	/**
	 * Gets the task name.
	 *
	 * @return the task name
	 */
	public String getTaskName() {
		return this.jobConetext.getJobDetail().getDescription();
	}
}
