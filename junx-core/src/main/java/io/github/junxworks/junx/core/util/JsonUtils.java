/*
 ***************************************************************************************
 * 
 * @Title:  JsonUtils.java   
 * @Package io.github.junxworks.junx.core.util   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:35   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.util;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonUtils.
 *
 * @author Michael
 * @since v1.0
 * @date Aug 7, 2015
 */
public class JsonUtils {

	/**
	 * Quote.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String quote(String string) {
		if (string == null || string.length() == 0) {
			return "";
		}

		char b;
		char c = 0;
		int i;
		int len = string.length();
		StringBuffer sb = new StringBuffer(len + 4);
		String t;

		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					sb.append('\\');
					sb.append(c);
					break;
				case '/':
					if (b == '<') {
						sb.append('\\');
					}
					sb.append(c);
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\r':
					sb.append("\\r");
					break;
				default:
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
						t = "000" + Integer.toHexString(c);
						sb.append("\\u" + t.substring(t.length() - 4));
					} else {
						sb.append(c);
					}
			}
		}
		return sb.toString();
	}

	/**
	 * Parses the object f.
	 *
	 * @param <T> the generic type
	 * @param claz the claz
	 * @param jsonStr the json str
	 * @return the t
	 */

	public static <T> T parseObject(Class<T> claz, String jsonStr) {
		T t = null;
		t = com.alibaba.fastjson.JSON.parseObject(jsonStr, claz);
		return t;
	}

	/**
	 * Parses the array f.
	 *
	 * @param <T> the generic type
	 * @param claz the claz
	 * @param jsonStr the json str
	 * @return the list
	 */
	public static <T> List<T> parseArray(Class<T> claz, String jsonStr) {
		List<T> t = null;
		t = com.alibaba.fastjson.JSON.parseArray(jsonStr, claz);
		return t;
	}

	/**
	 * Object to string f.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String objectToString(Object obj) {
		return com.alibaba.fastjson.JSON.toJSONString(obj);
	}

}