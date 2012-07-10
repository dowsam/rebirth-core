package cn.com.rebirth.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.com.rebirth.core.utils.TokenUtils;

/**
 * The Class AsynsService.
 *
 * @author l.xue.nong
 */
public class AsynsService implements InitializingBean, DisposableBean, BeanNameAware, AsynServiceHandle {

	/** The Constant LOG. */
	protected static final Logger LOG = LoggerFactory.getLogger(AsynsService.class);

	/** The shutdown executor service. */
	protected boolean shutdownExecutorService = true;

	/** The bean name. */
	private String beanName;

	/** The thread pool size. */
	protected int threadPoolSize = 2;

	/** The executor service. */
	protected ExecutorService executorService;

	/** The wait for tasks to complete on shutdown. */
	protected boolean waitForTasksToCompleteOnShutdown = true;

	/** The async factory. */
	protected AsyncFactory asyncFactory = new DefaultAsyncFactory();

	/* (non-Javadoc)
	 * @see cn.com.summall.core.concurrent.AsynServiceHandle#submit(java.util.concurrent.Callable)
	 */
	@Override
	public <T> Future<T> submit(Callable<T> callable) {
		FutureTask<T> task = new FutureTask<T>(callable);
		TokenUtils.execute(executorService, asyncFactory, task);
		return task;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.concurrent.AsynServiceHandle#execute(cn.com.summall.core.concurrent.Token, java.util.concurrent.Callable)
	 */
	@Override
	public <T> void execute(final Token<T> token, final Callable<T> task) {
		TokenUtils.execute(executorService, token, task);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.concurrent.AsynServiceHandle#execute(cn.com.summall.core.concurrent.Token, java.lang.Runnable)
	 */
	@Override
	public <T> void execute(final Token<T> token, final Runnable task) {
		TokenUtils.execute(executorService, token, task);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.concurrent.AsynServiceHandle#execute(java.util.concurrent.Callable)
	 */
	@Override
	public <T> Token<T> execute(final Callable<T> task) {
		return TokenUtils.execute(executorService, asyncFactory, task);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.core.concurrent.AsynServiceHandle#execute(java.lang.Runnable)
	 */
	@Override
	public <T> Token<T> execute(final Runnable task) {
		return TokenUtils.execute(executorService, asyncFactory, task);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		if (shutdownExecutorService) {
			shutdown();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (executorService == null && threadPoolSize > 0) {
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * threadPoolSize,
					new ThreadFactory() {
						private final AtomicInteger threadNumber = new AtomicInteger(1);

						public Thread newThread(Runnable r) {
							return new Thread(r, "AsynsService-" + threadNumber.getAndIncrement());
						}
					});
			LOG.info("create Index executorService,threadPoolSize:" + threadPoolSize);
		}
		Assert.notNull(executorService, "executorService must be not null");
		Assert.notNull(asyncFactory, "asyncFactory must be not null");
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		LOG.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		if (waitForTasksToCompleteOnShutdown)
			executorService.shutdown();
		else
			executorService.shutdownNow();
	}

	/**
	 * Checks if is wait for tasks to complete on shutdown.
	 *
	 * @return true, if is wait for tasks to complete on shutdown
	 */
	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	/**
	 * Sets the wait for tasks to complete on shutdown.
	 *
	 * @param waitForTasksToCompleteOnShutdown the new wait for tasks to complete on shutdown
	 */
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	/**
	 * Gets the executor service.
	 *
	 * @return the executor service
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * Sets the executor service.
	 *
	 * @param executorService the new executor service
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * Sets the shutdown executor service.
	 *
	 * @param shutdownExecutorService the new shutdown executor service
	 */
	public void setShutdownExecutorService(boolean shutdownExecutorService) {
		this.shutdownExecutorService = shutdownExecutorService;
	}

	/**
	 * Checks if is shutdown executor service.
	 *
	 * @return true, if is shutdown executor service
	 */
	public boolean isShutdownExecutorService() {
		return shutdownExecutorService;
	}

	/**
	 * Gets the thread pool size.
	 *
	 * @return the thread pool size
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	/**
	 * Sets the thread pool size.
	 *
	 * @param threadPoolSize the new thread pool size
	 */
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	/**
	 * Gets the async factory.
	 *
	 * @return the async factory
	 */
	public AsyncFactory getAsyncFactory() {
		return asyncFactory;
	}

	/**
	 * Sets the async factory.
	 *
	 * @param asyncFactory the new async factory
	 */
	public void setAsyncFactory(AsyncFactory asyncFactory) {
		this.asyncFactory = asyncFactory;
	}

}
