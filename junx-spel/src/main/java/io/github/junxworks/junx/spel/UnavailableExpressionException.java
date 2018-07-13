/*
 ***************************************************************************************
 * 
 * @Title:  UnavailableExpressionException.java   
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

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 不规范的表达式
 *
 * @ClassName:  UnavailableExpressionException
 * @author: Michael
 * @date:   2018-5-15 18:49:07
 * @since:  v5.0
 */
public class UnavailableExpressionException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7846093433064578444L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public UnavailableExpressionException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public UnavailableExpressionException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public UnavailableExpressionException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public UnavailableExpressionException(String msg, Object... args) {
		super(msg, args);
	}
}

