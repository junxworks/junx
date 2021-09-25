/*
 ***************************************************************************************
 * 
 * @Title:  TaskThread.java   
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tomcat封装的执行线程类。
 *
 * @author: Michael
 * @date:   2017-5-9 11:15:42
 * @since:  v1.0
 */
public class TaskThread extends Thread {

	/** 常量 log. */
	private static final Logger log = LoggerFactory.getLogger(TaskThread.class);

	/**线程被创建的时间。 */
	private final long creationTime;

	/**
	 * 构造一个新的对象.
	 *
	 * @param group the group
	 * @param target the target
	 * @param name the name
	 */
	public TaskThread(ThreadGroup group, Runnable target, String name) {
		super(group, new WrappingRunnable(target), name);
		this.creationTime = System.currentTimeMillis();
	}

	/**
	 * 构造一个新的对象.
	 *
	 * @param group the group
	 * @param target the target
	 * @param name the name
	 * @param stackSize the stack size
	 */
	public TaskThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, new WrappingRunnable(target), name, stackSize);
		this.creationTime = System.currentTimeMillis();
	}

	/**
	 * @return the time (in ms) at which this thread was created
	 */
	public final long getCreationTime() {
		return creationTime;
	}

	/**
	 * Wraps a {@link Runnable} to swallow any {@link StopPooledThreadException}
	 * instead of letting it go and potentially trigger a break in a debugger.
	 */
	private static class WrappingRunnable implements Runnable {
		
		/** wrapped runnable. */
		private Runnable wrappedRunnable;

		/**
		 * 构造一个新的对象.
		 *
		 * @param wrappedRunnable the wrapped runnable
		 */
		WrappingRunnable(Runnable wrappedRunnable) {
			this.wrappedRunnable = wrappedRunnable;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				wrappedRunnable.run();
			} catch (StopPooledThreadException exc) {
				//expected : we just swallow the exception to avoid disturbing
				//debuggers like eclipse's
				log.debug("Thread exiting on purpose", exc);
			}
		}

	}

}
