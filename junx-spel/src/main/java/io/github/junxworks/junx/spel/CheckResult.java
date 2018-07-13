/*
 ***************************************************************************************
 * 
 * @Title:  CheckResult.java   
 * @Package io.github.junxworks.junx.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:27   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel;

import io.github.junxworks.junx.core.util.StringUtils;

public class CheckResult {
	private Exception exception;

	private String exceptionCause;

	private String message;

	private boolean pass;

	public Exception getException() {
		return exception;
	}

	public String getExceptionCause() {
		return exceptionCause;
	}

	public void setExceptionCause(String exceptionCause) {
		this.exceptionCause = exceptionCause;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return StringUtils.isNull(message) ? exceptionCause : message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

}
