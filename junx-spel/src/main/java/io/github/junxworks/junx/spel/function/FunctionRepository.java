/*
 ***************************************************************************************
 * 
 * @Title:  FunctionRepository.java   
 * @Package io.github.junxworks.junx.spel.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:26   
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

public class FunctionRepository {
	private static Map<String, MethodDescriptor> methods = Maps.newHashMap();

	private static Map<String, Object> methodMap;

	public static Map<String, MethodDescriptor> methods() {
		return methods;
	}

	public static Map<String, Object> funcMap() {
		if (methodMap == null) {
			synchronized (FunctionRepository.class) {
				if (methodMap == null) {
					methodMap = methods.values().stream().collect(Collectors.toMap(md -> md.getName(), md -> md.getMethod()));
				}
			}
		}
		return methodMap;
	}
}
