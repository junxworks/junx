/*
 ***************************************************************************************
 * 
 * @Title:  TypesUtils.java   
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

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.junxworks.junx.core.exception.UnsupportedTypeException;

/**
 * JDBC类型到java类型的处理工具
 *
 * @ClassName:  TypesUtils
 * @author: Michael
 * @date:   2017-9-22 16:02:50
 * @since:  v1.0
 */
public final class TypesUtils {

	/** type value mapping. */
	private static Map<Integer, String> typeValueMapping = new HashMap<Integer, String>();

	/** type name mapping. */
	private static Map<String, Integer> typeNameMapping = new HashMap<String, Integer>();

	/** 常量 logger. */
	private static final Logger logger = LoggerFactory.getLogger(TypesUtils.class);

	static {
		try {
			Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(java.sql.Types.class.getName());
			Field[] fields = clazz.getFields();
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				int value = fields[i].getInt(null);
				typeValueMapping.put(new Integer(value), name);
				typeNameMapping.put(name, new Integer(value));
			}
		} catch (Exception ex) {
			logger.error("occur error when initialize java type mapping", ex);
		}
	}

	/**
	 * 返回 type name 属性.
	 *
	 * @param type the type
	 * @return type name 属性
	 */
	public static String getTypeName(int type) {
		return (String) typeValueMapping.get(new Integer(type));
	}

	/**
	 * 返回 type value 属性.
	 *
	 * @param name the name
	 * @return type value 属性
	 */
	public static int getTypeValue(String name) {
		if (name == null || name.equals("")) {
			return java.sql.Types.VARCHAR;
		} else {
			return ((Integer) typeNameMapping.get(name)).intValue();
		}
	}

	/**
	 * 返回布尔值 lob.
	 *
	 * @param type the type
	 * @return 返回布尔值 lob
	 */
	public static boolean isLob(int type) {
		switch (type) {
			case java.sql.Types.CLOB:
			case java.sql.Types.BLOB: {
				return true;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * 返回布尔值 string.
	 *
	 * @param type the type
	 * @return 返回布尔值 string
	 */
	public static boolean isString(int type) {
		switch (type) {
			case Types.LONGVARCHAR:
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.CLOB:
			case Types.BLOB: {
				return true;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * 返回布尔值 temporal.
	 *
	 * @param type the type
	 * @return 返回布尔值 temporal
	 */
	public static boolean isTemporal(int type) {
		switch (type) {
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP: {
				return true;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * 返回 temporal class 属性.
	 *
	 * @param type the type
	 * @return temporal class 属性
	 */
	public static Class<?> getTemporalClass(int type) {
		switch (type) {
			case Types.DATE: {
				return java.sql.Date.class;
			}
			case Types.TIME: {
				return java.sql.Time.class;
			}
			case Types.TIMESTAMP: {
				return java.sql.Timestamp.class;
			}
			default: {
				return java.sql.Date.class;
			}
		}
	}

	/**
	 * 根据sqltype获取java类型
	 *
	 * @param sqlType the sql type
	 * @return java type 属性
	 */
	public static Class<?> getJavaType(int sqlType) {
		switch (sqlType) {
			case Types.LONGVARCHAR:
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.CLOB:
			case Types.BLOB:
				return String.class;
			case Types.DATE:
				return java.sql.Date.class;
			case Types.TIME:
				return java.sql.Time.class;
			case Types.TIMESTAMP:
				return java.sql.Timestamp.class;
			case Types.BIT:
				return boolean.class;
			case Types.TINYINT:
				return byte.class;
			case Types.SMALLINT:
				return short.class;
			case Types.INTEGER:
				return int.class;
			case Types.BIGINT:
				return long.class;
			case Types.REAL:
				return float.class;
			case Types.FLOAT:
				return double.class;
			case Types.DOUBLE:
				return double.class;
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				return byte[].class;
			case Types.DECIMAL:
			case Types.NUMERIC:
				return java.math.BigDecimal.class;
		}
		throw new UnsupportedTypeException("Unsupported JDBC type %d", sqlType);
	}
}