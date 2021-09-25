/*
 ***************************************************************************************
 * 
 * @Title:  FatalException.java   
 * @Package io.github.junxworks.junx.core.exception   
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
package io.github.junxworks.junx.core.exception;

/**
 * 致命异常
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class FatalException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8210543412924486043L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public FatalException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public FatalException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public FatalException(String msg, Throwable ex) {
		super(msg, ex);
	}

	public FatalException(String msg, Object... args) {
		super(msg, args);
	}
}
