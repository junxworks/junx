/*
 ***************************************************************************************
 * 
 * @Title:  ResumeServiceException.java   
 * @Package io.github.junxworks.junx.core.lifecycle   
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
package io.github.junxworks.junx.core.lifecycle;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 服务恢复异常.
 *
 * @author: Michael
 * @date:   2017-5-7 17:32:52
 * @since:  v1.0
 */
public class ResumeServiceException extends BaseRuntimeException {
	
	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String)
	 */
	public ResumeServiceException(String msg) {
		super(msg);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(Throwable)
	 */
	public ResumeServiceException(Throwable ex) {
		super(ex);
	}

	/**
	 * @see BaseRuntimeException#BaseRuntimeException(String,Throwable)
	 */
	public ResumeServiceException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
