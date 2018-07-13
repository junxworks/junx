/*
 ***************************************************************************************
 * 
 * @Title:  SimpleChannelTestHandler.java   
 * @Package io.github.junxworks.junx.test.event   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.event;

import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventChannelHandler;
import io.github.junxworks.junx.event.EventContext;

public class SimpleChannelTestHandler implements EventChannelHandler {

	@Override
	public void handleEvent(EventContext event, EventChannel channel) throws Exception {
		String myName = event.getData(String.class, Params.NAME);
		System.out.println(Thread.currentThread().getName() + ":" + myName);
	}

}
