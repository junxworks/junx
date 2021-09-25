/*
 ***************************************************************************************
 * 
 * @Title:  CallUtils.java   
 * @Package io.github.junxworks.junx.netty.call   
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
package io.github.junxworks.junx.netty.call;

import java.io.IOException;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.netty.message.IoRequest;
import io.netty.channel.Channel;

public class CallUtils {
	public static <T> CallFuture<T> call(Channel channel, IoRequest req) throws IOException {
		return call(channel, req, null);
	}

	public static <T> CallFuture<T> call(Channel channel, IoRequest req, Callback<T> callback) throws IOException {
		if (!channel.isActive()) {
			throw new BaseRuntimeException("Channel is inactive.");
		}
		CallFuture<T> f = ReferenceManager.prepare(req.getUUID());//一定是先进入ReferenceManager缓存
		f.setCallback(callback);
		channel.writeAndFlush(req.toBytes());//再发送数据
		return f;
	}
}
