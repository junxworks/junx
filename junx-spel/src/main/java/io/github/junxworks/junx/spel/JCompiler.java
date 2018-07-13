/*
 ***************************************************************************************
 * 
 * @Title:  Compiler.java   
 * @Package io.github.junxworks.junx.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:27   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.ast.FunctionReference;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import io.github.junxworks.junx.core.util.ExceptionUtils;
import io.github.junxworks.junx.core.util.ObjectUtils;
import io.github.junxworks.junx.spel.function.FunctionRepository;

/**
 * 编译工具类
 *
 * @ClassName:  ExpressionUtils
 * @author: Michael
 * @date:   2018-5-15 14:19:29
 * @since:  v1.0
 */
public class JCompiler {
	private static SpelExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, Thread.currentThread().getContextClassLoader()));

	public static Expression parse(String expressionString, ParserContext context) throws Exception {
		try {
			Expression exp = new Expression();
			SpelExpression expression = (SpelExpression) parser.parseExpression(expressionString, context);
			exp.setExpression(expression);
			exp.setExpString(expressionString);
			exp.setRootNode(expression.getAST());
			CheckResult res = check(exp);
			if (res.isPass()) {
				return exp;
			} else {
				throw new UnavailableExpressionException("Unable to compile expression %s[%s]", expressionString, res.getMessage());
			}
		} catch (Exception e) {
			throw new UnavailableExpressionException("Unable to compile expression %s[%s]", expressionString, ExceptionUtils.getCauseMessage(e));
		}
	}

	/**
	 * 分析表达式内部元素
	 *
	 * @param exp the exp
	 */
	public static NodeVisitor analyzeExpression(Expression exp, NodeVisitor visitor) {
		visit(exp.getRootNode(), visitor);
		return visitor;
	}

	private static void visit(SpelNode node, NodeVisitor visitor) {
		visitor.visit(node);
		int childCount = node.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				visit(node.getChild(i), visitor);
			}
		}
	}

	public static Expression parse(String expressionString) throws Exception {
		return parse(expressionString, null);
	}

	/**
	 * 检查表达式是否合法.
	 *
	 * @param expression the expression
	 * @return the test result
	 * @throws Exception the exception
	 */
	public static CheckResult check(Expression expression) throws Exception {
		CheckResult res = new CheckResult();
		try {
			JCompiler.analyzeExpression(expression, new NodeVisitor() {

				@Override
				public void visit(SpelNode node) {
					if (node instanceof FunctionReference) {
						FunctionReference f = (FunctionReference) node;
						String funcName = ObjectUtils.mirror().on(f).get().field("name").toString();
						Object method = FunctionRepository.currentMethodMap().get(funcName);
						if (method == null) {
							throw new UnavailableExpressionException("Function \"%s\" not found.", funcName);
						}
						if (method instanceof Method) {
							Method m = (Method) method;
							int paramSize = m.getParameters().length;
							//检查method是否包含数组...参数
							boolean hasArray = false;
							Parameter[] ps = m.getParameters();
							for (Parameter p : ps) {
								String parameterString = p.toString();
								if (parameterString.contains("...")) {
									hasArray = true;
								}
							}
							if (!hasArray && f.getChildCount() != paramSize) {
								throw new UnavailableExpressionException("Function \"%s\" parameter size is %d,but there is noly %d in the expression.", funcName, paramSize, f.getChildCount());
							}
						} else {
							throw new UnavailableExpressionException("Function \"%s\" is not Method", funcName);
						}
					}
				}

			});
		} catch (Exception e) {
			res.setException(e);
			res.setExceptionCause(ExceptionUtils.getCauseMessage(e));
			return res;
		}
		res.setPass(true);
		return res;
	}

}
