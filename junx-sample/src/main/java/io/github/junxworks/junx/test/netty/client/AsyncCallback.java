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
