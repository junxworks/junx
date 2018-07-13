/*
 ***************************************************************************************
 * 
 * @Title:  DataWindow.java   
 * @Package io.github.junxworks.junx.stat.datawindow   
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
package io.github.junxworks.junx.stat.datawindow;

import java.util.Collection;

import io.github.junxworks.junx.stat.Serializable;
import io.github.junxworks.junx.stat.StatContext;

/**
 * 数据窗口，这是顶层定义，定义了外部接口，数据窗口到底提供什么样的功能，都在此接口中定义。
 * 数据窗口，主要是提供基于窗口实现的数据集合过滤功能，基于某种条件，在某个数据集合中过滤出另一个数据集合。
 * 数据窗口主要负责管理需要进行函数计算的数据，从各种维度去累计函数锁需要的数据，因此数据窗口可能会跟函数关联，但是函数不会跟数据窗口关联。
 * 数据窗口的实现，可以是基于长度、时间、长度时间组合等条件实现。<br/>
 * 目前主要实现基于时间的数据窗口，基于时间窗口的数据窗口实现定义，请参考类{@link }
 * 
 * @param <T> the generic type
 * @author: Michael
 * @date:   2017-5-15 14:07:46
 * @since:  v1.0
 */
public interface DataWindow extends Serializable{

	/**
	 * 返回当前数据集合
	 *
	 * @return data 属性
	 */
	public Collection<?> getData();

	/**
	 * 往数据窗口中累计对象.
	 *
	 * @param newData the new data
	 * @throws Exception the exception
	 */
	public void compose(DataBundle... newData) throws Exception;

	public void setData(Collection<?> data);

	/**
	 * 抽取数据
	 *
	 * @param params 抽取时候用到的参数，每个数据窗口定义不一样，参数个数和类型由数据窗口自己定义和验证
	 * @return 返回数据窗口内，根据参数过滤后的数据
	 * @throws Exception the exception
	 */
	public Collection<?> extractData(StatContext context) throws Exception;

	/**
	 * 清空数据
	 */
	public void clear();

	/**
	 * 返回数据窗口的持久化类型
	 * 取数据和存储数据的时候会用到此类型
	 * @return the int
	 */
	public String storageType();

}
