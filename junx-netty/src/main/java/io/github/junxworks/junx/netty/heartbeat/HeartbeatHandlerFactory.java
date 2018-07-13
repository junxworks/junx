/*
 ***************************************************************************************
 * 
 * @Title:  HeartbeatHandlerFactory.java   
 * @Package io.github.junxworks.junx.netty.heartbeat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:52:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.netty.heartbeat;

import io.netty.channel.ChannelInboundHandler;

/**
 * HeartbeatHandler 对象的工厂类.
 *
 * @ClassName:  HeartbeatHandlerFactory
 * @author: Michael
 * @date:   2018-7-6 23:07:03
 * @since:  v1.0
 */
public interface HeartbeatHandlerFactory {
	public ChannelInboundHandler createHeartbeatHandler() throws Exception;
}
