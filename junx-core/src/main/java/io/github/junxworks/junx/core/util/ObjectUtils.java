/*
 ***************************************************************************************
 * 
 * @Title:  ObjectUtils.java   
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.esotericsoftware.kryo.Kryo;
import com.google.common.base.Objects;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;
import io.github.junxworks.junx.core.lang.Copyable;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.MirrorList;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectUtils.
 *
 * @author Michael
 * @since v1.0
 * @date Aug 7, 2015
 */
public class ObjectUtils {

	/** simple type. */
	private static Map<String, Class<?>> simpleType = new HashMap<String, Class<?>>();

	/** containner. */
	private static Map<String, Class<?>> containner = new HashMap<String, Class<?>>();

	/** numeric. */
	private static Map<String, Class<?>> numeric = new HashMap<String, Class<?>>();

	static {
		simpleType.put(BigDecimal.class.getName(), BigDecimal.class);
		simpleType.put(BigInteger.class.getName(), BigInteger.class);
		simpleType.put(java.sql.Date.class.getName(), java.sql.Date.class);
		simpleType.put(java.util.Date.class.getName(), java.util.Date.class);
		simpleType.put(Time.class.getName(), Time.class);
		simpleType.put(Timestamp.class.getName(), Timestamp.class);
		simpleType.put(boolean.class.getName(), boolean.class);
		simpleType.put(Boolean.class.getName(), Boolean.class);
		simpleType.put(char.class.getName(), char.class);
		simpleType.put(Character.class.getName(), Character.class);
		simpleType.put(byte.class.getName(), byte.class);
		simpleType.put(Byte.class.getName(), Byte.class);
		simpleType.put(short.class.getName(), short.class);
		simpleType.put(Short.class.getName(), Short.class);
		simpleType.put(int.class.getName(), int.class);
		simpleType.put(Integer.class.getName(), Integer.class);
		simpleType.put(long.class.getName(), long.class);
		simpleType.put(Long.class.getName(), Long.class);
		simpleType.put(float.class.getName(), float.class);
		simpleType.put(Float.class.getName(), Float.class);
		simpleType.put(double.class.getName(), double.class);
		simpleType.put(Double.class.getName(), Double.class);
		simpleType.put(String.class.getName(), String.class);
		containner.put(Set.class.getName(), Set.class);
		containner.put(HashSet.class.getName(), HashSet.class);
		containner.put(List.class.getName(), List.class);
		containner.put(ArrayList.class.getName(), ArrayList.class);
		containner.put(LinkedList.class.getName(), LinkedList.class);
		containner.put(Vector.class.getName(), Vector.class);
		containner.put(Map.class.getName(), Map.class);
		containner.put(HashMap.class.getName(), HashMap.class);
		containner.put(Hashtable.class.getName(), Hashtable.class);

		numeric.put(Integer.class.getName(), Integer.class);
		numeric.put(int.class.getName(), int.class);
		numeric.put(Long.class.getName(), Long.class);
		numeric.put(long.class.getName(), long.class);
		numeric.put(Float.class.getName(), Float.class);
		numeric.put(float.class.getName(), float.class);
		numeric.put(Double.class.getName(), Double.class);
		numeric.put(double.class.getName(), double.class);
	}

	/**
	 * Creates the object.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Object createObject(String className) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		}
		return createObject(clazz);
	}

	/**
	 * Creates the object.
	 *
	 * @param className the class name
	 * @param params the params
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Object createObject(String className, Object[] params) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		}
		return createObject(clazz, params);
	}

	/**
	 * Creates the object.
	 *
	 * @param clazz the clazz
	 * @return the object
	 */
	public static Object createObject(Class clazz) {
		return new Mirror().on(clazz).invoke().constructor().withoutArgs();
	}

	/**
	 * Creates the object.
	 *
	 * @param clazz the clazz
	 * @param params the params
	 * @return the object
	 */
	public static Object createObject(Class clazz, Object[] params) {
		return new Mirror().on(clazz).invoke().constructor().withArgs(params);
	}

	/**
	 * Creates the object by pass.
	 *
	 * @param clazz the clazz
	 * @return the object
	 */
	public static Object createObjectByPass(Class clazz) {
		return new Mirror().on(clazz).invoke().constructor().bypasser();
	}

