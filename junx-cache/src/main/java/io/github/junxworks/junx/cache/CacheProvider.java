/*
 ***************************************************************************************
 * 
 * @Title:  CacheProvider.java   
 * @Package io.github.junxworks.junx.cache   
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
package io.github.junxworks.junx.cache;

import io.github.junxworks.junx.core.lifecycle.Lifecycle;

/**
 * 缓存对象提供者，不管缓存技术采用何种方式实现，客户端只需通过此接口即可获得cache对象。
 * 目前提供EHCache、Redis、Aerospike的CacheProvider实现。<br/>
 * CacheProvider通过CacheManager来获得，代码如下：<br/>
 * {@code 
 * 	CacheProvicer provider=CacheManager.getProvider("test1"); //test1建议定义到常量类中
 *  Cache cache=protiver.getCache(); //进一步获取Cache对象
 * }
 *
 * @ClassName: CacheProvider
 * @author: Michael
 * @date: 2017-7-24 15:54:14
 * @since: v1.0
 */
public interface CacheProvider extends Lifecycle {

	/**
	 * 返回 provider对应的env配置
	 *
	 * @return env 属性
	 */
	public CacheEnv getEnv();

	/**
	 * 设置 provider对应的env配置
	 *
	 * @param env the env
	 */
	public void setEnv(CacheEnv env);

	/**
	 * 获得provider本身对应的cache对象。
	 *
	 * @return cache 属性
	 * @throws Exception the exception
	 */
	public Cache getCache() throws Exception;
}
