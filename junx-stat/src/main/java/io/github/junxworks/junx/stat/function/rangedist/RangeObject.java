/*
 ***************************************************************************************
 * 
 * @Title:  RangeObject.java   
 * @Package io.github.junxworks.junx.stat.function.rangedist   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function.rangedist;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 区间定义对象
 *
 * @ClassName: RangeDefinition
 * @author: Michael
 * @date: 2017-11-23 15:57:48
 * @since: v1.0
 */
public class RangeObject {

	private static final Logger log = LoggerFactory.getLogger(RangeObject.class);

	/** 区间比较类型：时间. */
	public static final int COMPARE_TYPE_TIME = 1;

	/** 区间比较类型：数值. */
	public static final int COMPARE_TYPE_VALUE = 2;

	/** 区间类型，参考区间类型常量 */
	private String type;

	/** 区间比较类型，如果是时间，则取时间撮，如果是数值，则比较数值大小. */
	private int compareType;

	/** 起值 */
	private double startValue;

	/** 左无穷大. */
	private boolean leftInfinity = false;

	/** 尾值，如果等于尾值，则计入下一个区间. */
	private double endValue;

	/** 右无穷大. */
	private boolean rightInfinity = false;

	private String range;

	/** 区间定义字符串. */
	private String funcAddtion;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCompareType() {
		return compareType;
	}

	public void setCompareType(int compareType) {
		this.compareType = compareType;
	}

	public void setStartValue(String startValue) {
		if (RangeDistributionConstants.RANGE_INFINITY.equals(startValue)) {
			leftInfinity = true;
		} else {
			this.startValue = Double.valueOf(startValue);
		}
	}

	public void setEndValue(String endValue) {
		if (RangeDistributionConstants.RANGE_INFINITY.equals(endValue)) {
			rightInfinity = true;
		} else {
			this.endValue = Double.valueOf(endValue);
		}
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getFuncAddtion() {
		return funcAddtion;
	}

	public void setFuncAddtion(String funcAddtion) {
		this.funcAddtion = funcAddtion;
	}

	/**
	 * 判断一个值是否在本区间内
	 *
	 * @param obj the obj
	 * @return 返回布尔值 in
	 */
	public boolean isIn(RangeCompare rco) {
		if (COMPARE_TYPE_TIME == compareType) {
			// 时间比较
			long timestamp = rco.getTime();
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(timestamp);
			if (RangeDistributionConstants.RANGE_TYPE_HOUR.equals(type)) {
				int hour = c.get(Calendar.HOUR_OF_DAY);
				return isIn(Double.valueOf(hour));
			} else if (RangeDistributionConstants.RANGE_TYPE_WEEK.equals(type)) {
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				return isIn(Double.valueOf(dayOfWeek));
			}
		} else if (COMPARE_TYPE_VALUE == compareType) {
			// 值比较
			Object value = rco.getValue();
			// 此处不再判断value值，有异常直接抛出
			return isIn(Double.valueOf(value.toString()));

		}
		return false;
	}

	private boolean isIn(double value) {
		if (leftInfinity && rightInfinity) {
			log.warn("Infinity range for function addtion {} is meaningless", funcAddtion);
			return true;
		} else if (leftInfinity) {// 无穷小判断，只要小于endValue即可
			return value < endValue ? true : false;
		} else if (rightInfinity) {// 无穷大判断，只要大于等于startValue即可
			return value >= startValue ? true : false;
		} else if (startValue <= value && value < endValue) {
			// 左闭右开，例如一个区间0-6，0这个value算区间内，6不算。区间内的值0<=value<6
			return true;
		}
		return false;
	}

}
