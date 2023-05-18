/*
 ***************************************************************************************
 * 
 * @Title:  NumberUtils.java   
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;

/**
 * 数字类型的工具类
 *
 * @author: Michael
 * @date:   2017-5-7 18:32:15
 * @since:  v1.0
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

	private static final int DEFAULT_SCALE = 2;

	/**
	 * 汉语中数字大写
	 */
	private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	/**
	 * 汉语中货币单位大写，这样的设计类似于占位符
	 */
	private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };

	/**
	 * 特殊字符：整
	 */
	private static final String CN_FULL = "整";

	/**
	 * 特殊字符：负
	 */
	private static final String CN_NEGATIVE = "负";

	/**
	 * 金额的精度，默认值为2
	 */
	private static final int MONEY_PRECISION = 2;

	/**
	 * 特殊字符：零元整
	 */
	private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

	/**
	 * 把输入的金额转换为汉语中人民币的大写
	 * 
	 * @param numberOfMoney
	 *            输入的金额
	 * @return 对应的汉语大写
	 */
	public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
		StringBuffer sb = new StringBuffer();
		// -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
		// positive.
		int signum = numberOfMoney.signum();
		// 零元整的情况
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}
		// 这里会进行金额的四舍五入
		long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
		// 得到小数点后两位值
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
		if (!(scale > 0)) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if ((scale > 0) && (!(scale % 10 > 0))) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			if (number <= 0) {
				break;
			}
			// 每次获取到最后一个数
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				if ((numIndex == 9) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
				}
				if ((numIndex == 13) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
				}
				sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				++zeroSize;
				if (!(getZero)) {
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				}
				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
				} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				}
				getZero = true;
			}
			// 让number每次都去掉最后一个数
			number = number / 10;
			++numIndex;
		}
		// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (!(scale > 0)) {
			sb.append(CN_FULL);
		}
		return sb.toString();
	}

	/**
	 * 对一组BigDecimal求和.
	 *
	 * @param bigDecimals the big decimals
	 * @return the big decimal
	 */
	public static BigDecimal sum(BigDecimal... bigDecimals) {
		return sumWithScale(DEFAULT_SCALE, bigDecimals);
	}

	public static BigDecimal sum(Object... bigDecimals) {
		return sumWithScale(DEFAULT_SCALE, bigDecimals);
	}

	public static BigDecimal sumWithScale(int scale, Object... numbers) {
		return sumWithScale(scale, Arrays.asList(numbers).stream().flatMap(n -> {
			if (n == null) {
				return Stream.of(BigDecimal.ZERO);
			}
			return Stream.of(new BigDecimal(String.valueOf(n)));
		}).collect(Collectors.toList()).toArray(new BigDecimal[0]));
	}

	public static BigDecimal sumWithScale(int scale, BigDecimal... bigDecimals) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0, len = bigDecimals.length; i < len; i++) {
			if (bigDecimals[i] != null) {
				sum = sum.add(bigDecimals[i]);
			}
		}
		return sum.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * 减.
	 *
	 * @param minuend the minuend
	 * @param subtrahend the subtrahend
	 * @return the big decimal
	 */
	public static BigDecimal subtract(Object minuend, Object subtrahend) {
		return subtract(new BigDecimal(String.valueOf(minuend)), new BigDecimal(String.valueOf(subtrahend)));
	}

	/**
	 * 减.
	 *
	 * @param minuend the minuend
	 * @param subtrahend the subtrahend
	 * @return the big decimal
	 */
	public static BigDecimal subtract(BigDecimal minuend, BigDecimal subtrahend) {
		return subtract(minuend, DEFAULT_SCALE, subtrahend);
	}

	public static BigDecimal subtract(Object minuend, int scale, Object... numbers) {
		return subtract(new BigDecimal(String.valueOf(minuend)), scale, Arrays.asList(numbers).stream().flatMap(n -> {
			if (n == null) {
				return Stream.of(BigDecimal.ZERO);
			}
			return Stream.of(new BigDecimal(String.valueOf(n)));
		}).collect(Collectors.toList()).toArray(new BigDecimal[0]));
	}

	public static BigDecimal subtract(BigDecimal minuend, int scale, BigDecimal... bigDecimals) {
		BigDecimal res = minuend;
		for (int i = 0, len = bigDecimals.length; i < len; i++) {
			if (bigDecimals[i] != null)
				res = res.subtract(bigDecimals[i]);
		}
		return res.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * 两个float类型的数相除。
	 *
	 * @param denominator 分母
	 * @param divisor 分子
	 * @param scale 计算结果精度
	 * @return 计算结果，float类型
	 */
	public static float devide(float denominator, float divisor, int scale) throws UnsupportedParameterException {
		BigDecimal de = BigDecimal.valueOf(denominator);
		BigDecimal di = BigDecimal.valueOf(divisor);
		return devide(de, di, scale).floatValue();
	}

	/**
	 * 两个double类型的数相除。
	 *
	 * @param denominator 分母
	 * @param divisor 分子
	 * @param scale 计算结果精度
	 * @return 计算结果，double类型
	 */
	public static double devide(double denominator, double divisor, int scale) throws UnsupportedParameterException {
		BigDecimal de = BigDecimal.valueOf(denominator);
		BigDecimal di = BigDecimal.valueOf(divisor);
		return devide(de, di, scale).doubleValue();
	}

	public static BigDecimal devide(Object denominator, Object divisor, int scale) throws UnsupportedParameterException {
		return devide(new BigDecimal(String.valueOf(denominator)), new BigDecimal(String.valueOf(divisor)), scale);
	}

	public static BigDecimal devide(BigDecimal denominator, BigDecimal divisor, int scale) throws UnsupportedParameterException {
		if ((new BigDecimal(0)).compareTo(divisor) == 0) {
			throw new UnsupportedParameterException("Devisor is 0");
		}
		return denominator.divide(divisor, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 两个float类型的数相乘。
	 *
	 * @param multiplicand1 乘数1
	 * @param multiplicand2 乘数2
	 * @param scale 计算结果精度
	 * @return the float
	 */
	public static float mutiply(float multiplicand1, float multiplicand2, int scale) {
		BigDecimal one = BigDecimal.valueOf(multiplicand1);
		BigDecimal two = BigDecimal.valueOf(multiplicand2);
		return one.multiply(two).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 两个double类型的数相乘。
	 *
	 * @param multiplicand1 乘数1
	 * @param multiplicand2 乘数2
	 * @param scale 计算结果精度
	 * @return the double
	 */
	public static double mutiply(double multiplicand1, double multiplicand2, int scale) {
		BigDecimal one = BigDecimal.valueOf(multiplicand1);
		BigDecimal two = BigDecimal.valueOf(multiplicand2);
		return one.multiply(two).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static BigDecimal mutiply(Object multiplicand1, Object multiplicand2) {
		return mutiply(multiplicand1, multiplicand2, DEFAULT_SCALE);
	}

	public static BigDecimal mutiply(Object multiplicand1, Object multiplicand2, int scale) {
		return mutiply(new BigDecimal(String.valueOf(multiplicand1)), new BigDecimal(String.valueOf(multiplicand2)), scale);
	}

	public static BigDecimal mutiply(BigDecimal multiplicand1, BigDecimal multiplicand2) {
		return mutiply(multiplicand1, multiplicand2, DEFAULT_SCALE);
	}

	public static BigDecimal mutiply(BigDecimal multiplicand1, BigDecimal multiplicand2, int scale) {
		return multiplicand1.multiply(multiplicand2).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 根据指定精度格式化给定的数字。
	 *
	 * @param f 给定的float对象
	 * @param scale 计算结果精度
	 * @return 格式化后的数字
	 */
	public static float format(float f, int scale) {
		return BigDecimal.valueOf(f).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 根据指定精度格式化给定的数字。
	 *
	 * @param d 给定的double对象
	 * @param scale 计算结果精度
	 * @return 格式化后的数字
	 */
	public static double format(double d, int scale) {
		return BigDecimal.valueOf(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 将给定的float数字根据scale精度格式化成字符串。
	 *
	 * @param f 给定的float对象
	 * @param scale 计算结果精度
	 * @return 格式化后的string对象
	 */
	public static String formatString(float f, int scale) {
		return BigDecimal.valueOf(f).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 将给定的double数字根据scale精度格式化成字符串。
	 *
	 * @param d 给定的double对象
	 * @param scale 计算结果精度
	 * @return 格式化后的string对象
	 */
	public static String formatString(double d, int scale) {
		return BigDecimal.valueOf(d).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 返回 integer 属性.
	 *
	 * @param value the value
	 * @param defaultValue the default value
	 * @return integer 属性
	 */
	public static int getInteger(Object value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			if (value instanceof Number) {
				return ((Number) value).intValue();
			} else if (value instanceof String) {
				String stringValue = (String) value;
				return StringUtils.isNull(stringValue) ? defaultValue : Integer.parseInt(stringValue);
			} else {
				return defaultValue;
			}
		}
	}

	/**
	 * 返回 long 属性.
	 *
	 * @param value the value
	 * @param defaultValue the default value
	 * @return long 属性
	 */
	public static long getLong(Object value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			if (value instanceof Number) {
				return ((Number) value).longValue();
			} else if (value instanceof String) {
				String stringValue = (String) value;
				return StringUtils.isNull(stringValue) ? defaultValue : Long.parseLong(stringValue);
			} else {
				return defaultValue;
			}
		}
	}

	/**
	 * 返回 double 属性.
	 *
	 * @param value the value
	 * @param defaultValue the default value
	 * @return double 属性
	 */
	public static double getDouble(Object value, double defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			} else if (value instanceof String) {
				String stringValue = (String) value;
				return StringUtils.isNull(stringValue) ? defaultValue : Double.parseDouble(stringValue);
			} else {
				return defaultValue;
			}
		}
	}

	public static int min(int... array) {
		validateArray(array);
		int min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	public static int max(int... array) {
		validateArray(array);
		int max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}

	/**
	 * Checks if the specified array is neither null nor empty.
	 *
	 * @param array  the array to check
	 * @throws IllegalArgumentException if {@code array} is either {@code null} or empty
	 */
	private static void validateArray(final Object array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (Array.getLength(array) == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtils.sum(2,20,null,30));
	}
}
