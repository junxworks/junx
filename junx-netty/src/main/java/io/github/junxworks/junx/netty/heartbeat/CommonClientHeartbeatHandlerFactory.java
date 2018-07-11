/*
 ***************************************************************************************
 * 
 * @Title:  CommonClientHeartbeatHandlerFactory.java   
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

import io.github.junxworks.junx.netty.message.MessageConstants;
import io.netty.channel.ChannelInboundHandler;

/**
 * 通用客户端心跳处理器工厂
 *
 * @ClassName:  CommonClientHeartbeatHandlerFactory
 * @author: Administrator
 * @date:   2018-7-7 10:31:24
 * @since:  v1.0
 */
public class CommonClientHeartbeatHandlerFactory implements HeartbeatHandlerFactory {
	
	/** heartbeat request bytes. */
	public static byte[] HEARTBEAT_REQUEST_BYTES=new byte[] {MessageConstants.MESSAGE_TYPE_HEARTBEAT};

	@Override
	public ChannelInboundHandler createHeartbeatHandler() throws Exception {
		return new ClientHeartbeatCheckHandler(new HeartbeatMessageFilter() {
			@Override
			public boolean isHeartbeatMssage(Object message) {
				return message != null && ((byte[]) message)[0] == MessageConstants.MESSAGE_TYPE_HEARTBEAT;
			}
		}, HEARTBEAT_REQUEST_BYTES);
	}

}