	/**
	 * Clone.
	 *
	 * @param object the object
	 * @return the object
	 */
	public static <T> T clone(T object) {
		if (object == null) {
			return null;
		}

		if (object instanceof String[]) {
			String[] source = (String[]) object;
			String[] value = new String[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Long[]) {
			Long[] source = (Long[]) object;
			Long[] value = new Long[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Integer[]) {
			Integer[] source = (Integer[]) object;
			Integer[] value = new Integer[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Short[]) {
			Short[] source = (Short[]) object;
			Short[] value = new Short[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Character[]) {
			Character[] source = (Character[]) object;
			Character[] value = new Character[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Byte[]) {
			Byte[] source = (Byte[]) object;
			Byte[] value = new Byte[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Boolean[]) {
			Boolean[] source = (Boolean[]) object;
			Boolean[] value = new Boolean[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Double[]) {
			Double[] source = (Double[]) object;
			Double[] value = new Double[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Float[]) {
			Float[] source = (Float[]) object;
			Float[] value = new Float[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof BigDecimal[]) {
			BigDecimal[] source = (BigDecimal[]) object;
			BigDecimal[] value = new BigDecimal[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Date[]) {
			Date[] source = (Date[]) object;
			Date[] value = new Date[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = source[i];
			}

			return (T) value;
		}

		if (object instanceof Object[]) {
			Object[] source = (Object[]) object;
			Object[] value = new Object[source.length];
			for (int i = 0; i < source.length; i++) {
				value[i] = clone(source[i]);
			}

			return (T) value;
		}

		if (object instanceof List) {
			List source = (List) object;
			List value = new ArrayList(source.size());
			for (int i = 0, size = source.size(); i < size; i++) {
				value.add(clone(source.get(i)));
			}

			return (T) value;
		}

		if (object instanceof Map) {
			Map source = (Map) object;
			Map value = new HashMap();
			Iterator iter = source.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				value.put(key, clone(source.get(key)));
			}

			return (T) value;
		}

		if (object instanceof Copyable) {
			return (T) ((Copyable) object).deepCopy();
		} else {
			Kryo k = ByteUtils.KRYOPOOL_FALSE.borrow();
			try {
				return (T) k.copy(object);
			} finally {
				ByteUtils.KRYOPOOL_FALSE.release(k);
			}
		}
	}

	/**
	 * Mirror.
	 *
	 * @return the mirror
	 */
	public static Mirror mirror() {
		return new Mirror();
	}

	/**
	 * 返回布尔值 simple type.
	 *
	 * @param obj the obj
	 * @return 返回布尔值 simple type
	 */
	public static boolean isSimpleType(Object obj) {
		return isSimpleType(obj.getClass());
	}

	/**
	 * 返回布尔值 simple type.
	 *
	 * @param clazz the clazz
	 * @return 返回布尔值 simple type
	 */
	public static boolean isSimpleType(Class clazz) {
		return isSimpleType(clazz.getName());
	}

	/**
	 * 返回布尔值 simple type.
	 *
	 * @param clazzName the clazz name
	 * @return 返回布尔值 simple type
	 */
	public static boolean isSimpleType(String clazzName) {
		if (clazzName == null) {
			throw new UnsupportedParameterException("Input parameter can not be null.");
		}
		return simpleType.get(clazzName) == null ? false : true;
	}

	/**
	 * 返回布尔值 containner.
	 *
	 * @param obj the obj
	 * @return 返回布尔值 containner
	 */
	public static boolean isContainner(Object obj) {
		return isContainner(obj.getClass());
	}

	/**
	 * 返回布尔值 containner.
	 *
	 * @param clazz the clazz
	 * @return 返回布尔值 containner
	 */
	public static boolean isContainner(Class clazz) {
		return isContainner(clazz.getName());
	}

	/**
	 * 返回布尔值 containner.
	 *
	 * @param clazzName the clazz name
	 * @return 返回布尔值 containner
	 */
	public static boolean isContainner(String clazzName) {
		if (clazzName == null) {
			throw new UnsupportedParameterException("Input parameter can not be null.");
		}
		return containner.get(clazzName) == null ? false : true;
	}

	/**
	 * 返回布尔值 numeric.
	 *
	 * @param obj the obj
	 * @return 返回布尔值 numeric
	 */
	public static boolean isNumeric(Object obj) {
		return isNumeric(obj.getClass().getName());
	}

	/**
	 * 返回布尔值 numeric.
	 *
	 * @param clazz the clazz
	 * @return 返回布尔值 numeric
	 */
	public static boolean isNumeric(Class<?> clazz) {
		return isNumeric(clazz.getName());
	}

	/**
	 * 返回布尔值 numeric.
	 *
	 * @param clazzName the clazz name
	 * @return 返回布尔值 numeric
	 */
	public static boolean isNumeric(String clazzName) {
		if (clazzName == null) {
			throw new UnsupportedParameterException("Input parameter can not be null.");
		}
		return numeric.get(clazzName) == null ? false : true;
	}

	/**
	 * 返回布尔值 relative.
	 *
	 * @param obj1 the obj1
	 * @param obj2 the obj2
	 * @return 返回布尔值 relative
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isRelative(Class obj1, Class obj2) {
		if (obj1 == obj2)
			return true;
		if (isAncestors(obj1, obj2))
			return true;
		if (isGenerations(obj1, obj2))
			return true;
		return false;
	}

	/**
	 * 返回布尔值 ancestors.
	 *
	 * @param obj1 the obj1
	 * @param obj2 the obj2
	 * @return 返回布尔值 ancestors
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isAncestors(Class obj1, Class obj2) {
		if (obj1 == obj2) {
			return false;
		}
		Class obj2GT = (Class) obj2.getGenericSuperclass();
		if (obj2GT != null) {
			if (obj1 == obj2GT) {
				return true;
			} else {
				return isAncestors(obj1, obj2GT);
			}
		}
		return false;
	}

	/**
	 * 返回布尔值 generations.
	 *
	 * @param obj1 the obj1
	 * @param obj2 the obj2
	 * @return 返回布尔值 generations
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isGenerations(Class obj1, Class obj2) {
		return isAncestors(obj2, obj1);
	}

	/**
	 * To string.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String toString(Object obj) {

		StringBuilder sb = new StringBuilder();
		if (obj instanceof Object[]) {
			for (Object _obj : (Object[]) obj) {
				sb.append(_toJsonString(_obj));
			}
		} else if (obj instanceof Collection) {
			for (Object _obj : (Collection) obj) {
				sb.append(_toJsonString(_obj));
			}
		} else {
			sb.append(_toJsonString(obj));
		}
		return sb.toString();
	}

	/**
	 * _to json string.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	private static String _toJsonString(Object obj) {
		MirrorList<Field> fields = mirror().on(obj.getClass()).reflectAll().fields();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0, len = fields.size(); i < len; i++) {
			Field f = fields.get(i);
			if (i == 0) {
				sb.append(f.getName()).append(":").append(mirror().on(obj).get().field(f));
			} else {
				sb.append(",").append(f.getName()).append(":").append(mirror().on(obj).get().field(f));
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public static int hash(Object o) {
		return Objects.hashCode(o);
	}

}
