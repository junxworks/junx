/*
 ***************************************************************************************
 * 
 * @Title:  Cache.java   
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

import java.io.Closeable;
import java.util.List;
import java.util.Map;

/**
 * 缓存接口定义，此处定义缓存接口的总体操作列表，支持单个{@link io.github.junxworks.junx.cache.KV}对象以及多个KV对象的操作。
 * 具体缓存适配器要适配此接口定义的操作，目前缓存适配器有EHCache本地缓存接口、Redis、Aerospike缓存接口适配器。
 * 开发人员在使用缓存的时候，不用关心缓存到底是采用何种适配器实现的，对本地代码没有影响。
 *
 * @ClassName: Cache
 * @author: Michael
 * @date: 2017-7-24 15:55:50
 * @since: v1.0
 */
public interface Cache extends Closeable {

	/**
	 * 获取key对应的值，以KV类型返回
	 *
	 * @param kv
	 *            KV对象
	 * @return the string
	 */
	public KV get(KV kv) throws Exception;

	/**
	 * 获取key对应的值，以KV类型返回
	 *
	 * @param kv
	 *            KV对象
	 * @return the string
	 */
	public List<KV> get(List<KV> kvs) throws Exception;
	
	/**
	 * 获取group下所有key对应的值，以KV类型返回
	 * 注意此方法以返回value为主，key可能会不准确，因为如果是Aerospike数据库的话，key是没有存储的。
	 * redis和ehcache目前可以取到key值
	 * 
	 * @param kv
	 *            KV对象
	 * @return the string
	 */
	public List<KV> getGroupValues(String groupName) throws Exception;

	/**
	 * 设置key对应的值，value会被序列化并压缩，因此建议缓存服务那边不要进行再次压缩存储
	 *
	 * @param kv
	 *            KV对象
	 */
	public void set(KV kv) throws Exception;

	/**
	 * 批量添加
	 *
	 * @param kvs
	 *            the kvs
	 */
	public void set(List<KV> kvs) throws Exception;

	/**
	 * 删除kv对应的数据.
	 *
	 * @param kv
	 *            the kv
	 */
	public void delete(KV kv) throws Exception;

	/**
	 * 删除kv列表中对应的数据.
	 *
	 * @param kvs
	 *            the kvs
	 */
	public void delete(List<KV> kvs) throws Exception;

	/**
	 * 查看对应KV是否存在.
	 *
	 * @param kv the kv
	 * @return true, if successful
	 */
	public boolean exists(KV kv) throws Exception;

	/**
	 * 查看对应KV列表中的KV是否存在，返回一个跟KV对应的map
	 *
	 * @param kvs the kvs
	 * @return the map
	 */
	public Map<KV, Boolean> exists(List<KV> kvs) throws Exception;
	

}
