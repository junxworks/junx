/*
 ***************************************************************************************
 * 
 * @Title:  StandardThreadExecutor.java   
 * @Package io.github.junxworks.junx.core.executor   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.executor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.exception.FatalException;
import io.github.junxworks.junx.core.lifecycle.Service;
import io.github.junxworks.junx.core.util.ObjectUtils;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.core.util.SystemUtils;

/**
 * 标准线程池类，采用装饰模式封装了TOMCAT的executor，支持自定义长度的等待队列，
 * 同时支持动态设置线程池大小，也支持task提交超时。
 * 使用方式：<br>
 * {@code 
 * StandardThreadExecutor executor=new StandardThreadExecutor();
 * //设置关键属性
 * executor.setMaxQueueSize(xxx);//设置等待队列的长度
 * executor.setMinSpareThreads(xxx);//设置核心线程数，也就是正常运行时候的线程数
 * executor.setMaxThreads(xxx); //最大线程数
 * executor.setMaxIdleTime(xxx);  //最大空闲时间
 * executor.setNamePrefix(xxx);  //设置执行线程前缀
 * executor.setThreadPriority(xxx);  //设置线程优先级
 * executor.setDaemon(xxx); //是否是守护线程，默认true
 * //还有更多的属性设置，请参考每个属性的意义
 * 
 * executor.start(); //启动线程池实例
 * 
 * //提交任务
 * executor.execute(xxx,xxx,xxx); //支持提交任务超时，防止无限等待。
 * }
 * <br>
 * 可以通过{@link #resizePool(int, int)}动态设置线程池大小。
 * 
 * @author: Michael
 * @date:   2017-5-8 18:25:00
 * @since:  v1.0
 */
public class StandardThreadExecutor extends Service implements InnerExecutor, ResizableExecutor {

	/** 线程池配置. */
	protected ExecutorConfig config = new ExecutorConfig();

	/** 内部线程池对象，tomcat封装的执行线程池。 */
	protected ThreadPoolExecutor executor = null;

	/** 线程的任务队列，tomcat内部封装的队列，继承了java.util.concurrent.LinkedBlockingQueue<Runnable>. */
	private TaskQueue taskqueue = null;

	private RejectedExecutionHandler rejectedExecutionHandler;

	/**
	 * 构造一个新的对象.
	 */
	public StandardThreadExecutor() {
	}

	public StandardThreadExecutor(ExecutorConfig config) {
		this.config = config;
	}

	public ExecutorConfig getConfig() {
		return config;
	}

	public void setConfig(ExecutorConfig config) {
		this.config = config;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@Override
	protected void doStart() throws Throwable {
		try {
			taskqueue = new TaskQueue(config.getMaxQueueSize());
			TaskThreadFactory tf = new TaskThreadFactory(config.getNamePrefix(), config.isDaemon(), config.getThreadPriority());
			executor = new ThreadPoolExecutor(config.getMinSpareThreads(), config.getMaxThreads(), config.getMaxIdleTime(), TimeUnit.SECONDS, taskqueue, tf);
			executor.setThreadRenewalDelay(config.getThreadRenewalDelay());
			if (rejectedExecutionHandler == null) {//代码写死的优先级最高，其次是配置
				try {
					RejectedExecutionHandlers handlerEnum = RejectedExecutionHandlers.valueOf(config.getRejectedExecutionHandler());
					rejectedExecutionHandler = handlerEnum.getHandler();
				} catch (Exception e) {
					try {
						rejectedExecutionHandler = (RejectedExecutionHandler) ObjectUtils.createObject(config.getRejectedExecutionHandler());
					} catch (Exception fatal) {
						throw new BaseRuntimeException(StringUtils.format("Exception occurred when init rejectedExecutionHandler for thread executor which name prefix is \"%s\"", config.getNamePrefix()), fatal);
					}
				}
			}
			executor.setRejectedExecutionHandler(rejectedExecutionHandler);
			if (config.isPrestartminSpareThreads()) {
				executor.prestartAllCoreThreads();
			}
			taskqueue.setParent(executor);
		} catch (Exception e) {
			throw new FatalException("StandardThreadExecutor " + this.getServiceName() + " start failed.", e);
		}
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {
		if (executor != null)
			executor.shutdownNow();
		executor = null;
		taskqueue = null;
	}

	/**
	 * @see io.github.junxworks.junx.core.executor.InnerExecutor#execute(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public void execute(Runnable command, long timeout, TimeUnit unit) {
		if (executor != null) {
			executor.execute(command, timeout, unit);
		} else {
			throw new IllegalStateException("StandardThreadExecutor not started.");
		}
	}

	/**
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable command) {
		if (executor != null) {
			try {
				executor.execute(command);
			} catch (RejectedExecutionException rx) {
				//there could have been contention around the queue
				if (!((TaskQueue) executor.getQueue()).force(command))
					throw new RejectedExecutionException("Work queue full.");
			}
		} else
			throw new IllegalStateException("StandardThreadPool not started.");
	}

	//	/**
	//	 * 停止上下文，这个目前用的比较少，tomcat内部使用的
	//	 */
	//	public void contextStopping() {
	//		if (executor != null) {
	//			executor.contextStopping();
	//		}
	//	}

	public int getThreadPriority() {
		return config.getThreadPriority();
	}

	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return rejectedExecutionHandler;
	}

	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}

