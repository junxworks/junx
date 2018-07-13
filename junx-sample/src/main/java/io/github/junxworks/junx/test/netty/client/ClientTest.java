/*
 ***************************************************************************************
 * 
 * @Title:  ClientTest.java   
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

import java.net.InetSocketAddress;

import org.junit.Test;

import io.github.junxworks.junx.netty.call.CallFuture;
import io.github.junxworks.junx.netty.call.CallUtils;
import io.github.junxworks.junx.netty.heartbeat.CommonClientHeartbeatHandlerFactory;
import io.github.junxworks.junx.netty.initializer.CommonChannelInitializer;
import io.github.junxworks.junx.netty.message.IoRequest;
import io.github.junxworks.junx.netty.message.IoResponse;
import io.github.junxworks.junx.netty.pool.NettyChannelPool;
import io.github.junxworks.junx.netty.pool.NettyChannelPoolManager;
import io.netty.channel.Channel;

public class ClientTest {

	private String host = "localhost";

	private int port = 8080;

	private byte[] data = "michael".getBytes();

	/**
	 * 同步调用测试
	 */
	@Test
	public void syncRequestTest() throws Exception {
		NettyChannelPoolManager poolManager = new NettyChannelPoolManager();
		poolManager.start();
		InetSocketAddress serverAddr = InetSocketAddress.createUnresolved(host, port);
		NettyChannelPool pool = poolManager.getPool(serverAddr);
		if (pool == null) {
			CommonChannelInitializer initializer = new CommonChannelInitializer(new ClientChannelHandler());
			initializer.setHeartbeatHandlerFactory(new CommonClientHeartbeatHandlerFactory());//长连接，客户端心跳
			pool = poolManager.getPool(serverAddr, initializer);
		}
		Channel ch = pool.acquire(1000);
		try {
			IoRequest req = new IoRequest();
			req.setRequestTimeout(1000);
			req.setData(data);
			CallFuture<IoResponse> syn = CallUtils.call(ch, req);
			IoResponse res = syn.get(); //线程同步等待应答
			System.out.println("[Sync] Handler request id:" + res.getRequestId() + " result:" + new String(res.getData()));
		} finally {
			pool.release(ch);
		}
		poolManager.stop();
	}

	/**
	 * 异步调用测试
	 * @throws Exception 
	 */
	@Test
	public void asyncRequestTest() throws Exception {
		NettyChannelPoolManager poolManager = new NettyChannelPoolManager();
		poolManager.start();
		InetSocketAddress serverAddr = InetSocketAddress.createUnresolved(host, port);
		NettyChannelPool pool = poolManager.getPool(serverAddr);
		if (pool == null) {
			CommonChannelInitializer initializer = new CommonChannelInitializer(new ClientChannelHandler());
			initializer.setHeartbeatHandlerFactory(new CommonClientHeartbeatHandlerFactory());//长连接，客户端心跳
			pool = poolManager.getPool(serverAddr, initializer);
		}
		Channel ch = pool.acquire(1000);
		try {
			IoRequest req = new IoRequest();
			req.setRequestTimeout(1000);
			req.setData(data);
			CallUtils.call(ch, req, new AsyncCallback(req)); //异步回调
		} finally {
			pool.release(ch);
		}
		Thread.sleep(1000); //block主线程
		poolManager.stop();
	}

}
