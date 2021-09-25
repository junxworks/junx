/*
 ***************************************************************************************
 * 
 * @Title:  NettyServer.java   
 * @Package io.github.junxworks.junx.netty   
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
package io.github.junxworks.junx.netty;

import io.github.junxworks.junx.core.lifecycle.ThreadService;
import io.github.junxworks.junx.core.util.SystemUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty服务器
 *
 * @ClassName:  NettyServer
 * @author: Michael
 * @date:   2017-11-5 13:22:20
 * @since:  v1.0
 */
public class NettyServer extends ThreadService {

	/** 服务器配置. */
	private ServerConfig config;

	/** 通道初始化器. */
	private ChannelInitializer<Channel> channelInitializer;

	/** acceptor线程. */
	private EventLoopGroup bossGroup;

	/** worker线程. */
	private EventLoopGroup workGroup;

	/** 启动引导类. */
	private ServerBootstrap bootstrap;

	/** socket class. */
	private Class<ServerSocketChannel> socketClass = null;

	/**
	 * 构造一个新的 netty server 对象.
	 *
	 * @param config the config
	 * @param channelInitializer the channel initializer
	 */
	public NettyServer(ServerConfig config, ChannelInitializer<Channel> channelInitializer) {
		this.config = config;
		this.channelInitializer = channelInitializer;
	}

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	public EventLoopGroup getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(EventLoopGroup workGroup) {
		this.workGroup = workGroup;
	}

	public ChannelInitializer<Channel> getChannelInitializer() {
		return channelInitializer;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onStart() throws Throwable {
		bootstrap = new ServerBootstrap();
		int workerCount = config.getWorkerCount();
		if (workerCount == 0) {
			workerCount = SystemUtils.SYS_PROCESSORS * 2;
		}
		if (SystemUtils.LINUX_SYSTEM) {
			bossGroup = new EpollEventLoopGroup(1);
			workGroup = new EpollEventLoopGroup(workerCount);
			socketClass = (Class<ServerSocketChannel>) Class.forName(EpollServerSocketChannel.class.getCanonicalName());
		} else {
			bossGroup = new NioEventLoopGroup(1);
			workGroup = new NioEventLoopGroup(workerCount);
			socketClass = (Class<ServerSocketChannel>) Class.forName(NioServerSocketChannel.class.getCanonicalName());
		}
	}

	@Override
	protected void onStop() throws Throwable {
	}

	@Override
	protected void onRun() throws Throwable {
		try {
			bootstrap = bootstrap.group(bossGroup, workGroup);
			if (config.isOpenNettyLog()) {
				bootstrap.handler(new LoggingHandler(LogLevel.INFO));
			}
			bootstrap.channel(socketClass).childHandler(channelInitializer);
			
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnTimeoutMillis());
			bootstrap.option(ChannelOption.SO_BACKLOG, config.getBacklogSize());
			bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

			bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, config.isTcpKeepAlive());
			bootstrap.childOption(ChannelOption.SO_RCVBUF, config.getRcvBufSize());
			bootstrap.childOption(ChannelOption.SO_SNDBUF, config.getSendBufSize());
			bootstrap.childOption(ChannelOption.TCP_NODELAY, config.isTcpNoDelay());

			logger.info("Server is started,listen on port " + config.getPort() + ".");
			ChannelFuture future = bootstrap.bind(config.getIp(), config.getPort()).sync();
			ChannelFuture close = future.channel().closeFuture();
			close.sync();
			logger.info("Server is stopped.");
		} catch (InterruptedException ignore) {
		} catch (Exception e) {
			logger.error("Exception occurred when start/shutdown Netty Server.", e);
			stop();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
