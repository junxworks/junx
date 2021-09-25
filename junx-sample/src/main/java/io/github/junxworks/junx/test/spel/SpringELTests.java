/*
 ***************************************************************************************
 * 
 * @Title:  SpringELTests.java   
 * @Package io.github.junxworks.junx.test.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:14   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel;


import java.util.Map;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Maps;

public class SpringELTests {

	ExpressionParser parser = new SpelExpressionParser();

	@Test
	public void stringTest() {
		log(parser.parseExpression("\"String test\"").getValue());
		log(parser.parseExpression("'String test'").getValue());
		log(parser.parseExpression("T(String).valueOf(1)").getValue());

	}

	@Test
	public void collectionTest() {
		log(parser.parseExpression("{1, 3, 5, 7}.?[#this > 3]").getValue()); // [5, 7] ,
		log(parser.parseExpression("{1, 3, 5, 7}.^[#this > 3]").getValue()); // 5 , 第一个
		log(parser.parseExpression("{1, 3, 5, 7}.$[#this > 3]").getValue()); // 7 , 最后一个
		log(parser.parseExpression("{1, 3, 5, 7}.![#this + 1]").getValue()); // [2, 4,
																				// 6,8],每个元素都加1
	}

	@Test
	public void mapTest() {
		Map<Integer, String> map = Maps.newHashMap();
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "D");
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("map", map);
		log(parser.parseExpression("#map.?[key > 3]").getValue(context)); // {4=D}
		log(parser.parseExpression("#map.?[value == 'A']").getValue(context)); // {1=A}
		log(parser.parseExpression("#map.?[key > 2 and key < 4]").getValue(context)); // {3=C}
	}

	public static void log(Object obj) {
		System.out.println(obj.toString());
	}

}
