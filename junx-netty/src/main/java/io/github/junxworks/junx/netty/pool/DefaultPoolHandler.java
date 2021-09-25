/*
 ***************************************************************************************
 * 
 * @Title:  DefaultPoolHandler.java   
 * @Package io.github.junxworks.junx.netty.pool   
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
package io.github.junxworks.junx.netty.pool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;

public class DefaultPoolHandler implements ChannelPoolHandler {

	private ChannelInitializer<Channel> channelInitializer;

	public DefaultPoolHandler(ChannelInitializer<Channel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	@Override
	public void channelReleased(Channel ch) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelAcquired(Channel ch) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		if (ch instanceof SocketChannel) {
			SocketChannel channel = (SocketChannel) ch;
			channel.config().setKeepAlive(true);
			channel.config().setTcpNoDelay(true);
		}
		ch.pipeline().addLast(channelInitializer);
	}
}
