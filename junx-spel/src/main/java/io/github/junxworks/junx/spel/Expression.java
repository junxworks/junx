/*
 ***************************************************************************************
 * 
 * @Title:  Expression.java   
 * @Package io.github.junxworks.junx.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:26   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel;

import java.lang.reflect.InvocationTargetException;

import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.standard.SpelExpression;

import io.github.junxworks.junx.spel.function.FunctionRepository;

/**
 * 抽象出来的规则对象
 *
 * @ClassName:  Rule
 * @author: Michael
 * @date:   2018-5-15 14:07:25
 * @since:  v5.0
 */
public class Expression {

	/**规则表达式字符串. */
	private String expString;

	/** Spel编译过后的规则表达式. */
	private SpelExpression expression;

	private SpelNode rootNode;

	public String getExpString() {
		return expString;
	}

	public void setExpString(String expString) {
		this.expString = expString;
	}

	public SpelExpression getExpression() {
		return expression;
	}

	public void setExpression(SpelExpression expression) {
		this.expression = expression;
	}

	public SpelNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(SpelNode rootNode) {
		this.rootNode = rootNode;
	}


	public <T> T execute(ExpressionEvaluationContext context, Class<T> expectedResultType) throws ExpressionExecuteFailedException {
		try {
			return expression.getValue(context, expectedResultType);
		} catch (SpelEvaluationException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof InvocationTargetException) {
				Throwable targetE = ((InvocationTargetException) cause).getTargetException();
				if (targetE != null) {
					throw new ExpressionExecuteFailedException("", targetE);
				}
			}
			throw new ExpressionExecuteFailedException("", e);
		} catch (Exception e3) {
			throw new ExpressionExecuteFailedException("", e3);
		}
	}

	public Object execute(ExpressionEvaluationContext context) {
		return execute(context, Object.class);
	}

	public static ExpressionEvaluationContext createContext() {
		return createContext(null);
	}

	/**
	 * 构造一个表达式上下文，并且初始化好可执行函数
	 *
	 * @param rootObject the root object
	 * @return the expression evaluation context
	 */
	public static ExpressionEvaluationContext createContext(Object rootObject) {
		ExpressionEvaluationContext context = new ExpressionEvaluationContext(rootObject);
		context.setVariables(FunctionRepository.currentMethodMap()); //补齐内置函数，有条件的话，可以通过反射来检查是否有重复参数
		return context;
	}
}
