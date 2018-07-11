/*
 ***************************************************************************************
 * 
 * @Title:  ExecutorEventChannel.java   
 * @Package io.github.junxworks.junx.event.impl   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:47:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.event.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import io.github.junxworks.junx.core.exception.FatalException;
import io.github.junxworks.junx.core.executor.StandardThreadExecutor;
import io.github.junxworks.junx.core.util.NumberUtils;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventContext;

/**
 * 支持并发的事件通道，此通道是单独的线程，具有内部的线程池，具体触发event执行的都是通过执行线程去实现的。
 * 详细介绍参考{@link io.github.junxworks.junx.event.EventChannel}
 *
 * @author: Michael
 * @date:   2017-5-10 9:50:02
 * @since:  v1.0
 */
public class ExecutorEventChannel extends AbstractEventChannel {

	/** 默认缓存队列大小. */
	protected int DEFAULT_EVENT_BUFFERSIZE = 1024 * 8;

	/** 缓存队列大小，建议通过配置实现. */
	protected int bufferSize = 0;

	/** 是否是守护线程，默认是true。 */
	protected boolean daemon = true;

	/** 最小线程数，核心线程数，如果不设置，则默认为threadCount. */
	protected Integer minThreads;

	/** 最大线程数，如果不设置，则默认为threadCount和minThreads中的最大值. */
	protected Integer maxThreads;

	/** 线程池最大线程数，默认Runtime.getRuntime().availableProcessors() * 2 + 1 */
	protected int threadCount = Runtime.getRuntime().availableProcessors() * 2 + 1;

	protected boolean callerRunTaskWhenBusy = false;

	/** 通道内部使用的线程池. */
	private ExecutorService executor;

	private boolean shutdownExecutor = false;

	public ExecutorEventChannel(String channelName, String topic) {
		super(channelName, topic);
	}

	public boolean isCallerRunTaskWhenBusy() {
		return callerRunTaskWhenBusy;
	}

	public void setCallerRunTaskWhenBusy(boolean callerRunTaskWhenBusy) {
		this.callerRunTaskWhenBusy = callerRunTaskWhenBusy;
	}

	public Integer getMinThreads() {
		return minThreads == null ? threadCount : minThreads;
	}

	public void setMinThreads(Integer minThreads) {
		this.minThreads = minThreads;
	}

	public Integer getMaxThreads() {
		return NumberUtils.max(maxThreads == null ? threadCount : maxThreads, getMinThreads());
	}

	public void setMaxThreads(Integer maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public int getBufferSize() {
		return bufferSize == 0 ? DEFAULT_EVENT_BUFFERSIZE : bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#onEvent(io.github.junxworks.junx.event.EventContext)
	 */
	@Override
	public void onEvent(final EventContext event) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					//这里要注意，handler内部是多线程的，最好不要有竞争性资源，避免handler内部线程同步
					handler.handleEvent(event, ExecutorEventChannel.this);
				} catch (Exception e) {
					logger.error(StringUtils.format("Exception occurred when processing event [topic=%s,source=%s,createtime=%s]", event.getTopic(), event.getSourceChannel(), event.getClock().countMillis()), e);
				}
			}
		});
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@Override
	protected void doStart() throws Throwable {
		try {
			//线程池采用的提交拒绝策略为CallerRunsPolicy，由提交线程自己去调用task
			//		executor = new ThreadPoolExecutor(threadCount, threadCount, 15, TimeUnit.MINUTES, new ArrayBlockingQueue<>(getBufferSize()), new TaskThreadFactory(getName() + "-exec-", true, Thread.NORM_PRIORITY), new CallerRunsPolicy());
			if (executor == null) {
				executor = new StandardThreadExecutor();
				StandardThreadExecutor _executor = (StandardThreadExecutor) executor;
				String threadPrefix = (StringUtils.isNull(name) ? topic : name) + "-";
				_executor.setNamePrefix(threadPrefix);
				_executor.setMinSpareThreads(getMinThreads());
				_executor.setMaxThreads(getMaxThreads());
				_executor.setMaxIdleTime(15 * 60);//15分钟
				_executor.setMaxQueueSize(getBufferSize());
				if (callerRunTaskWhenBusy) {
					_executor.setRejectedExecutionHandler(new CallerRunsPolicy());
				}
				_executor.start();
				shutdownExecutor = true; //内部生产的线程池需要在stop的时候关掉
			}
		} catch (Throwable e) {
			throw new FatalException(StringUtils.format("Exception accurred when start ConcurrentEventChannel \"%s\" on topic \"%s\"", getServiceName(), topic), e);
		}
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {
		if (shutdownExecutor) {
			executor.shutdown();
		}
	}

}
