/*
 ***************************************************************************************
 * 
 * @Title:  MyFunc.java   
 * @Package io.github.junxworks.junx.test.spel.customize   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel.customize;

import io.github.junxworks.junx.spel.function.anno.Func;
import io.github.junxworks.junx.spel.function.anno.FuncMethod;
import io.github.junxworks.junx.spel.function.builtin.BuiltInFunctions;

@Func
public class MyFunc {

	public static final String FUNC_CUSTOM = "my";

	@FuncMethod(funcDesc = "自定义函数", funcGroup = { BuiltInFunctions.FUNC_GROUP_STRING }, funcName = FUNC_CUSTOM)
	public static String customizeFunc(String name) {
		System.out.println("Hello " + name);
		return name + "-";
	}
}
