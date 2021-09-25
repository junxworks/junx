/*
 ***************************************************************************************
 * 
 * @Title:  StopPooledThreadException.java   
 * @Package io.github.junxworks.junx.core.executor   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:35   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.executor;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 停止线程池异常.
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class StopPooledThreadException extends BaseRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5664216042899222111L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public StopPooledThreadException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public StopPooledThreadException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public StopPooledThreadException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
