/*
 ***************************************************************************************
 * 
 * @Title:  MathFunc.java   
 * @Package io.github.junxworks.junx.spel.function.builtin   
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
package io.github.junxworks.junx.spel.function.builtin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.github.junxworks.junx.spel.function.FunctionExecuteFailedException;
import io.github.junxworks.junx.spel.function.anno.Func;
import io.github.junxworks.junx.spel.function.anno.FuncMethod;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;
import io.github.junxworks.junx.core.util.NumberUtils;

@Func
public class MathFunc {
	@FuncMethod(funcDesc = "最大值", funcName = BuiltInFunctions.MAX, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static Double max(Object... objs) {
		try {
			return NumberUtils.max(Arrays.asList(objs).stream().mapToDouble(x -> Double.valueOf(x.toString())).toArray());
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.MAX, objs, e);
		}
	}

	@FuncMethod(funcDesc = "最小值", funcName = BuiltInFunctions.MIN, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static Double min(Object... objs) {
		try {
			return NumberUtils.min(Arrays.asList(objs).stream().mapToDouble(x -> Double.valueOf(x.toString())).toArray());
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.MIN, objs, e);
		}

	}

	/**
	 * 为了避免java浮点数计算产生的精度问题，加入了sum求和函数.
	 *
	 * @param objs the objs
	 * @return the big decimal
	 * @throws UnsupportedParameterException the unsupported parameter exception
	 */
	@FuncMethod(funcDesc = "求和", funcName = BuiltInFunctions.SUM, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static BigDecimal sum(Object... objs) throws FunctionExecuteFailedException {
		try {
			BigDecimal[] bgs = new BigDecimal[objs.length];
			Arrays.asList(objs).stream().map(x -> {
				return new BigDecimal(x.toString());
			}).collect(Collectors.toList()).toArray(bgs);
			return NumberUtils.sum(bgs);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.SUM, objs, e);
		}
	}

	/**
	 * 为了避免java浮点数计算产生的精度问题，加入了sum求和函数.
	 *
	 * @param objs the objs
	 * @return the big decimal
	 * @throws UnsupportedParameterException the unsupported parameter exception
	 */
	@FuncMethod(funcDesc = "求差", funcName = BuiltInFunctions.SUB, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static BigDecimal subtract(Object minuend, Object subtrahend) throws FunctionExecuteFailedException {
		try {
			return NumberUtils.subtract(minuend, subtrahend);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.SUB, String.valueOf(minuend) + "-" + String.valueOf(subtrahend), e);
		}
	}

	/**
	 * 为了避免java浮点数计算产生的精度问题，加入了devide除函数.
	 *
	 * @param objs the objs
	 * @return the big decimal
	 * @throws UnsupportedParameterException the unsupported parameter exception
	 */
	@FuncMethod(funcDesc = "除以", funcName = BuiltInFunctions.DEVIDE, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static BigDecimal devide(Object denominator, Object divisor, int scale) throws FunctionExecuteFailedException {
		try {
			return NumberUtils.devide(denominator, divisor, scale);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.DEVIDE, String.valueOf(denominator) + "/" + String.valueOf(divisor), e);
		}
	}

	/**
	 * 为了避免java浮点数计算产生的精度问题，加入了sum求和函数.
	 *
	 * @param objs the objs
	 * @return the big decimal
	 * @throws UnsupportedParameterException the unsupported parameter exception
	 */
	@FuncMethod(funcDesc = "乘以", funcName = BuiltInFunctions.MUTIPLY, funcGroup = { BuiltInFunctions.FUNC_GROUP_MATH })
	public static BigDecimal mutiply(Object multiplicand1, Object multiplicand2, int scale) throws FunctionExecuteFailedException {
		try {
			return NumberUtils.mutiply(multiplicand1, multiplicand2, scale);
		} catch (Exception e) {
			throw MsgHelper.getException(MathFunc.class, BuiltInFunctions.MUTIPLY, String.valueOf(multiplicand1) + "×" + String.valueOf(multiplicand2), e);
		}
	}
}
