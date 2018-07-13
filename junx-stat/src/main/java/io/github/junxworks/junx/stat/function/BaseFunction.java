/*
 ***************************************************************************************
 * 
 * @Title:  BaseFunction.java   
 * @Package io.github.junxworks.junx.stat.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计函数的基类
 *
 * @author: michael
 * @date: 2017-5-31 18:48:51
 * @since: v1.0
 */
public abstract class BaseFunction implements Function {

	/** 常量 默认计算时候的小数精度. */
	protected static final int DEFAULT_SCALE = 10;

	/** logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public abstract Object getValue(Collection<?> data, StatContext context) throws Exception;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(Class<T> clazz, Collection<?> data, StatContext context) throws Exception {
		Object res = this.getValue(data, context);
		return res == null ? null : (T) res;
	}

}
