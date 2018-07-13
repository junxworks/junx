/*
 ***************************************************************************************
 * 
 * @Title:  MsgHelper.java   
 * @Package io.github.junxworks.junx.spel.function.builtin   
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
package io.github.junxworks.junx.spel.function.builtin;

import io.github.junxworks.junx.spel.function.FunctionExecuteFailedException;

import com.alibaba.fastjson.JSON;

public class MsgHelper {
	public static FunctionExecuteFailedException getException(Class<?> clazz, String name, Object param, Throwable e) {
		return new FunctionExecuteFailedException("Function \"%s.%s\" execute failed,parameter is %s,exception is \"%s\"", clazz.getCanonicalName(), name, JSON.toJSONString(param), e.toString());
	}
}
