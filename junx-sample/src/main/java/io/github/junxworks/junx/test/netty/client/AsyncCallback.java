/*
 ***************************************************************************************
 * 
 * @Title:  AsyncCallback.java   
 * @Package io.github.junxworks.junx.test.netty.client   
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
package io.github.junxworks.junx.test.netty.client;

import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.netty.call.Callback;
import io.github.junxworks.junx.netty.message.IoRequest;
import io.github.junxworks.junx.netty.message.IoResponse;
import io.netty.channel.ChannelHandlerContext;

public class AsyncCallback implements Callback<IoResponse> {

	private IoRequest req;

	public AsyncCallback(IoRequest req) {
		this.req = req;
	}

	@Override
	public void callback(ChannelHandlerContext ctx, IoResponse t) {
		System.out.println("[async] Handler request id:" + t.getRequestId() + " result:" + new String(t.getData()));
	}

	@Override
	public void dead() {
		System.out.println(StringUtils.format("Request \"%s\" is dead.", req.getId()));
	}

}
