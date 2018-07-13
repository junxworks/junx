/*
 ***************************************************************************************
 * 
 * @Title:  UndefinedRangeTypeException.java   
 * @Package io.github.junxworks.junx.stat.function.rangedist   
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
package io.github.junxworks.junx.stat.function.rangedist;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 未定义的区间类型，目前支持week、hour、number类型
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class UndefinedRangeTypeException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436443265473973074L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public UndefinedRangeTypeException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public UndefinedRangeTypeException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public UndefinedRangeTypeException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public UndefinedRangeTypeException(String msg, Object... args) {
		super(msg, args);
	}
}
