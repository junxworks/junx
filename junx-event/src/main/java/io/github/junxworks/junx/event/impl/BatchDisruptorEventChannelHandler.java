/*
 ***************************************************************************************
 * 
 * @Title:  BatchDisruptorEventChannelHandler.java   
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

import java.util.ArrayList;
import java.util.List;

import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventChannelHandler;
import io.github.junxworks.junx.event.EventContext;

/**
 * 批处理的事件处理器，一次处理一批事件，只能结合disruptor通道一起使用。
 * 批次的定义目前为一次处理过程后的累计条数，即一次批处理过后ringbuffer中余留的事件条数。
 * 并没有对大小做限制，也没有对时间做限制。
 *
 * @author: Michael
 * @date:   2017-6-28 18:24:26
 * @since:  v1.0
 */
public abstract class BatchDisruptorEventChannelHandler implements EventChannelHandler {

	protected int batchSize = 1024;

	protected List<EventContext> events = new ArrayList<EventContext>(batchSize);

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	/** 
	 * @see io.github.junxworks.junx.event.EventChannelHandler#handleEvent(io.github.junxworks.junx.event.EventContext, io.github.junxworks.junx.event.EventChannel)
	 */
	@Override
	public void handleEvent(EventContext event, EventChannel channel) throws Exception {
		events.add(event);
		if (event.isEndOfBatch() || events.size() >= batchSize) {
			try {
				handleEvents(events, channel);
			} catch (Exception e) {
				throw e;
			} finally {
				events.clear();
			}
		}
	}

	public abstract void handleEvents(List<EventContext> events, EventChannel channel) throws Exception;

}
