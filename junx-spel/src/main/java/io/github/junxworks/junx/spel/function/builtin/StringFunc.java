/*
 ***************************************************************************************
 * 
 * @Title:  StringFunc.java   
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

import io.github.junxworks.junx.spel.function.anno.Func;
import io.github.junxworks.junx.spel.function.anno.FuncMethod;

import io.github.junxworks.junx.core.exception.EmptyStringException;
import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 字符串函数
 *
 * @ClassName:  StringFunc
 * @author: Michael
 * @date:   2018-5-18 18:38:22
 * @since:  v1.0
 */
@Func
public class StringFunc {

	/**
	 * 子串
	 *
	 * @param str the str
	 * @param from the from
	 * @param to the to
	 * @return the string
	 */
	@FuncMethod(funcDesc = "字符串截取", funcGroup = { BuiltInFunctions.FUNC_GROUP_STRING }, funcName = BuiltInFunctions.SUBSTR)
	public static String subStr(Object str, int beginIndex, int endIndex) {
		String string = String.valueOf(str);
		if (StringUtils.isNull(string)) {
			throw new EmptyStringException("Input String is null.");
		}
		try {
			return string.substring(beginIndex, endIndex);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.SUBSTR, string + "[" + beginIndex + "," + endIndex + "]", e);
		}
	}
}
