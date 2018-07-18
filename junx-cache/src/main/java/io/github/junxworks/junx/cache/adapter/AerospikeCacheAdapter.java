/*
 ***************************************************************************************
 * 
 * @Title:  AerospikeCacheAdapter.java   
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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.WritePolicy;

import io.github.junxworks.junx.cache.KV;
import io.github.junxworks.junx.core.exception.NullParameterException;

/**
 * Aerospike适配器，可对aerospike数据库进行增删改查以及批量增删改查操作。<p/>
 * @ClassName:  AerospikeCacheAdapter
 * @author: Michael
 * @date:   2017-7-26 11:37:29
 * @since:  v1.0
 */
public class AerospikeCacheAdapter extends AbstractCacheAdapter {

	/** 每个服务对象有自己的日志打印。 */
	protected final Logger logger = LoggerFactory.getLogger(AerospikeCacheAdapter.class);

	/** 内存数据库列的名字 */
	private static final String BIN_NAME = "";

	/** aerospike 客户端 */
	private AerospikeClient client;

	/** aerospike 数据库名字 */
	private String namespace;

	public AerospikeClient getClient() {
		return client;
	}

	public void setClient(AerospikeClient client) {
		this.client = client;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/** 
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#get(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public KV get(KV kv) {
		if (kv == null)
			throw new NullParameterException("param kv is musts be inited");

		Key key = new Key(namespace, kv.getGroup(), kv.getKey());
		Record record = client.get(null, key);
		Object val = null;
		if (record != null)
			val = record.getValue(BIN_NAME);
		kv.setValue(val);
		return kv;
	}

	/** 
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#get(java.util.List)
	 */
	@Override
	public List<KV> get(List<KV> kvs) {
		if (kvs == null || kvs.size() == 0)
			throw new NullParameterException("param kvs is musts be inited");

		Key[] keys = new Key[kvs.size()];
		int i;
		KV kv = null;
		for (i = 0; i < kvs.size(); i++) {
			kv = kvs.get(i);
			keys[i] = new Key(namespace, kv.getGroup(), kv.getKey());
		}
		Record[] records = client.get(null, keys);
		for (i = 0; i < kvs.size(); i++) {
			kv = kvs.get(i);
			kv.setValue(records[i] == null ? null : records[i].getValue(BIN_NAME));
		}
		return kvs;
	}

	/** 
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#set(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void set(KV kv) {
		set(kv, getWritePolicy(kv));
	}

	private WritePolicy getWritePolicy(KV kv) {
		WritePolicy policy = null;
		long expireTime = getTTLSeconds(kv);
		if (expireTime > 0) {
			policy = new WritePolicy();
			policy.expiration = (int) expireTime;
		}
		return policy;
	}

	/**
	 * 内部设置值
	 */
	private void set(KV kv, WritePolicy policy) {
		if (kv == null)
			throw new NullParameterException("Parameter kv can not be null.");
		byte[] value = kv.getValue();
		if (value == null) {
			log.warn("Value of KV[group is \"{}\",key is \"{}\"] which will be cached can not bt null.This KV object will be ignored.", kv.getGroup(), kv.getKey());
			return;
		}
		Key key = new Key(namespace, kv.getGroup(), kv.getKey());
		Bin bin = new Bin(BIN_NAME, value);
		client.put(policy, key, bin);
	}

	/**
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#set(java.util.List)
	 */
	@Override
	public void set(List<KV> kvs) {
		if (kvs == null || kvs.size() == 0)
			throw new NullParameterException("param kvs is musts be inited");
		for (int i = 0; i < kvs.size(); i++) {
			try {
				set(kvs.get(i));
			} catch (RuntimeException e) {
				logger.error("aerospike delete the key {} occured exception ", kvs.get(i).getKey(), e);
			}
		}

	}

	/**
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#delete(io.github.junxworks.junx.cache.KV)
	 */
	@Override
	public void delete(KV kv) {
		if (kv == null)
			throw new NullParameterException("param kv is musts be inited");
		Key key = new Key(namespace, kv.getGroup(), kv.getKey());
		client.delete(null, key);
	}

	/**
	 * @see io.github.junxworks.junx.cache.adapter.AbstractCacheAdapter#delete(java.util.List)
	 */
	@Override
	public void delete(List<KV> kvs) {
		if (kvs == null || kvs.size() == 0)
			throw new NullParameterException("param kvs is musts be inited");
		for (int i = 0; i < kvs.size(); i++) {
			try {
				delete(kvs.get(i));
			} catch (RuntimeException e) {
				logger.error("aerospike delete the key {} occured exception ", kvs.get(i).getKey(), e);
			}
		}
	}

	@Override
	public boolean exists(KV kv) {
		Key key = new Key(namespace, kv.getGroup(), kv.getKey());
		return client.exists(null, key);
	}

	@Override
	public Map<KV, Boolean> exists(List<KV> kvs) {
		Map<KV, Boolean> resMap = new HashMap<KV, Boolean>(kvs.size() * 2);
		int size = kvs.size();
		Key[] keys = new Key[size];
		KV kv;
		for (int i = 0; i < size; i++) {
			kv = kvs.get(i);
			keys[i] = new Key(namespace, kv.getGroup(), kv.getKey());
		}
		boolean[] res = client.exists(null, keys);
		for (int i = 0; i < size; i++) {
			kv = kvs.get(i);
			resMap.put(kv, res[i]);
		}
		return resMap;
	}

	@Override
	public List<KV> getGroupValues(String group) {
		final List<KV> kvs = new ArrayList<KV>();

		ScanCallback cllBack = new ScanCallback() {

			@Override
			public void scanCallback(Key key, Record record) throws AerospikeException {

				KV kv = new KV(group, "UnknownKey");
				kv.setValue((byte[]) record.getValue(BIN_NAME));
				kvs.add(kv);
			}

		};
		client.scanAll(null, namespace, group, cllBack, BIN_NAME);
		return kvs;
	}

	/**
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {

	}

}
