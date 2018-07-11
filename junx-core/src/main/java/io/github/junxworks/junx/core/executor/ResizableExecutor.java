/*
 ***************************************************************************************
 * 
 * @Title:  ResizableExecutor.java   
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

import java.util.concurrent.ExecutorService;

/**
 * 可伸缩的线程池接口定义。
 *
 * @author: Michael
 * @date:   2017-5-9 11:08:17
 * @since:  v1.0
 */
public interface ResizableExecutor extends ExecutorService {

	/**
	 * 获取当前线程池中所有线程数，包含活动和非活动。
	 *
	 * @return 当前线程池中所有线程数，包含活动和非活动
	 */
	public int getPoolSize();

	/**
	 * 返回最大线程池大小。
	 *
	 * @return 最大线程池大小
	 */
	public int getMaxThreads();

	/**
	 * 获取当前活动线程数。
	 *
	 * @return 当前活动线程数
	 */
	public int getActiveCount();

	/**
	 * 动态设置核心线程和最大线程大小。
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @return true, if successful
	 */
	public boolean resizePool(int corePoolSize, int maximumPoolSize);

	/**
	 * 动态设置等待队列大小。
	 *
	 * @param capacity the capacity
	 * @return true, if successful
	 */
	public boolean resizeQueue(int capacity);

}
