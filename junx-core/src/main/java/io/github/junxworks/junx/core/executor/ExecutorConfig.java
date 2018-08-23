/*
 ***************************************************************************************
 * 
 * @Title:  ExecutorConfig.java   
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

import io.github.junxworks.junx.core.util.SystemUtils;

public class ExecutorConfig {
	/** 线程优先级，默认java.lang.Thread.NORM_PRIORITY */
	private int threadPriority = Thread.NORM_PRIORITY;

	/** 是否是守护线程，默认是true。 */
	private boolean daemon = true;

	/** 线程名前缀，这个参数是传递给线程工厂用的，默认"sys-exec-"，可以根据实际使用情况定义 */
	private String namePrefix = "sys-exec-";

	/** 线程池最大线程数，默认Runtime.getRuntime().availableProcessors() * 2 */
	private int maxThreads = SystemUtils.SYS_PROCESSORS * 2;

	/** 核心线程数，也就是空闲的时候，线程池需要维护的线程数，默认 Runtime.getRuntime().availableProcessors() / 2 + 1。*/
	private int minSpareThreads = SystemUtils.SYS_PROCESSORS / 2 + 1;

	/** 最大空闲时间，当线程池中的线程数超过核心线程数minSpareThreads时候，如果有线程已经超过最大空闲时间没有接受到任务，那么这个线程会被线程池释放掉，默认300秒。 */
	private int maxIdleTime = 300;

	/** 是否需要在线程池启动的时候就创建核心线程。 */
	private boolean prestartminSpareThreads = false;

	/** 等待队列最大长度，默认是10000。 */
	private int maxQueueSize = 10000;

	/** 为了避免在上下文停止之后，所有的线程在同一时间段被更新，所以进行线程的延迟操作，默认是1000毫秒延迟。 */
	private long threadRenewalDelay = 1000l;

	/** 任务队列溢出后的拒绝策略，默认Abort，参考{@link io.github.junxworks.junx.core.executor.RejectedExecutionHandlers}，自定义的handler直接采用类全路径名（不能是内部类）. */
	private String rejectedExecutionHandler = "Abort";

	public int getThreadPriority() {
		return threadPriority;
	}

	public void setThreadPriority(int threadPriority) {
		this.threadPriority = threadPriority;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMinSpareThreads() {
		return minSpareThreads;
	}

	public void setMinSpareThreads(int minSpareThreads) {
		this.minSpareThreads = minSpareThreads;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public boolean isPrestartminSpareThreads() {
		return prestartminSpareThreads;
	}

	public void setPrestartminSpareThreads(boolean prestartminSpareThreads) {
		this.prestartminSpareThreads = prestartminSpareThreads;
	}

	public int getMaxQueueSize() {
		return maxQueueSize;
	}

	public void setMaxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	public long getThreadRenewalDelay() {
		return threadRenewalDelay;
	}

	public void setThreadRenewalDelay(long threadRenewalDelay) {
		this.threadRenewalDelay = threadRenewalDelay;
	}

	public String getRejectedExecutionHandler() {
		return rejectedExecutionHandler;
	}

	public void setRejectedExecutionHandler(String rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}

}
