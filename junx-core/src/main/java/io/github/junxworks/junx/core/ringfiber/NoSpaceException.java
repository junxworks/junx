/*
 ***************************************************************************************
 * 
 * @Title:  NoSpaceException.java   
 * @Package io.github.junxworks.junx.core.ringfiber   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.ringfiber;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 没有空间
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class NoSpaceException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1763431603790935792L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public NoSpaceException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public NoSpaceException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public NoSpaceException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public NoSpaceException(String msg, Object... args) {
		super(msg, args);
	}
}
