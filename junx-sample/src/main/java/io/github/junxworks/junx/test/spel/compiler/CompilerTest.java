/*
 ***************************************************************************************
 * 
 * @Title:  CompilerTest.java   
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

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Maps;

import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;
import io.github.junxworks.junx.core.util.ObjectUtils;
import io.github.junxworks.junx.spel.JCompiler;
import io.github.junxworks.junx.spel.Expression;
import io.github.junxworks.junx.test.spel.EventContext;

public class CompilerTest {
	private static final String EXP_ADD_INT = "plus(4,2)";

	private static final String EXP_CONTEXT = "plus(value('a'),value('b'))";

	private static final String EXP_CONTEXT2 = "1+'a123'";

	private static Function fun = new Function();

	public static int plus(Integer a, Integer b) {
		return a + b;
	}

	public static int plus(Integer a, Integer b, Integer c) {
		return a + b + c;
	}

	@Test
	public void compilerTest() throws Exception {
		EventContext c = new EventContext();
		c.putField("a", 1);
		c.putField("b", 2);
		EventContext.setContext(c);
		Expression exp = JCompiler.parse(EXP_CONTEXT);
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(fun));
	}

	@Test
	public void compilerTest2() throws Exception {
		Expression exp = JCompiler.parse(EXP_ADD_INT);
		StandardEvaluationContext context = new StandardEvaluationContext(fun);
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	@Test
	public void compilerTest3() throws Exception {
		Expression exp = JCompiler.parse(EXP_CONTEXT2);
		StandardEvaluationContext context = new StandardEvaluationContext(fun);
		TypeDescriptor t = TypeDescriptor.valueOf(Function.class);
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	private static final String functionTest = "#plus(1,1)";

	private static final String functionTest2 = "#plus(1,1,1)";

	private static Map<String, String> params = Maps.newHashMap();

	@Test
	public void functionTest() throws Exception {
		Expression exp = JCompiler.parse(functionTest);
		StandardEvaluationContext context = new StandardEvaluationContext();
		Method m = ObjectUtils.mirror().on(CompilerTest.class).reflect().method("plus").withArgs(new Class<?>[] { Integer.class, Integer.class });
		context.registerFunction("plus", m);
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
		exp = JCompiler.parse(functionTest2);
		m = ObjectUtils.mirror().on(CompilerTest.class).reflect().method("plus").withArgs(new Class<?>[] { Integer.class, Integer.class, Integer.class });
		context.registerFunction("plus", m);
		spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	@Test
	public void paramTest() throws Exception {
		Expression exp = JCompiler.parse(functionTest);
		StandardEvaluationContext context = new StandardEvaluationContext();
		Method m = ObjectUtils.mirror().on(CompilerTest.class).reflect().method("plus").withArgs(new Class<?>[] { Integer.class, Integer.class });
		context.registerFunction("plus", m);
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	@Test
	public void paramCostTest() throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		for (int i = 0; i < 400; i++) {
			params.put(i + "", i);
		}
		Clock c = ClockUtils.createClock();
		int len = 100000;
		for (int i = 0; i < len; i++) {
			new StandardEvaluationContext().setVariables(params);
		}
		long cost = c.countMillis();
		System.out.println(cost + " tps:" + (len / cost * 1000));
	}

	@Test
	public void paramCostTest2() throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		for (int i = 0; i < 400; i++) {
			params.put("a" + i, i * i);
		}
		int len = 1000000;
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < len; i++) {
			paramtest2(params);
		}
		long cost = c.countMillis();
		System.out.println(cost + " tps:" + (len / cost * 1000));
	}

	private static final String paramTest2 = "#plus(#a1,#a2)";

	public void paramtest2(Map<String, Object> params) throws Exception {
		Expression exp = JCompiler.parse(paramTest2);
		StandardEvaluationContext context = new StandardEvaluationContext();
		Method m = ObjectUtils.mirror().on(CompilerTest.class).reflect().method("plus").withArgs(new Class<?>[] { Integer.class, Integer.class });
		context.registerFunction("plus", m);
		context.setVariables(params);
		SpelExpression spel = exp.getExpression();
		spel.getValue(context);
	}

	@Test
	public void paramCostTest3() throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		for (int i = 0; i < 400; i++) {
			params.put("a" + i, i * i);
		}
		int len = 1000000;
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < len; i++) {
			paramtest3(params);
		}
		long cost = c.countMillis();
		System.out.println(cost + " tps:" + (len / cost * 1000));
	}

	private static final String paramTest3 = "#plus(#ATTR['a1'],#ATTR['a2'])";

	public void paramtest3(Map<String, Object> params) throws Exception {
		Expression exp = JCompiler.parse(paramTest3);
		StandardEvaluationContext context = new StandardEvaluationContext();
		Method m = ObjectUtils.mirror().on(CompilerTest.class).reflect().method("plus").withArgs(new Class<?>[] { Integer.class, Integer.class });
		context.registerFunction("plus", m);
		context.setVariable("ATTR", params);
		SpelExpression spel = exp.getExpression();
		spel.getValue(context);
	}
}
