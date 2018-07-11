/*
 ***************************************************************************************
 * 
 * @Title:  ProcessingEventException.java   
 * @Package io.github.junxworks.junx.event   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:47:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.event;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 处理事件时发生了异常
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class ProcessingEventException extends BaseRuntimeException {


	private static final long serialVersionUID = 1351764906384015244L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public ProcessingEventException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public ProcessingEventException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public ProcessingEventException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public ProcessingEventException(String msg, Object... args) {
		super(msg, args);
	}
}
