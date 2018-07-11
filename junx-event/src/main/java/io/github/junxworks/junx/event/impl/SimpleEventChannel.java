/*
 ***************************************************************************************
 * 
 * @Title:  SimpleEventChannel.java   
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

import io.github.junxworks.junx.event.EventContext;

/**
 * 单线程事件通道，具体说明参考{@link io.github.junxworks.junx.event.EventChannel}
 *
 * @author: Michael
 * @date:   2017-5-10 15:45:06
 * @since:  v1.0
 */
public class SimpleEventChannel extends AbstractEventChannel {

	public SimpleEventChannel(String channelName, String topic) {
		super(channelName, topic);
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#onEvent(io.github.junxworks.junx.event.EventContext)
	 */
	@Override
	public void onEvent(EventContext event) throws Exception {
		this.handler.handleEvent(event, this);
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@Override
	protected void doStart() throws Throwable {

	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {

	}

}
