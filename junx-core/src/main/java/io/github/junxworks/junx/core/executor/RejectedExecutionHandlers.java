/*
 ***************************************************************************************
 * 
 * @Title:  RejectedExecutionHandlers.java   
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

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

/**
 * 拒收处理器枚举类
 *
 * @ClassName:  RejectedExecutionHandlers
 * @author: Michael
 * @date:   2018-6-26 18:47:20
 * @since:  v1.0
 */
public enum RejectedExecutionHandlers {

	/** 抛出RejectedExecutionException异常，并且中断操作. */
	Abort(new AbortPolicy()),
	/** 直接放弃执行并且不抛出任何异常. */
	Discard(new DiscardPolicy()),
	/** 由调用线程自己执行. */
	CallerRuns(new CallerRunsPolicy()),
	/** 放弃最长时间没有执行的任务，将新任务加入到队尾. */
	DiscardOldest(new DiscardOldestPolicy());

	/**
	 * 构造一个新的 rejected execution handlers 对象.
	 *
	 * @param handler the handler
	 */
	private RejectedExecutionHandlers(RejectedExecutionHandler handler) {
		this.handler = handler;
	}

	/** handler. */
	private RejectedExecutionHandler handler;

	public RejectedExecutionHandler getHandler() {
		return handler;
	}

}
