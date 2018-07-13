/*
 ***************************************************************************************
 * 
 * @Title:  ExpressionExecuteFailedException.java   
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

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 不规范的表达式
 *
 * @ClassName:  UnavailableExpressionException
 * @author: Michael
 * @date:   2018-5-15 18:49:07
 * @since:  v5.0
 */
public class ExpressionExecuteFailedException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2281292609291519970L;

	public ExpressionExecuteFailedException() {
		super();
	}
	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public ExpressionExecuteFailedException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public ExpressionExecuteFailedException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public ExpressionExecuteFailedException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public ExpressionExecuteFailedException(String msg, Object... args) {
		super(msg, args);
	}
}

