/*
 ***************************************************************************************
 * 
 * @Title:  FunctionRepository.java   
 * @Package io.github.junxworks.junx.spel.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 12:16:41   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel.function;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

/**
 * 存储函数的类，线程安全
 *
 * @ClassName:  FunctionRepository
 * @author: Michael
 * @date:   2018-7-13 11:15:11
 * @since:  v1.0
 */
public class FunctionRepository {

	/** methods. */
	private static Map<String, MethodDescriptor> methods = Maps.newHashMap();

	/** method map. */
	private static Map<String, Object> methodMap;

	/**
	 * Func map.
	 *
	 * @return the map
	 */
	public static Map<String, Object> currentMethodMap() {
		if (methodMap == null) {
			synchronized (FunctionRepository.class) {
				if (methodMap == null) {
					methodMap = extractMethodMap();
				}
			}
		}
		return methodMap;
	}

	private static Map<String, Object> extractMethodMap() {
		return methods.values().stream().collect(Collectors.toMap(md -> md.getName(), md -> md.getMethod()));
	}

	/**
	 * 以写时复制的方式实现新增方法
	 *
	 * @param inputMethods the input methods
	 */
	public static synchronized void putAllMethods(Map<String, MethodDescriptor> inputMethods) {
		Map<String, MethodDescriptor> _methods = Maps.newHashMap();
		_methods.putAll(methods);
		_methods.putAll(inputMethods);
		methods = _methods;
		methodMap = extractMethodMap();
	}

	/**
	 * 以写时复制的方式实现新增方法
	 *
	 * @param inputMethods the input methods
	 */
	public static synchronized void putMethod(MethodDescriptor inputMethod) {
		Map<String, MethodDescriptor> _methods = Maps.newHashMap();
		_methods.putAll(methods);
		_methods.put(inputMethod.getName(), inputMethod);
		methods = _methods;
		methodMap = extractMethodMap();
	}
}
