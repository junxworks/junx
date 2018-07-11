/*
 ***************************************************************************************
 * 
 * @Title:  DisruptorEventHandler.java   
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

import com.lmax.disruptor.EventHandler;

import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventDispatcher;

/**
 * bus的处理类只负责分发event到具体的eventchannel中去，不做任何其他处理。
 *
 * @author: Michael
 * @date:   2017-5-9 17:32:48
 * @since:  v1.0
 */
public class DisruptorEventHandler implements EventHandler<EventContext> {

	/** dispatcher. */
	private EventDispatcher dispatcher;

	/**
	 * 构造一个新的对象.
	 *
	 * @param dispatcher the dispatcher
	 */
	public DisruptorEventHandler(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @see com.lmax.disruptor.EventHandler#onEvent(java.lang.Object, long, boolean)
	 */
	@Override
	public void onEvent(EventContext event, long sequence, boolean endOfBatch) throws Exception {
		event.setSequence(sequence);
		event.setEndOfBatch(endOfBatch);
		dispatcher.dispatch(event);
	}

}
