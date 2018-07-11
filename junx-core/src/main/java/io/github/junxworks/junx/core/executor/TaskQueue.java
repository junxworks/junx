/*
 ***************************************************************************************
 * 
 * @Title:  TaskQueue.java   
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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * tomcat内部的任务队列，继承了{@link java.util.concurrent.LinkedBlockingQueue<Runnable>}
 *
 * @author: Michael
 * @date:   2017-5-9 11:12:09
 * @since:  v1.0
 */
public class TaskQueue extends LinkedBlockingQueue<Runnable> {

	/** 常量 serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 跟此对象关联的线程池。 */
	private ThreadPoolExecutor parent = null;

	/** no need to be volatile, the one times when we change and read it occur in a single thread (the one that did stop a context and fired listeners). */
	private Integer forcedRemainingCapacity = null;

	/**
	 * 构造一个新的对象.
	 */
	public TaskQueue() {
		super();
	}

	/**
	 * 构造一个新的对象.
	 *
	 * @param capacity the capacity
	 */
	public TaskQueue(int capacity) {
		super(capacity);
	}

	/**
	 * 构造一个新的对象.
	 *
	 * @param c the c
	 */
	public TaskQueue(Collection<? extends Runnable> c) {
		super(c);
	}

	public void setParent(ThreadPoolExecutor tp) {
		parent = tp;
	}

	/**
	 * Force.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public boolean force(Runnable o) {
		if (parent == null || parent.isShutdown())
			throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
		return super.offer(o); //forces the item onto the queue, to be used if the task is rejected
	}

	/**
	 * 当任务提交被拒绝的时候，强制把任务提交到任务队列
	 *
	 * @param o 执行任务
	 * @param timeout 超时时间
	 * @param unit 时间单位
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
		if (parent == null || parent.isShutdown())
			throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
		return super.offer(o, timeout, unit); //forces the item onto the queue, to be used if the task is rejected
	}

	/**
	 * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(Runnable o) {
		//we can't do any checks
		if (parent == null)
			return super.offer(o);
		//we are maxed out on threads, simply queue the object
		if (parent.getPoolSize() == parent.getMaximumPoolSize())
			return super.offer(o);
		//we have idle threads, just add it to the queue
		if (parent.getSubmittedCount() < (parent.getPoolSize()))
			return super.offer(o);
		//if we have less threads than maximum force creation of a new thread
		if (parent.getPoolSize() < parent.getMaximumPoolSize())
			return false;
		//if we reached here, we need to add it to the queue
		return super.offer(o);
	}

	/**
	 * @see java.util.concurrent.LinkedBlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
		Runnable runnable = super.poll(timeout, unit);
		if (runnable == null && parent != null) {
			// the poll timed out, it gives an opportunity to stop the current
			// thread if needed to avoid memory leaks.
			parent.stopCurrentThreadIfNeeded();
		}
		return runnable;
	}

	/**
	 * @see java.util.concurrent.LinkedBlockingQueue#take()
	 */
	@Override
	public Runnable take() throws InterruptedException {
		if (parent != null && parent.currentThreadShouldBeStopped()) {
			return poll(parent.getKeepAliveTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
			// yes, this may return null (in case of timeout) which normally
			// does not occur with take()
			// but the ThreadPoolExecutor implementation allows this
		}
		return super.take();
	}

	/**
	 * @see java.util.concurrent.LinkedBlockingQueue#remainingCapacity()
	 */
	@Override
	public int remainingCapacity() {
		if (forcedRemainingCapacity != null) {
			// ThreadPoolExecutor.setCorePoolSize checks that
			// remainingCapacity==0 to allow to interrupt idle threads
			// I don't see why, but this hack allows to conform to this
			// "requirement"
			return forcedRemainingCapacity.intValue();
		}
		return super.remainingCapacity();
	}

	public void setForcedRemainingCapacity(Integer forcedRemainingCapacity) {
		this.forcedRemainingCapacity = forcedRemainingCapacity;
	}

}
