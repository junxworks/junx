/*
 ***************************************************************************************
 * 
 * @Title:  CacheEnv.java   
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

import java.util.Map;

import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 缓存的配置，每个缓存一个CecheEnv对象，全局可以存在多个CacheEnv对象.
 * 此类为解析过后的cache配置类，每个cache对应一个和CacheEnv。
 * 
 * @ClassName: CacheEnv
 * @author: Michael
 * @date: 2017-7-24 15:55:16
 * @since: v1.0
 */
public class CacheEnv {

	/** 缓存ID. */
	private String cacheId;

	/** 缓存配置前缀. */
	private String prefix;

	/** 参数map. */
	private Map<String, Object> attributs;

	public CacheEnv(String prefix, String id) {
		this.prefix = prefix;
		this.cacheId = id;
	}

	public CacheEnv(String prefix, String id, Map<String, Object> attributs) {
		this.prefix = prefix;
		this.cacheId = id;
		this.attributs = attributs;
	}

	public String getCacheId() {
		return cacheId;
	}

	public String getPrefix() {
		return prefix;
	}

	public Map<String, Object> getAttributs() {
		return attributs;
	}

	/**
	 * 返回 string 属性.
	 *
	 * @param key
	 *            the key
	 * @return string 属性
	 */
	public String getString(String key, String defaultValue) {
		String v = String.valueOf(attributs.get(key));
		return StringUtils.isNull(v) ? defaultValue : v;
	}

	/**
	 * 返回 intger 属性.
	 *
	 * @param key
	 *            the key
	 * @return intger 属性
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String s = getString(key, null);
		return s == null ? defaultValue : Integer.valueOf(s);
	}

	/**
	 * 返回 double 属性.
	 *
	 * @param key
	 *            the key
	 * @return double 属性
	 */
	public Double getDouble(String key, Double defaultValue) {
		String s = getString(key, null);
		return s == null ? defaultValue : Double.valueOf(s);
	}

	/**
	 * 返回 boolean 属性.
	 *
	 * @param key
	 *            the key
	 * @return boolean 属性
	 */
	public Boolean getBoolean(String key, Boolean defaultValue) {
		String s = getString(key, null);
		return s == null ? defaultValue : Boolean.valueOf(s);
	}
}
