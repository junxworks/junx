package io.github.junxworks.junx.test.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.junxworks.junx.core.util.ExceptionUtils;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.netty.message.IoRequest;
import io.github.junxworks.junx.netty.message.IoResponse;
import io.github.junxworks.junx.netty.message.MessageConstants;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
@Sharable
public class ServerEventHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ServerEventHandler.class);

	private static final byte[] data = "I`m OK!!!".getBytes();

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		IoRequest req = new IoRequest().readFromBytes((byte[]) msg);
		System.out.println(new String(req.getData()));
		IoResponse res = new IoResponse();
		res.setRequestId(req.getId());
		res.setServerAddr(ctx.channel().localAddress().toString());
		res.setData(data);
		ctx.writeAndFlush(res.toBytes());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			IoResponse response = new IoResponse();
			response.setRequestId("unknown");
			response.setStatusCode(MessageConstants.STATUS_CODE_SERVER_INTERNAL_ERROR);
			String errorMsg = ExceptionUtils.getCauseMessage(cause);
			response.setData(StringUtils.isNull(errorMsg) ? "Server internal error.".getBytes() : errorMsg.getBytes());
			response.setServerAddr(ctx.channel().localAddress().toString());
			ctx.writeAndFlush(response.toBytes());
		}
		logger.error("Netty handler catch exption.", cause);
	}
}