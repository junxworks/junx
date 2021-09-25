/*
 ***************************************************************************************
 * 
 * @Title:  AbstractCacheProvider.java   
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

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.CacheEnv;
import io.github.junxworks.junx.cache.CacheProvider;
import io.github.junxworks.junx.core.lifecycle.Service;

/**
 * CacheProvider类的基类，提供一些基础操作，具体实现由各自缓存自己实现
 *
 * @ClassName: AbstractCacheProvider
 * @author: Michael
 * @date: 2017-7-25 14:04:05
 * @since: v1.0
 */
public abstract class AbstractCacheProvider extends Service implements CacheProvider {

	/** cache配置 */
	protected CacheEnv cacheEnv;

	/**
	 * @see io.github.junxworks.junx.cache.CacheProvider#getEnv()
	 */
	@Override
	public CacheEnv getEnv() {
		return cacheEnv;
	}

	/**
	 * @see io.github.junxworks.junx.cache.CacheProvider#setEnv(io.github.junxworks.junx.cache.CacheEnv)
	 */
	@Override
	public void setEnv(CacheEnv env) {
		cacheEnv = env;
	}

	/**
	 * @see io.github.junxworks.junx.cache.CacheProvider#getCache()
	 */
	@Override
	public abstract Cache getCache() throws Exception;

}
