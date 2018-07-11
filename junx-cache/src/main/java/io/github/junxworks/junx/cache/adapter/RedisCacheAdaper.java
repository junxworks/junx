/*
 ***************************************************************************************
 * 
 * @Title:  RedisCacheAdaper.java   
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.junxworks.junx.cache.KV;
import io.github.junxworks.junx.core.exception.NullParameterException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * redis适配器，用于操作redis中存储的数据，包括：单个和批量的增删改查操作。<br/>
 * 
 * @ClassName: RedisCacheAdaper
 * @author: Michael
 * @date: 2017-7-26 16:31:15
 * @since: v1.0
 */
public class RedisCacheAdaper extends AbstractCacheAdapter {

	/** 常量 SEPARATOR. */
	private static final String SEPARATOR = "_";

	/** redis 客户端 */
	private Jedis jedis;

	public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * 获得组合过后的key值
	 *
	 * @param kv
	 *            the kv
	 * @return composed key 属性
	 */
	private String getComposedKey(KV kv) {
		return new StringBuilder(kv.getGroup()).append(SEPARATOR).append(kv.getKey()).toString();
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#get(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public KV get(KV kv) {
		if (kv == null)
			throw new NullParameterException("When the value is read in redis, the parameter cannot be null");
		kv.setValue(jedis.get(getComposedKey(kv).getBytes()));
		return kv;
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#get(java.util.List)
	 */
	@Override
	public List<KV> get(List<KV> kvs) {
		if (kvs == null)
			throw new NullParameterException("When reading values in batch redis, parameters cannot be null");
		if (!kvs.isEmpty()) {
			Pipeline p = jedis.pipelined();
			Map<String, Response<byte[]>> newMap = new HashMap<String, Response<byte[]>>();
			KV kv = null;
			for (int i = 0, len = kvs.size(); i < len; i++) {
				kv = kvs.get(i);
				newMap.put(kv.getKey(), p.get(getComposedKey(kv).getBytes()));
			}
			p.sync();
			for (int i = 0; i < kvs.size(); i++) {
				kvs.get(i).setValue(newMap.get(kvs.get(i).getKey()).get());
			}
		}
		return kvs;
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#set(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void set(KV kv) {
		if (kv == null)
			throw new NullParameterException("Parameter kv can not be null.");
		byte[] value = kv.getValue();
		if (value == null) {
			log.warn("Value of KV[group is \"{}\",key is \"{}\"] which will be cached can not bt null.This KV object will be ignored.", kv.getGroup(), kv.getKey());
			return;
		}
		byte[] key = getComposedKey(kv).getBytes();
		jedis.set(key, value);
		long expireTime = getTTLSeconds(kv);
		if (expireTime > 0) {
			jedis.expire(key, (int) expireTime);
		}
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#set(java.util.List)
	 */
	@Override
	public void set(List<KV> kvs) {
		if (kvs == null)
			throw new NullParameterException("When the values is added to redis, parameters cannot be null");
		if (!kvs.isEmpty()) {
			Pipeline p = jedis.pipelined();
			KV kv = null;
			for (int i = 0, len = kvs.size(); i < len; i++) {
				kv = kvs.get(i);
				byte[] value = kv.getValue();
				if (value == null) {
					log.warn("Value of KV[group is \"{}\",key is \"{}\"] which will be cached can not bt null.This KV object will be ignored.", kv.getGroup(), kv.getKey());
					continue;
				}
				byte[] key = getComposedKey(kv).getBytes();
				p.set(key, value);
				long expireTime = getTTLSeconds(kv);
				if (expireTime > 0) {
					p.expire(key, (int) expireTime);
				}
			}
			p.sync();
		}
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void delete(KV kv) {
		if (kv == null)
			throw new NullParameterException("When the redis delete value, parameter cannot be null");
		jedis.del(getComposedKey(kv).getBytes());
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(java.util.List)
	 */
	@Override
	public void delete(List<KV> kvs) {
		if (kvs == null)
			throw new NullParameterException("When redis batch delete value, parameters cannot be null");
		if (!kvs.isEmpty()) {
			Pipeline p = jedis.pipelined();
			for (int i = 0, len = kvs.size(); i < len; i++) {
				p.del(getComposedKey(kvs.get(i)).getBytes());
			}
			p.sync();
		}
	}

	@Override
	public boolean exists(KV kv) {
		return jedis.exists(getComposedKey(kv));
	}

	@Override
	public Map<KV, Boolean> exists(List<KV> kvs) {
		if (kvs == null)
			throw new NullParameterException("When reading values in batch redis, parameters cannot be null");
		Map<KV, Boolean> resMap = new HashMap<KV, Boolean>(kvs.size() * 2);
		if (!kvs.isEmpty()) {
			Pipeline p = jedis.pipelined();
			Map<String, Response<Boolean>> newMap = new HashMap<String, Response<Boolean>>();
			KV kv = null;
			for (int i = 0, len = kvs.size(); i < len; i++) {
				kv = kvs.get(i);
				newMap.put(kv.getKey(), p.exists(getComposedKey(kv)));
			}
			p.sync();
			for (int i = 0; i < kvs.size(); i++) {
				kv = kvs.get(i);
				resMap.put(kv, newMap.get(kv.getKey()).get());
			}
		}
		return resMap;
	}

	@Override
	public void deleteAll() {
		jedis.flushDB(); //仅清除当前选择的DB
	}

	@Override
	public void deleteAll(KV kv) {

		Set<String> set = jedis.keys(kv.getGroup() + this.SEPARATOR + "*");
		if (set == null || set.size() == 0)
			return;
		Iterator<String> keys = set.iterator();
		Pipeline p = jedis.pipelined();
		while (keys.hasNext()) {
			p.del(keys.next().getBytes());
		}
		p.sync();
	}

	@Override
	public List<KV> getAll(KV kv) {

		List<KV> kvs = new ArrayList<KV>();
		Set<String> set = jedis.keys(kv.getGroup() + SEPARATOR + "*");
		if (set == null || set.size() == 0)
			return kvs;
		String[] keys = (String[]) set.toArray();
		Pipeline p = jedis.pipelined();
		Map<String, Response<byte[]>> newMap = new HashMap<String, Response<byte[]>>();
		for (int i = 0; i < keys.length; i++) {
			newMap.put(keys[i], p.get(keys[i].getBytes()));
		}
		p.sync();
		KV v = null;

		for (int i = 0; i < keys.length; i++) {
			v = new KV(kv.getGroup(), keys[i], newMap.get(keys[i]).get());
			kvs.add(v);
		}
		return kvs;
	}

	/*
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (jedis != null) {
			jedis.close();
		}
	}

}
