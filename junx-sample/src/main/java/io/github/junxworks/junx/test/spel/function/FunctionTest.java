/*
 ***************************************************************************************
 * 
 * @Title:  FunctionTest.java   
 * @Package io.github.junxworks.junx.test.spel.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 12:52:13   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel.function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.junxworks.junx.spel.Expression;
import io.github.junxworks.junx.spel.ExpressionEvaluationContext;
import io.github.junxworks.junx.spel.JCompiler;
import io.github.junxworks.junx.spel.function.FunctionRepository;
import io.github.junxworks.junx.test.spel.SpelApplication;
import io.github.junxworks.junx.test.spel.customize.MyFunc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpelApplication.class)
@Import({ MyFunc.class }) //引入自定义函数
public class FunctionTest {

	private static final String expMaxMin = "#max(4,2,100.1f)+#min(1,3,-123)";

	@Test
	public void maxminTest() throws Exception {
		Expression exp = JCompiler.parse(expMaxMin);
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables(FunctionRepository.currentMethodMap());
		SpelExpression spel = exp.getExpression();
		System.out.println(spel.getValue(context));
	}

	private static final String expSum = "#sum(#max(4,2,100.1f),#min(1,3,-123),'100.1234')";

	@Test
	public void sumTest() throws Exception {
		Expression exp;
		exp = JCompiler.parse(expSum);
		ExpressionEvaluationContext context = new ExpressionEvaluationContext();
		System.out.println(exp.execute(context));
	}

	private static final String devide = "#devide(2.1,0,10)";

	@Test
	public void devideTest() throws Exception {
		Expression exp = JCompiler.parse(devide);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String mutiply = "#mutiply(2.1,12.341,10)";

	@Test
	public void mutiplyTest() throws Exception {
		Expression exp = JCompiler.parse(mutiply);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String sub = "#sub(1.0 , 0.42)";

	@Test
	public void substractTest() throws Exception {
		Expression exp = JCompiler.parse(sub);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
		System.out.println(System.currentTimeMillis());
	}

	private static final String format = "#format(1526873393911L, 'yyyy-MM-dd HH:mm:ss.SSS')";

	@Test
	public void dateFormatTest() throws Exception {
		Expression exp = JCompiler.parse(format);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String subStr = "#subStr('1234567890',3,5)";

	@Test
	public void subStrTest() throws Exception {
		Expression exp = JCompiler.parse(subStr);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}

	private static final String customizeStr = "#my('Michael')";

	/**
	 * @Import({ MyFunc.class }) //要事先在class上引入自定义函数
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void customizeTest() throws Exception {
		Expression exp = JCompiler.parse(customizeStr);
		ExpressionEvaluationContext context = Expression.createContext();
		System.out.println(exp.execute(context));
	}
}
