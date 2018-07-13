/*
 ***************************************************************************************
 * 
 * @Title:  DuplicateFunctionException.java   
 * @Package io.github.junxworks.junx.spel.function   
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
package io.github.junxworks.junx.spel.function;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 不规范的表达式
 *
 * @ClassName:  UnavailableExpressionException
 * @author: Michael
 * @date:   2018-5-15 18:49:07
 * @since:  v1.0
 */
public class DuplicateFunctionException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5767583243201442276L;

	public DuplicateFunctionException() {
		super();
	}
	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public DuplicateFunctionException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public DuplicateFunctionException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public DuplicateFunctionException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public DuplicateFunctionException(String msg, Object... args) {
		super(msg, args);
	}
}

