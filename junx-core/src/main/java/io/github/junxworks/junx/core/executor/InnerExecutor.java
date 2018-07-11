/*
 ***************************************************************************************
 * 
 * @Title:  InnerExecutor.java   
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

import java.util.concurrent.TimeUnit;

/**
 * 定义的一个内部线程池接口类，支持提交任务时候设置等待时间，超过这个等待时间，将会抛出RejectedExecutionException异常
 *
 * @author: Michael
 * @date:   2017-5-9 10:54:39
 * @since:  v1.0
 */
public interface InnerExecutor {
	/**
	 * Executes the given command at some time in the future.  The command
	 * may execute in a new thread, in a pooled thread, or in the calling
	 * thread, at the discretion of the <tt>Executor</tt> implementation.
	 * If no threads are available, it will be added to the work queue.
	 * If the work queue is full, the system will wait for the specified
	 * time until it throws a RejectedExecutionException
	 *
	 * @param command the runnable task
	 * @throws java.util.concurrent.RejectedExecutionException if this task
	 * cannot be accepted for execution - the queue is full
	 * @throws NullPointerException if command or unit is null
	*/
	void execute(Runnable command, long timeout, TimeUnit unit);
}
