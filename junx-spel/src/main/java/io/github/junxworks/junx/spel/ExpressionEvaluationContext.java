/*
 ***************************************************************************************
 * 
 * @Title:  ExpressionEvaluationContext.java   
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
import java.util.Map;

import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Maps;


/**
 * 带有参数覆盖检查的上下文
 *
 * @ClassName:  ExpressionEvaluationContext
 * @author: Michael
 * @date:   2018-5-17 19:16:34
 * @since:  v5.0
 */
public class ExpressionEvaluationContext extends StandardEvaluationContext {

	/** variables. */
	private final Map<String, Object> variables = Maps.newHashMap();

	public ExpressionEvaluationContext() {
		super();
	}

	public ExpressionEvaluationContext(Object rootObject) {
		super(rootObject);
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.spel.spel.support.StandardEvaluationContext#setVariable(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setVariable(String name, Object value) {
		putWithWarn(variables, name, value);
	}

	@Override
	public void setVariables(Map<String, Object> variables) {
		this.variables.putAll(variables);
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.spel.spel.support.StandardEvaluationContext#registerFunction(java.lang.String, java.lang.reflect.Method)
	 */
	@Override
	public void registerFunction(String name, Method method) {
		putWithWarn(variables, name, method);
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.spel.spel.support.StandardEvaluationContext#lookupVariable(java.lang.String)
	 */
	@Override
	public Object lookupVariable(String name) {
		return this.variables.get(name);
	}

	private void putWithWarn(Map<String, Object> variables, String key, Object obj) {
		if (variables.put(key, obj) != null) {
			throw new DuplicateKeyException(key);
		}
	}
}
