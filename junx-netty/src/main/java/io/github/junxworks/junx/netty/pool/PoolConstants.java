package io.github.junxworks.junx.netty.pool;

import io.netty.channel.pool.FixedChannelPool.AcquireTimeoutAction;

public class PoolConstants {

	/** 默认最大连接数. */
	public static final int DEFAULT_MAX_CONNECTIONS = 100;

	/** 默认连接超时时间millis. */
	public static final int DEFAULT_CONNECT_TIMEOUT = 3000;

	/** 连接池默认最大等待时间millis */
	public static final int DEFAULT_MAX_ACQUIRE_TIMEOUT = 10000;

	/** 连接池默认最大等待请求连接数 */
	public static final int DEFAULT_MAX_PENDING_ACQUIRES = 100000;

	/** 连接池默认等待超时处理，直接异常 */
	public static final AcquireTimeoutAction DEFAULT_ACQUIRE_TIMEOUT_ACTION = AcquireTimeoutAction.FAIL;
}
