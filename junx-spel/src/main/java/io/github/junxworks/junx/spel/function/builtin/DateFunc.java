/*
 ***************************************************************************************
 * 
 * @Title:  DateFunc.java   
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

import io.github.junxworks.junx.core.exception.NullParameterException;
import io.github.junxworks.junx.core.util.DateUtils;
import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 日期函数
 *
 * @ClassName:  DateFunc
 * @author: Michael
 * @date:   2018-5-18 18:38:40
 * @since:  v1.0
 */
@Func
public class DateFunc {

	/**
	 * 对时间撮进行格式化.
	 *
	 * @param timestamp the timestamp
	 * @param format the format
	 * @return the string
	 */
	@FuncMethod(funcDesc = "格式化", funcGroup = { BuiltInFunctions.FUNC_GROUP_DATE }, funcName = BuiltInFunctions.FORMAT)
	public static String format(Object timestamp, String format) {
		String string = String.valueOf(timestamp);
		if (StringUtils.isNull(string)) {
			throw new NullParameterException("Input parameter is null.");
		}
		try {
			return DateUtils.formatDate(Long.valueOf(string), format);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.FORMAT, string + "[" + format + "]", e);
		}
	}
}
