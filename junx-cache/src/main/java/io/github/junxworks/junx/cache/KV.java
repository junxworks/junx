/*
 ***************************************************************************************
 * 
 * @Title:  KV.java   
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

import com.google.common.base.Objects;

import io.github.junxworks.junx.cache.keyserializers.StringSerializer;
import io.github.junxworks.junx.core.exception.UnsupportedTypeException;

/**
 * 缓存存储的基础对象.
 *
 * @ClassName: KV
 * @author: Michael
 * @date: 2017-7-24 17:37:48
 * @since: v1.0
 */
public class KV {

	private static final KeySerializer stringSerializer = new StringSerializer();

	public static final String DEFAULT_SEPARATOR = "$";

	/** 值分组，可以用于向redis的key添加前缀，或者是向AS里面添加set. */
	private String group;

	/** 分隔符，用于组装key和group，例如redis，最后存入redis的key为group+separater+key，如果separater为空，则直接group+key . */
	private String separator = DEFAULT_SEPARATOR;

	/** 主键. */
	private String key;

	private KeySerializer keySerializer = stringSerializer;

	/** 值. */
	private byte[] value;

	/** 过期时间是否采用TTL（time to live）模式，如果是TTL的话，expireTimestamp表示存在的时长，单位是秒 */
	private boolean useTTL = true;

	/** 过期时间，值应该是一个时间撮，设置当前KV对象在缓存中的过期时间，支持对key设置过期时间的缓存有效. 
	 *  如果useTTL是true（默认），则单位是秒
	 *  如果useTTL是false，那么这个值代表KV对象过期的时间戳
	 */
	private long expireTimestamp;

	public KV() {
		setGroup(null);
		setKey(null);
	}

	/**
	 * 构造一个新的 kv 对象.
	 *
	 * @param key the key
	 */
	public KV(String key) {
		setGroup(null);
		setKey(key);
	}

	/**
	 * 构造一个新的 kv 对象.
	 *
	 * @param group the group
	 * @param key the key
	 * @param value the value
	 */
	public KV(String group, String key, Object value) {
		setGroup(group);
		setKey(key);
		setValue(value);
	}

	/**
	 * 构造一个新的 kv 对象.
	 *
	 * @param group the group
	 * @param key the key
	 */
	public KV(String group, String key) {
		setGroup(group);
		setKey(key);
	}

	public KeySerializer getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(KeySerializer keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public long getExpireTimestamp() {
		return expireTimestamp;
	}

	public void setExpireTimestamp(long expireTimestamp) {
		this.expireTimestamp = expireTimestamp;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getKey() {
		return key;
	}

	private void setKey(String key) {
		this.key = key;
	}

	public byte[] getValue() {
		return value;
	}

	/**
	 * 将value转换成String对象返回.
	 *
	 * @return string 属性
	 */
	public String getString() {
		if (value != null) {
			return new String(value);
		} else {
			return null;
		}
	}

	/**
	 * 设置 value 属性，目前只支持基础类型的封装和String类型，自定义复杂类型需要开发人员自己定义序列化，以byte数组的方式传入。
	 * 基础类型的封装类，都转换成String过后getBytes，不会每个类型单独转bytes，因此在转换的时候，要根据String转换成其他类型。
	 *
	 * @param value the value
	 */
	public void setValue(Object _value) {
		if (_value != null) {
			if (_value instanceof byte[]) {
				value = (byte[]) _value;
			} else if (_value instanceof String) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Integer) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Double) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Float) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Boolean) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Long) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Short) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Byte) {
				value = _value.toString().getBytes();
			} else if (_value instanceof Character) {
				value = _value.toString().getBytes();
			} else {
				throw new UnsupportedTypeException(_value.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && (obj instanceof KV)) {
			KV kv = (KV) obj;
			return _equals(group, kv.group) && _equals(key, kv.key) && _equals(separator, kv.separator);
		}
		return false;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @param target the target
	 * @return true, if successful
	 */
	private boolean _equals(Object obj, Object target) {
		if (obj != null && obj.equals(target)) {
			return true;
		} else if (obj == null && target == null) {
			return true;
		}
		return false;
	}

	public boolean isUseTTL() {
		return useTTL;
	}

	public void setUseTTL(boolean useTTL) {
		this.useTTL = useTTL;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(group, key, separator);
	}
}
