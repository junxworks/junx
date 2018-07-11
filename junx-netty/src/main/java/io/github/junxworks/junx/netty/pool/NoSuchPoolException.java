/*
 ***************************************************************************************
 * 
 * @Title:  UnsupportedFormatException.java   
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
package io.github.junxworks.junx.netty.pool;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 没有对应的连接池
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class NoSuchPoolException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8287230112763682317L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException()
	 */
	public NoSuchPoolException() {
		super("");
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public NoSuchPoolException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public NoSuchPoolException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public NoSuchPoolException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Object...)
	 */
	public NoSuchPoolException(String msg, Object... args) {
		super(msg, args);
	}

	/**
	 *  @see BaseRuntimeException#BaseRuntimeException(String,Exception,Object...)
	 */
	public NoSuchPoolException(String msg, Exception e, Object... args) {
		super(msg, e, args);
	}
}
