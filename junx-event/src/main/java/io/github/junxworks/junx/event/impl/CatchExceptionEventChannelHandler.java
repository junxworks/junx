/*
 ***************************************************************************************
 * 
 * @Title:  CatchExceptionEventChannelHandler.java   
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

import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventChannelHandler;
import io.github.junxworks.junx.event.EventContext;

/**
 * 捕获内部异常，并且会发布处理异常的事件
 *
 * @ClassName:  CatchExceptionEventChannelHandler
 * @author: Michael
 * @date:   2018-7-6 14:14:33
 * @since:  v1.0
 */
public abstract class CatchExceptionEventChannelHandler implements EventChannelHandler {

	@Override
	public void handleEvent(EventContext event, EventChannel channel) throws Exception {
		try {
			doHandleEvent(event, channel);
		} catch (Throwable t) {
			publishExceptionEvent(event, channel, t);
		}
	}

	protected abstract void doHandleEvent(EventContext event, EventChannel channel) throws Exception;

	protected void publishExceptionEvent(EventContext event, EventChannel channel, Throwable t) throws Exception {
		event.setData(EXCEPTION_PARAM, t);
		channel.publish(EXCEPTION_TOPIC, event); //直接把event发布到exception主题中
	}

}
