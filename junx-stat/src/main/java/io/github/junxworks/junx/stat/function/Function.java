/*
 ***************************************************************************************
 * 
 * @Title:  Function.java   
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

// TODO: Auto-generated Javadoc
/**
 * 统计函数接口.
 *
 * @author: michael
 * @date: 2017-5-31 13:41:56
 * @since: v1.0
 */
public interface Function {

	/**
	 * 根据数据集合中的数据，和给定参数值，获取计算结果
	 * 数据集合是由datawindow抽取过后的数据
	 * @param data 数据集合
	 * @param context 统计上下文，可以用来传入函数入参，如果函数不支持入参，可以在函数中忽略。部分函数需要入参，例如二项分布、区间分布等
	 * @return 计算过后的值
	 * @throws Exception the exception
	 */
	public Object getValue(Collection<?> data, StatContext context) throws Exception;

	/**
	 * 根据数据集合中的数据，和给定参数值，获取计算结果
	 *数据集合是由datawindow抽取过后的数据
	 * @param <T> the generic type
	 * @param clazz 指定返回类型
	 * @param data 数据集合
	 * @param ontext 统计上下文，可以用来传入函数入参，如果函数不支持入参，可以在函数中忽略。部分函数需要入参，例如二项分布、区间分布等
	 * @return 计算过后的值
	 * @throws Exception the exception
	 */
	public <T> T getValue(Class<T> clazz, Collection<?> data, StatContext context) throws Exception;

}
