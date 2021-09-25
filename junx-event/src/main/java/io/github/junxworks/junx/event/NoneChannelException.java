/*
 ***************************************************************************************
 * 
 * @Title:  NoneChannelException.java   
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
 * 没有找到对应topic的通道.
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class NoneChannelException extends BaseRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7747139626177893299L;

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public NoneChannelException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public NoneChannelException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public NoneChannelException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
