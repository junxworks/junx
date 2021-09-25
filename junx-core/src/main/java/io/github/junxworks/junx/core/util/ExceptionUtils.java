/*
 ***************************************************************************************
 * 
 * @Title:  ExceptionUtils.java   
 * @Package io.github.junxworks.junx.core.util   
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
package io.github.junxworks.junx.core.util;

public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {
	public static String getCauseMessage(Throwable e) {
		return getCause(e).getMessage();
	}

	public static Throwable getCause(Throwable e) {
		while (e.getCause() != null) {
			e = e.getCause();
		}
		return e;
	}
}
