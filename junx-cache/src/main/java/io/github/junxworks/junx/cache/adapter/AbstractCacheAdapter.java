/*
 ***************************************************************************************
 * 
 * @Title:  AbstractCacheAdapter.java   
 * @Package io.github.junxworks.junx.cache.adapter   
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
package io.github.junxworks.junx.cache.adapter;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.KV;

/**
 * 缓存适配器的基类，除了close方法，其他所有方法均有实现，目前仅仅是抛出UnsupportedOperationException异常.
 * 这些方法都是要各自缓存适配器自己去适配的方法。
 *
 * @ClassName:  AbstractCacheAdapter
 * @author: Michael
 * @date:   2017-7-26 10:20:28
 * @since:  v1.0
 */
public abstract class AbstractCacheAdapter implements Cache {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public List<KV> getAll(KV kv) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#get(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public KV get(KV kv) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#get(java.util.List)
	 */
	@Override
	public List<KV> get(List<KV> kvs) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#set(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void set(KV kv) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#set(java.util.List)
	 */
	@Override
	public void set(List<KV> kvs) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void delete(KV kv) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(java.util.List)
	 */
	@Override
	public void delete(List<KV> kvs) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#exists(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public boolean exists(KV kv) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#exists(java.util.List)
	 */
	@Override
	public Map<KV, Boolean> exists(List<KV> kvs) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 获取KV的过期时间TTL
	 *
	 * @param kv the kv
	 * @return TTL seconds 属性
	 */
	protected long getTTLSeconds(KV kv) {
		long expireTime = kv.getExpireTimestamp();
		if (expireTime > 0) {
			if (kv.isUseTTL()) {
				return expireTime;
			} else {
				long time = (expireTime - System.currentTimeMillis())/1000;
				if (time < 0) {
					return 1;//1秒
				} else {
					return time;
				}
			}
		}
		return -1;
	}
}
