/*
 ***************************************************************************************
 * 
 * @Title:  EHCacheCacheAdapter.java   
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;

import io.github.junxworks.junx.cache.KV;
import io.github.junxworks.junx.core.exception.NullParameterException;

/**
 * EHCache的适配器，本类封装了EHCache的cache对象。
 *
 * @ClassName:  EHCacheCacheAdapter
 * @author: Michael
 * @date:   2017-7-26 10:24:05
 * @since:  v1.0
 */
public class EHCacheCacheAdapter extends AbstractCacheAdapter {

	/** 分隔符，用于组装key和group . */
	private static final String SEPARATOR = "$";

	/** EHCache的Cache对象. */
	private Cache<String, byte[]> ehcache;

	public Cache<String, byte[]> getEhcache() {
		return ehcache;
	}

	public void setEhcache(Cache<String, byte[]> ehcache) {
		this.ehcache = ehcache;
	}

	/**
	 * 获得组合过后的key值
	 *
	 * @param kv the kv
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
		kv.setValue(ehcache.get(getComposedKey(kv)));
		return kv;
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#get(java.util.List)
	 */
	@Override
	public List<KV> get(List<KV> kvs) {
		if (kvs != null && !kvs.isEmpty()) {
			Set<String> keys = new HashSet<String>();
			for (int i = 0, len = kvs.size(); i < len; i++) {
				keys.add(getComposedKey(kvs.get(i)));
			}
			Map<String, byte[]> values = ehcache.getAll(keys);
			KV kv = null;
			for (int i = 0, len = kvs.size(); i < len; i++) {
				kv = kvs.get(i);
				kv.setValue(values.get(getComposedKey(kv)));
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
		ehcache.put(getComposedKey(kv), value);
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#set(java.util.List)
	 */
	@Override
	public void set(List<KV> kvs) {
		if (kvs != null && !kvs.isEmpty()) {
			Map<String, byte[]> entries = new HashMap<String, byte[]>();
			KV kv = null;
			for (int i = 0, len = kvs.size(); i < len; i++) {
				kv = kvs.get(i);
				byte[] value = kv.getValue();
				if (value == null) {
					log.warn("Value of KV[group is \"{}\",key is \"{}\"] which will be cached can not bt null.This KV object will be ignored.", kv.getGroup(), kv.getKey());
					continue;
				}
				entries.put(getComposedKey(kv), value);
			}
			ehcache.putAll(entries);
		}
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void delete(KV kv) {
		ehcache.remove(getComposedKey(kv));
	}

	/**
	 * @see io.github.junxworks.junx.cache.Cache#delete(java.util.List)
	 */
	@Override
	public void delete(List<KV> kvs) {
		if (kvs != null && !kvs.isEmpty()) {
			Set<String> keys = new HashSet<String>();
			for (int i = 0, len = kvs.size(); i < len; i++) {
				keys.add(getComposedKey(kvs.get(i)));
			}
			ehcache.removeAll(keys);
		}
	}

	@Override
	public boolean exists(KV kv) {
		return ehcache.containsKey(getComposedKey(kv));
	}

	@Override
	public Map<KV, Boolean> exists(List<KV> kvs) {
		Map<KV, Boolean> resMap = new HashMap<KV, Boolean>(kvs.size() * 2);
		KV kv;
		for (int i = 0, len = kvs.size(); i < len; i++) {
			kv = kvs.get(i);
			resMap.put(kv, ehcache.containsKey(getComposedKey(kv)));
		}
		return resMap;
	}

	public List<KV> getAll(KV kv){
		
		Iterator<Entry<String, byte[]>> entrys = ehcache.iterator();
		Entry<String, byte[]> entry;
		List<KV> kvs = new ArrayList<KV>();
		KV v;
		while(entrys.hasNext()){
			entry = entrys.next();
			v = new KV(kv.getGroup(),"all",entry.getValue());
			kvs.add(v);
			
		}
		return kvs;
	}
	/**
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		//do nothing
	}

}
