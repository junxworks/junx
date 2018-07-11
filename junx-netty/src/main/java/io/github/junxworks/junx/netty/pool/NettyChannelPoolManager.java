/*
 ***************************************************************************************
 * 
 * @Title:  NettyChannelPoolManager.java   
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

import java.net.SocketAddress;
import java.util.Map;

import com.google.common.collect.Maps;

import io.github.junxworks.junx.core.executor.StandardThreadExecutor;
import io.github.junxworks.junx.core.lifecycle.Service;
import io.github.junxworks.junx.core.util.SystemUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool.AcquireTimeoutAction;

/**
 * 管理多个连接池，线程安全。
 * {@code  
 * NettyChannelPoolManager poolManager = new NettyChannelPoolManager();
 * poolManager.setGlobalEventLoopGroup(globalEventLoopGroup);//如果已经有eventloopgroup了，最好用现有的，没必要重新起，浪费cpu
 * poolManager.setMaxConnectionsPerPool(xxxx);//设置一下每个pool的最大连接数，每个链接维持需要消耗资源，如果不是同步等待服务器应答，没必要启动太多，默认是processor数*2
 * poolManager.start();
 * }
 *
 *
 * @ClassName:  NettyChannelPoolManager
 * @author: Michael
 * @date:   2018-5-29 19:49:06
 * @since:  v1.0
 */
public class NettyChannelPoolManager extends Service {

	/** 连接池map. */
	private Map<SocketAddress, NettyChannelPool> pools = Maps.newHashMap();

	/** 全局的event loop group. */
	private EventLoopGroup globalEventLoopGroup;

	/** 全局的event loop group是否应该在manager停止的时候shutdown. */
	private boolean eventLoopGroupShouldShutdown = true;

	/** 每个连接池默认的连接池最大连接数，默认值core_processor*2. */
	private int maxConnectionsPerPool = SystemUtils.SYS_PROCESSORS * 2;

	/** 连接池创建连接的超时时间. */
	private int connTimeout = 2000;

	public EventLoopGroup getGlobalEventLoopGroup() {
		return globalEventLoopGroup;
	}

	public void setGlobalEventLoopGroup(EventLoopGroup globalEventLoopGroup) {
		this.globalEventLoopGroup = globalEventLoopGroup;
	}

	public int getMaxConnectionsPerPool() {
		return maxConnectionsPerPool;
	}

	public void setMaxConnectionsPerPool(int maxConnectionsPerPool) {
		this.maxConnectionsPerPool = maxConnectionsPerPool;
	}

	public int getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}

	/**
	 *直接从map中获取pool，如果没有，直接抛出异常
	 *
	 * @param addr the addr
	 * @return pool 属性
	 * @throws Exception the exception
	 */
	public NettyChannelPool getPool(SocketAddress addr) throws Exception {
		NettyChannelPool pool = pools.get(addr);
		if (pool != null)
			return pool;
		throw new NoSuchPoolException(addr.toString());
	}

	public NettyChannelPool getPool(SocketAddress addr, ChannelInitializer<Channel> channelInitializer) throws Exception {
		return getPool(addr, channelInitializer, maxConnectionsPerPool);
	}

	public NettyChannelPool getPool(SocketAddress addr, ChannelInitializer<Channel> channelInitializer, int maxConnections) throws Exception {
		return getPool(addr, new DefaultPoolHandler(channelInitializer), maxConnections);
	}

	public NettyChannelPool getPool(SocketAddress addr, ChannelPoolHandler handler) throws Exception {
		return getPool(addr, handler, maxConnectionsPerPool);
	}

	public NettyChannelPool getPool(SocketAddress addr, ChannelPoolHandler handler, int maxConnections) throws Exception {
		return getPool(addr, globalEventLoopGroup, handler, ChannelHealthChecker.ACTIVE, null, -1, maxConnections, Integer.MAX_VALUE, true, false);
	}

	public NettyChannelPool getPool(SocketAddress addr, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck, boolean lastRecentUsed) throws Exception {
		return getPool(addr, globalEventLoopGroup, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, releaseHealthCheck, lastRecentUsed);
	}

	/**
	 * 根据SocketAddr返回pool，如果没有初始化pool，则会根据传入的.ChannelPoolHandler初始化连接池
	 *
	 * @param addr the addr
	 * @param workGroup the work group
	 * @param handler the handler
	 * @param healthCheck the health check
	 * @param action the action
	 * @param acquireTimeoutMillis the acquire timeout millis
	 * @param maxConnections the max connections
	 * @param maxPendingAcquires the max pending acquires
	 * @param releaseHealthCheck the release health check
	 * @param lastRecentUsed the last recent used
	 * @return pool 属性
	 * @throws Exception the exception
	 */
	public NettyChannelPool getPool(SocketAddress addr, EventLoopGroup workGroup, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck, boolean lastRecentUsed) throws Exception {
		NettyChannelPool pool = pools.get(addr);
		if (pool == null) {
			synchronized (NettyChannelPoolManager.class) {
				pool = pools.get(addr);
				if (pool == null) {
					pool = new NettyChannelPool(addr, null);
					pool.setAcquireTimeoutAction(action);
					pool.setAcquireTimeoutMillis(acquireTimeoutMillis);
					pool.setConnTimeout(connTimeout);
					pool.setMaxConnect(maxConnections);
					pool.setEventLoopGroup(workGroup);
					pool.setHealthCheck(healthCheck);
					pool.setLastRecentUsed(lastRecentUsed);
					pool.setMaxPendingAcquires(maxPendingAcquires);
					pool.setPoolHandler(handler);
					pool.setReleaseHealthCheck(releaseHealthCheck);
					pool.start();
					pools.put(addr, pool);
				}
			}
		}
		return pool;
	}

	@Override
	protected void doStart() throws Throwable {
		if (globalEventLoopGroup != null) {
			eventLoopGroupShouldShutdown = false;
		} else {
			StandardThreadExecutor workerExecutor = new StandardThreadExecutor();
			workerExecutor.setName("Netty-PoolWorker Excutor");
			workerExecutor.setNamePrefix("Netty-PoolWorker-");
			workerExecutor.start();
			if (SystemUtils.LINUX_SYSTEM) {
				globalEventLoopGroup = new EpollEventLoopGroup(workerExecutor.getMaxThreads(), workerExecutor);
			} else {
				globalEventLoopGroup = new NioEventLoopGroup(workerExecutor.getMaxThreads(), workerExecutor);
			}
		}
	}

	@Override
	protected void doStop() throws Throwable {
		if (eventLoopGroupShouldShutdown) {
			globalEventLoopGroup.shutdownGracefully();
		}
		globalEventLoopGroup = null;
		pools.values().forEach(pool -> {
			try {
				pool.stop();
			} catch (Throwable e) {
			}
		});
	}

}
