/*
 ***************************************************************************************
 * 
 * @Title:  StringUtils.java   
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

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 提供通用的字符串处理方法，继承自apache-commons的StringUtils类，
 * 同时也使用guava处理字符串的相关对象。
 * 
 * @author: Michael
 * @date:   2017-5-7 18:01:32
 * @since:  v1.0
 * @see org.apache.commons.lang3.StringUtils
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 如果字符串是null、空格或者"null"字符串，那么则返回true，否则返回false。
	 * @param str 入参，用于判断的字符串
	 * @return 如果字符串是null、空格或者"null"字符串，那么则返回true，反之返回false
	 */
	public static boolean isNull(String str) {
		return str == null || str.trim().length() == 0 || str.equalsIgnoreCase("null");
	}

	/**
	 * 判断一个字符串是否不为空。
	 *
	 * @param str 入参，用于判断的字符串
	 * @return 跟{@link StringUtils#isNull(String)}返回相反的值
	 */
	public static boolean notNull(String str) {
		return !isNull(str);
	}

	/**
	 * 将字符串中所有空格都去掉。
	 *
	 * @param str 用于替换的字符串
	 * @return the 替换过后的字符串
	 */
	public static String trimAll(String str) {
		return str.replaceAll(" ", "");
	}

	/**
	 * 判断str字符串是否包含在指定的字符串数组strs中
	 *
	 * @param str 用于判断的字符串
	 * @param strs 字符串数组
	 * @return 如果str在strs中，那么返回true，否则false
	 */
	public static boolean isIn(String str, String[] strs) {
		if (str == null) {
			throw new BaseRuntimeException("Input String can not be empty!");
		}
		for (int i = 0, len = strs.length; i < len; i++) {
			if (str.equals(strs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断suffix是否是给定string对象str的末尾，忽略大小写
	 *
	 * @param str 需要判断的字符串对象
	 * @param suffix 结尾字符串
	 * @return 忽略大小写的判断，如果suffix匹配str对象的末尾，则返回true，反之返回false
	 */
	public static boolean endWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			throw new BaseRuntimeException("Suffix string object can't be null.");
		}
		if (str.length() < suffix.length()) {
			return false;
		}
		return str.substring(str.length() - suffix.length()).equalsIgnoreCase(suffix);
	}

	/**
	 * 判断prefix是否是给定string对象str的开头，忽略大小写
	 *
	 * @param str 需要判断的字符串对象
	 * @param prefix 开头字符串
	 * @return 忽略大小写的判断，如果prefix匹配str对象的开头，则返回true，反之返回false
	 */
	public static boolean startWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null || str.length() < prefix.length()) {
			throw new RuntimeException("Prefix string object can't be null.");
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
	}

	/**
	 * 根据value对象，返回一个boolean值.
	 *
	 * @param value 入参
	 * @param defaultValue 默认值
	 * @return 将value入参转换成boolean的值
	 */
	public static boolean getBoolean(Object value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			if (value instanceof Boolean) {
				return ((Boolean) value).booleanValue();
			} else if (value instanceof String) {
				String stringValue = (String) value;
				return StringUtils.isNull(stringValue) ? defaultValue : stringValue.equalsIgnoreCase(Boolean.TRUE.toString());
			} else if (value instanceof Number) {
				return ((Number) value).intValue() > 0;
			} else {
				return defaultValue;
			}
		}
	}

	/**
	 * 首字母大写.
	 *
	 * @param str 需要首字母大写的字符串
	 * @return 首字母大写过后的字符串
	 */
	public static String firstLetterToUpperCase(String str) {
		if (isNull(str)) {
			return null;
		} else if (str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 格式化字符串.
	 *
	 * @param format模板，类似"this is %s ,hello."
	 * @param args 需要替换模板里面对应字符的参数
	 * @return 替换过后的字符串
	 */
	public static String format(String format, Object... args) {
		return String.format(format, args);
	}

	/**
	* 将指定字符数组中的参数用指定的seperator连接起来，会自动剔除null字符
	* @param seperator 连接符
	* @param strs 需要连接在基础字符串上的对象
	* @return 连接过后的字符串
	* @throws
	*/
	public static String join(String separator, String... strs) {
		Joiner joiner = Joiner.on(separator).skipNulls();
		return joiner.join(strs);
	}

	/**
	 * 使用guava的分离器，对字符串进行拆分，并且忽略前导和尾部空白，
	 * 并且忽略分离过程中出现的空串。
	 *
	 * @param source 需要切分的字符串
	 * @param separator 分隔符
	 * @return 切分过后的字符串列表
	 */
	public static List<String> splitter(String source, String separator) {
		return Lists.newArrayList(Splitter.on(separator) //设置拆分字符串
				.trimResults() //移除结果字符串的前导空白和尾部空白
				.omitEmptyStrings() //从结果中自动忽略空字符串
				.split(source));
	}

	/**
	* 自定义的一个string模板处理方法，可以在string中加入${xxx}变量，然后程序里面生成对应的string 
	* @param strTemplate 字符串模板
	* @param conditions 条件map对象
	* @return 处理过后的string字符串
	* @throws
	 */
	public static String process(String strTemplate, Map<String, String> conditions) {
		if (!StringUtils.isNull(strTemplate)) {
			String regex = "\\$\\{\\w+\\}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(strTemplate);
			while (m.find()) {
				String mather = m.group();
				String paramName = mather.substring(2, mather.length() - 1);
				String replacement = conditions != null && !conditions.isEmpty() ? conditions.get(paramName) : null;
				if (!StringUtils.isBlank(replacement)) {
					strTemplate = StringUtils.replace(strTemplate, mather, replacement);
				} else {
					strTemplate = StringUtils.replace(strTemplate, mather, "");
				}
			}
			return strTemplate;
		}
		return "";
	}

	public static String defaultString(Object obj, String defaultSgtring) {
		return obj == null ? defaultSgtring : obj.toString();
	}
}
