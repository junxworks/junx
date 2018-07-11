/*
 ***************************************************************************************
 * 
 * @Title:  CommonChannelInitializer.java   
 * @Package io.github.junxworks.junx.netty.initializer   
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
package io.github.junxworks.junx.netty.initializer;

import java.util.concurrent.TimeUnit;

import io.github.junxworks.junx.netty.ServerConfig;
import io.github.junxworks.junx.netty.heartbeat.HeartbeatHandlerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 通用通道初始化器
 *
 * @ClassName:  CommonChannelInitializer
 * @author: Michael
 * @date:   2018-7-6 21:55:04
 * @since:  v1.0
 */
public class CommonChannelInitializer extends ChannelInitializer<Channel> {

	private int readIdle = ServerConfig.DEFAULT_READ_IDLE;

	private int writeIdle = ServerConfig.DEFAULT_WRITE_IDLE;

	private int allIdle = ServerConfig.DEFAULT_ALL_IDLE;

	private ChannelInboundHandler eventHandler;

	private HeartbeatHandlerFactory heartbeatHandlerFactory;

	public CommonChannelInitializer(ChannelInboundHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public HeartbeatHandlerFactory getHeartbeatHandlerFactory() {
		return heartbeatHandlerFactory;
	}

	public void setHeartbeatHandlerFactory(HeartbeatHandlerFactory heartbeatHandlerFactory) {
		this.heartbeatHandlerFactory = heartbeatHandlerFactory;
	}

	public int getReadIdle() {
		return readIdle;
	}

	public void setReadIdle(int readIdle) {
		this.readIdle = readIdle;
	}

	public int getWriteIdle() {
		return writeIdle;
	}

	public void setWriteIdle(int writeIdle) {
		this.writeIdle = writeIdle;
	}

	public int getAllIdle() {
		return allIdle;
	}

	public void setAllIdle(int allIdle) {
		this.allIdle = allIdle;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));//netty基于长度的
		p.addLast("decoder", new ByteArrayDecoder());
		p.addLast("frameEncoder", new LengthFieldPrepender(4)); // 添加MessagePack编码器
		p.addLast("encoder", new ByteArrayEncoder());
		if (heartbeatHandlerFactory != null) {//心跳检测
			p.addLast("idleStateHandler", new IdleStateHandler(readIdle, writeIdle, allIdle, TimeUnit.SECONDS));//超时检测
			p.addLast(heartbeatHandlerFactory.createHeartbeatHandler());
		}
		p.addLast("eventHandler", eventHandler);

	}

}