	public boolean isDaemon() {
		return config.isDaemon();
	}

	public String getNamePrefix() {
		return config.getNamePrefix();
	}

	public int getMaxIdleTime() {
		return config.getMaxIdleTime();
	}

	@Override
	public int getMaxThreads() {
		return config.getMaxThreads();
	}

	public int getMinSpareThreads() {
		return config.getMinSpareThreads();
	}

	public boolean isPrestartminSpareThreads() {
		return config.isPrestartminSpareThreads();
	}

	public void setThreadPriority(int threadPriority) {
		config.setThreadPriority(threadPriority);
	}

	public void setDaemon(boolean daemon) {
		config.setDaemon(daemon);
	}

	public void setNamePrefix(String namePrefix) {
		config.setNamePrefix(namePrefix);
	}

	public void setMaxIdleTime(int maxIdleTime) {
		config.setMaxIdleTime(maxIdleTime);
		if (executor != null) {
			executor.setKeepAliveTime(maxIdleTime, TimeUnit.SECONDS);
		}
	}

	public void setMaxThreads(int maxThreads) {
		maxThreads = maxThreads == 0 ? SystemUtils.SYS_PROCESSORS * 2 : maxThreads;
		config.setMaxThreads(maxThreads);
		if (executor != null) {
			executor.setMaximumPoolSize(maxThreads);
		}
	}

	public void setMinSpareThreads(int minSpareThreads) {
		config.setMinSpareThreads(minSpareThreads);
		if (executor != null) {
			executor.setCorePoolSize(minSpareThreads);
		}
	}

	public void setPrestartminSpareThreads(boolean prestartminSpareThreads) {
		config.setPrestartminSpareThreads(prestartminSpareThreads);
	}

	public void setMaxQueueSize(int maxQueueSize) {
		config.setMaxQueueSize(maxQueueSize);
	}

	public int getMaxQueueSize() {
		return config.getMaxQueueSize();
	}

	public long getThreadRenewalDelay() {
		return config.getThreadRenewalDelay();
	}

	public void setThreadRenewalDelay(long threadRenewalDelay) {
		config.setThreadRenewalDelay(threadRenewalDelay);
		if (executor != null) {
			executor.setThreadRenewalDelay(threadRenewalDelay);
		}
	}

	/**
	 * @see io.github.junxworks.junx.core.executor.ResizableExecutor#getActiveCount()
	 */
	@Override
	public int getActiveCount() {
		return (executor != null) ? executor.getActiveCount() : 0;
	}

	/**
	 * 返回当前线程池对象已经完成的任务数。
	 *
	 * @return 当前线程池对象已经完成的任务数
	 */
	public long getCompletedTaskCount() {
		return (executor != null) ? executor.getCompletedTaskCount() : 0;
	}

	/**
	 * 返回线程池核心线程数。
	 *
	 * @return 线程池核心线程数
	 */
	public int getCorePoolSize() {
		return (executor != null) ? executor.getCorePoolSize() : 0;
	}

	/**
	 * 返回线程池最大池大小。
	 *
	 * @return 线程池最大池大小
	 */
	public int getLargestPoolSize() {
		return (executor != null) ? executor.getLargestPoolSize() : 0;
	}

	/**
	 * @see io.github.junxworks.junx.core.executor.ResizableExecutor#getPoolSize()
	 */
	@Override
	public int getPoolSize() {
		return (executor != null) ? executor.getPoolSize() : 0;
	}

	/**
	 * 返回当前等待队列大小。
	 *
	 * @return 当前等待队列大小
	 */
	public int getQueueSize() {
		return (executor != null) ? executor.getQueue().size() : -1;
	}

	/**
	 * @see io.github.junxworks.junx.core.executor.ResizableExecutor#resizePool(int, int)
	 */
	@Override
	public boolean resizePool(int corePoolSize, int maximumPoolSize) {
		if (executor == null)
			return false;

		executor.setCorePoolSize(corePoolSize);
		executor.setMaximumPoolSize(maximumPoolSize);
		return true;
	}

	/**
	 * @see io.github.junxworks.junx.core.executor.ResizableExecutor#resizeQueue(int)
	 */
	@Override
	public boolean resizeQueue(int capacity) {
		return false;
	}

	/**
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	@Override
	public void shutdown() {
		executor.shutdown();

	}

	/**
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	/**
	 * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
	 */
	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
	 */
	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
	 */
	@Override
	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
	 */
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	/**
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

}