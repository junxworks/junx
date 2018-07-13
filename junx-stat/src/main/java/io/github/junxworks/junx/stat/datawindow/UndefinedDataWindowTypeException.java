/*
 ***************************************************************************************
 * 
 * @Title:  UndefinedDataWindowTypeException.java   
 * @Package io.github.junxworks.junx.stat.datawindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 未定义的数据窗口类型.
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v4.3
 */
public class UndefinedDataWindowTypeException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -29799416008428412L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public UndefinedDataWindowTypeException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public UndefinedDataWindowTypeException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public UndefinedDataWindowTypeException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public UndefinedDataWindowTypeException(String msg, Object... args) {
		super(msg, args);
	}
}
