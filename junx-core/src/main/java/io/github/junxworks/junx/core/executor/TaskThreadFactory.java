/*
 ***************************************************************************************
 * 
 * @Title:  TaskThreadFactory.java   
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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * tomcat封装的TaskThread对象的工厂类.
 *
 * @ClassName:  TaskThreadFactory
 * @author: michael
 * @date:   2017-5-8 18:22:51
 * @since:  v1.0
 */
public class TaskThreadFactory implements ThreadFactory {

	/** group. */
	private final ThreadGroup group;

	/** thread number. */
	private final AtomicInteger threadNumber = new AtomicInteger(1);

	/** name prefix. */
	private final String namePrefix;

	/** daemon. */
	private final boolean daemon;

	/** thread priority. */
	private final int threadPriority;

	/**
	 * 构造一个新的对象.
	 *
	 * @param namePrefix the name prefix
	 * @param daemon the daemon
	 * @param priority the priority
	 */
	public TaskThreadFactory(String namePrefix, boolean daemon, int priority) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = namePrefix;
		this.daemon = daemon;
		this.threadPriority = priority;
	}

	/**
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			// Threads should not be created by the webapp classloader
			if (System.getSecurityManager() != null) {
				PrivilegedAction<Void> pa = new PrivilegedSetTccl(getClass().getClassLoader());
				AccessController.doPrivileged(pa);
			} else {
				Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			}
			TaskThread t = new TaskThread(group, r, namePrefix + threadNumber.getAndIncrement());
			t.setDaemon(daemon);
			t.setPriority(threadPriority);
			return t;
		} finally {
			if (System.getSecurityManager() != null) {
				PrivilegedAction<Void> pa = new PrivilegedSetTccl(loader);
				AccessController.doPrivileged(pa);
			} else {
				Thread.currentThread().setContextClassLoader(loader);
			}
		}
	}
}
