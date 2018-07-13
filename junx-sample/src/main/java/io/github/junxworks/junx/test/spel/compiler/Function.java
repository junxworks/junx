/*
 ***************************************************************************************
 * 
 * @Title:  Function.java   
 * @Package io.github.junxworks.junx.test.spel.compiler   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel.compiler;

import java.math.BigDecimal;

import io.github.junxworks.junx.test.spel.EventContext;



/**
 * 
 * 函数测试
 * @author michael
 *
 */
public class Function {

	/**
	 * 两个整数相加
	 * @param a
	 * @param b
	 * @return
	 */
	public int plus(int a, int b) {
		return a + b;
	}

	/**
	 * 两个整数相加
	 * @param a
	 * @param b
	 * @return
	 */
	public Integer plus(Integer a, Integer b) {
		return a + b;
	}

	public Integer plusInteger(Integer a, Integer b) {
		return a + b;
	}

	/**
	 * 两个浮点数相加
	 * @param a
	 * @param b
	 * @return
	 */
	public float plus(float a, float b) {
		return a + b;
	}

	/**
	 * 两个浮点数相加
	 * @param a
	 * @param b
	 * @return
	 */
	public Float plus(Float a, Float b) {
		return a + b;
	}

	/**两个number类型相加，必须是number
	 * @param a
	 * @param b
	 * @return
	 */
	public Object plus(Object a, Object b) {
		BigDecimal x = new BigDecimal(a.toString());
		BigDecimal y = new BigDecimal(b.toString());
		return x.add(y);
	}

	/**
	 * 获取交易模型字段值
	 * @param fieldName
	 * @return
	 */
	public Object value(String fieldName) {
		return EventContext.currentContext().getValue(fieldName);
	}

	/**
	 * 获取统计值
	 * @param statId
	 * @return
	 */
	public Object stat(int statId) {
		return EventContext.currentContext().getStat(statId);
	}
}
