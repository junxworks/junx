/*
 ***************************************************************************************
 * 
 * @Title:  StatContext.java   
 * @Package io.github.junxworks.junx.stat   
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
package io.github.junxworks.junx.stat;

/**
 * 统计上下文，用于统计函数计算统计值、数据窗口数据抽取
 *
 * @ClassName:  StatContext
 * @author: Michael
 * @date:   2018-5-18 15:29:28
 * @since:  v1.0
 */
public class StatContext {
	/** 事件时间戳. */
	private long timestamp;

	/** 统计目标值 */
	private Object value;

	/** 统计函数附加表达式，区间分布这样需要额外的统计定义的函数会用到. */
	private String functionAddition;

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
