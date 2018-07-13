/*
 ***************************************************************************************
 * 
 * @Title:  DataBundle.java   
 * @Package io.github.junxworks.junx.stat.datawindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:28   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow;

/**
 * 用于在window中传输数据的对象，用于数据窗口的数据累计。 本对象目前包含了事件的发生时间，统计目标的值。
 * 定义了数据窗口累计所需要的关键属性，例如时间窗口，需要timestamp和value，如果是其他数据窗口，可以定义其他必传参数。
 *
 * @author: Michael
 * @date: 2017-5-18 18:11:52
 * @since: v1.0
 */
public class DataBundle {

	/** 事件时间戳. */
	private long timestamp;

	/** 统计目标值 */
	private Object value;

	/** 统计函数附加表达式，区间分布这样需要额外的统计定义的函数会用到. */
	private String functionAddition;

	public DataBundle() {

	}

	/**
	 * 构造一个新的对象.
	 *
	 * @param timestamp the timestamp
	 * @param value the value
	 */
	public DataBundle(long timestamp, Object value) {
		this.timestamp = timestamp;
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFunctionAddition() {
		return functionAddition;
	}

	public void setFunctionAddition(String functionAddition) {
		this.functionAddition = functionAddition;
	}

}
