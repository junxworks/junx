/*
 ***************************************************************************************
 * 
 * @Title:  ServerHeartbeatCheckHandler.java   
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 带有心跳检测机制的handler，此handler有状态，不能在多个channel之间共享
 * 注意：必须添加在业务handler的前面
 * @ClassName:  HeartbeatCheckHandler
 * @author: Michael
 * @date:   2018-5-30 14:41:48
 * @since:  v1.0
 */
public class ServerHeartbeatCheckHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ServerHeartbeatCheckHandler.class);

	private static final int DEFAULT_IDLE_THRESHOLD = 3;

	/** 空闲超时计数. */
	private int idleCount = 0;

	/** 空闲超时计数上限，超过这个数会断开此连接. */
	private int idleCountThreshold = DEFAULT_IDLE_THRESHOLD;

	/** 心跳包. */
	private Object heartbeatResponse;

	private HeartbeatMessageFilter heartbeatMessageFilter;

	public ServerHeartbeatCheckHandler(HeartbeatMessageFilter heartbeatMessageFilter, Object heartbeatResponse) {
		this.heartbeatResponse = heartbeatResponse;
		this.heartbeatMessageFilter = heartbeatMessageFilter;
	}

	public int getIdleCount() {
		return idleCount;
	}

	public void setIdleCount(int idleCount) {
		this.idleCount = idleCount;
	}

	public int getIdleCountThreshold() {
		return idleCountThreshold;
	}

	public void setIdleCountThreshold(int idleCountThreshold) {
		this.idleCountThreshold = idleCountThreshold;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//不管是接收到什么消息，只要有通信，闲置计数器就清零
		idleCount = 0;
		if (heartbeatMessageFilter.isHeartbeatMssage(msg)) {
			ctx.writeAndFlush(heartbeatResponse);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			if (++idleCount >= idleCountThreshold) {
				logger.info("Channel \"{}\" idle for {} times,it will be closed.", ctx.channel().remoteAddress().toString(), idleCountThreshold);
				ctx.close();
			}
		}
	}
}
