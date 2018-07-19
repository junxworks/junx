/*
 ***************************************************************************************
 * 
 * @Title:  AbstractCacheAdapter.java   
 * @Package io.github.junxworks.junx.cache.adapter   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-18 16:48:04   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.adapter;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.KV;
import io.github.junxworks.junx.core.util.StringUtils;

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

	public static final String CHARSET = "UTF-8";

	/**
	 * 获得组合过后的key值，如果子类cache本身不支持group的话，可以通过这种方式让key分组
	 * 子类可以使用，也可以不使用
	 *
	 * @param kv
	 *            the kv
	 * @return 组合过后的Key属性
	 */
	protected String getComposedKey(KV kv) {
		return getKeyPrefix(kv) + kv.getKey();
	}

	/**
	 * 获得组合过后的key byte数组
	 *
	 * @param kv the kv
	 * @return composed key 属性
	 */
	protected byte[] getComposedKeyBytes(KV kv) {
		return getComposedKey(kv).getBytes(Charset.forName(CHARSET));
	}

	/**
	 * 获得组合过后的key byte数组
	 *
	 * @param key the key
	 * @return composed key 属性
	 */
	protected byte[] getComposedKeyBytes(String key) {
		return key.getBytes(Charset.forName(CHARSET));
	}

	/**
	 * 根据group获取key的前缀
	 *
	 * @return key prefix 属性
	 */
	protected String getKeyPrefix(KV kv) {
		String groupName = kv.getGroup();
		if (StringUtils.notNull(groupName)) {
			String separator = kv.getSeparator();
			return groupName + (StringUtils.notNull(separator) ? separator : "");
		}
		return "";
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
				long time = (expireTime - System.currentTimeMillis()) / 1000;
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
