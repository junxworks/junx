/*
 ***************************************************************************************
 * 
 * @Title:  ClientChannelHandler.java   
 * @Package io.github.junxworks.junx.test.netty.client   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:14   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.netty.client;

import io.github.junxworks.junx.netty.call.ReferenceManager;
import io.github.junxworks.junx.netty.message.IoResponse;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		IoResponse res = (IoResponse) new IoResponse().readFromBytes((byte[]) msg);
		ReferenceManager.future(res.getUUID(), ctx, res);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println(evt);
	}
}
