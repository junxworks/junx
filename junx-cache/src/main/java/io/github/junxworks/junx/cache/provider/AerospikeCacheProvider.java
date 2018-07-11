/*
 ***************************************************************************************
 * 
 * @Title:  AerospikeCacheProvider.java   
 * @Package io.github.junxworks.junx.cache.provider   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:38:51   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.provider;


import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.adapter.AerospikeCacheAdapter;
import io.github.junxworks.junx.core.exception.NullParameterException;
import io.github.junxworks.junx.core.util.StringUtils;

/**
 * Aerospike缓存提供者，启动时创建Aerospike客户端，创建AerospikeAdapter提供给用户进行
 * aerospike的增删改查操作
 * 	 
 * @ClassName:  AerospikeCacheProvider
 * @author: 司福林
 * @date:   2017-7-25 14:59:24
 * @since:  v1.0
 */
public class AerospikeCacheProvider extends AbstractCacheProvider {

	/** 默认socket连接超时时间 */
	private static final int DEFAULT_SOCKET_TIMEOUT = 500;
	/** aerospike默认端口*/
	private static final int DEFAULT_PORT = 3000; 
	
	/** aerospike默认的namespace(库名) */
	private static final String DEFAULT_NAMESPACE = "default";
	
	/** 用户名 */
	private static final String AS_CONFIG_USERNAME = "user";
	
	/** 密码,无论是单机还是集群只有一对用户名密码 */
	private static final String AS_CONFIG_PASSWORD = "password";
	
	/** 集群名称*/
	private static final String AS_CONFIG_CLUSNAME = "clusterName"; 
	
	/** 第一次连接服务端超时时间(单位:ms) */
	private static final String AS_CONFIG_TIMEOUT = "timeout";
	
	/** 节点最大连接数 */
	private static final String AS_CONFIG_MAXCONNS_PERNODE = "maxConnsPerNode";
	
	/** 每个节点连接池数 */
	private static final String AS_CONFIG_CONNPOOLS_PERNODE = "connPoolsPerNode";
	
	/** 最大空闲时间 (单位:s) default 55s 小于服务器端设置的时间(default 60000 milliseconds or 1 minute) */
	private static final String AS_CONFIG_MAXSOCKETIDLE = "maxSocketIdle";
	
	/** 集群节点之间维持心跳间隔(单位:ms) default 1000 */
	private static final String AS_CONFIG_INTERVAL = "tendInterval";
	
	/** 每次请求连接超时时间限制(单位:ms) default 1000*/
	private static final String AS_CONFIG_SOCKTIMOUT = "socketTimeout";
	
	/** 批量读取时开启最大线程数 default 1*/
	private static final String AS_CONFIG_MAXCURTHREADS = "maxConcurrentThreads";
	
	/** 默认端口号3000，集群公用 */
	private static final String AS_CONFIG_PORT = "port";
	
	/** 地址，多个用,隔开，如：192.168.10.188:3000,192.168.10.189:3000 */
	private static final String AS_CONFIG_HOST = "host";	
	
	/** 数据库名 */
	private static final String AS_CONFIG_NAMESPACE = "namespace";
	
	
	private AerospikeClient client;
	
	
	/**
	 * @see io.github.junxworks.junx.cache.provider.AbstractCacheProvider#getCache()
	 */
	@Override
	public Cache getCache() throws Exception {
		
		AerospikeCacheAdapter aerospikeAdapter = new AerospikeCacheAdapter();
		aerospikeAdapter.setClient(client);
		aerospikeAdapter.setNamespace(cacheEnv.getString(AS_CONFIG_NAMESPACE, DEFAULT_NAMESPACE));
		return aerospikeAdapter;
	}

	
	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@Override
	protected void doStart() throws Throwable {
		ClientPolicy policy = new ClientPolicy();
		policy.user = cacheEnv.getString(AS_CONFIG_USERNAME, null);
		policy.password = cacheEnv.getString(AS_CONFIG_PASSWORD, null);	
		policy.clusterName = cacheEnv.getString(AS_CONFIG_CLUSNAME, null);	
		policy.timeout = cacheEnv.getInteger(AS_CONFIG_TIMEOUT, policy.timeout);
		policy.maxConnsPerNode = cacheEnv.getInteger(AS_CONFIG_MAXCONNS_PERNODE, policy.maxConnsPerNode);
		policy.connPoolsPerNode = cacheEnv.getInteger(AS_CONFIG_CONNPOOLS_PERNODE, policy.connPoolsPerNode);
		policy.maxSocketIdle = cacheEnv.getInteger(AS_CONFIG_MAXSOCKETIDLE, policy.maxSocketIdle);
		policy.tendInterval = cacheEnv.getInteger(AS_CONFIG_INTERVAL, policy.tendInterval);
		int sockTimout =  cacheEnv.getInteger(AS_CONFIG_SOCKTIMOUT, DEFAULT_SOCKET_TIMEOUT);		
		policy.writePolicyDefault.socketTimeout = sockTimout;
		policy.writePolicyDefault.sleepBetweenRetries=500;
		policy.readPolicyDefault.socketTimeout = sockTimout;
		policy.scanPolicyDefault.socketTimeout = sockTimout;
		policy.batchPolicyDefault.socketTimeout = sockTimout;
		policy.infoPolicyDefault.timeout = sockTimout;
		policy.queryPolicyDefault.socketTimeout = sockTimout;
		policy.batchPolicyDefault.maxConcurrentThreads = cacheEnv.getInteger(AS_CONFIG_MAXCURTHREADS, 1);
		int port = cacheEnv.getInteger(AS_CONFIG_PORT, DEFAULT_PORT);
		String host = cacheEnv.getString(AS_CONFIG_HOST, null);
		if(StringUtils.isNull(host)){
			throw new NullParameterException("configer param \"host\" can not be null for Aerospike id =\"%s\"",cacheEnv.getCacheId());
		}
		Host[] hosts = new HostParse().converHosts(host, port);
		client = new AerospikeClient(policy, hosts);
	}

	
	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {
		if(client != null)
			client.close();

	}

}
