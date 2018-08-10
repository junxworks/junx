/*
 ***************************************************************************************
 * 
 * @Title:  StatDefinition.java   
 * @Package io.github.junxworks.junx.stat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-8-10 18:03:52   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat;

import io.github.junxworks.junx.stat.datawindow.DataWindowConstants;

/**
 * 统计必须使用的统计定义接口，实现类由外部系统自己实现。
 *
 * @ClassName: StatisticsDefinition
 * @author: Michael
 * @date: 2018-5-14 15:22:48
 * @since: v1.0
 */
public class StatDefinition {

	/** 数据窗口类型，默认是时间窗口. */
	private int dataWindowType = DataWindowConstants.WIN_TYPE_TIME;

	/** 数据窗口时间单位，如果是时间窗口的话，这个单位必填. 参考 {@link io.github.junxworks.junx.stat.datawindow.timewindow.TimeUnit}*/
	private String dataWindowTimeUnit;

	/** 数据窗口大小. */
	private int dataWindowSize;

	/** 统计函数名. */
	private String statFunction;

	/** 统计函数附加条件，可以用于区间分布等统计函数. */
	private String statFunctionAddition;

	/** 数据窗口的优先级类型，优先级数据窗口用到，参考 {@link io.github.junxworks.junx.stat.datawindow.prioritywindow.Priorities} . */
	private String dataWindowPriorityType;

	public int getDataWindowType() {
		return dataWindowType;
	}

	public void setDataWindowType(int dataWindowType) {
		this.dataWindowType = dataWindowType;
	}

	public String getDataWindowTimeUnit() {
		return dataWindowTimeUnit;
	}

	public void setDataWindowTimeUnit(String dataWindowTimeUnit) {
		this.dataWindowTimeUnit = dataWindowTimeUnit;
	}

	public int getDataWindowSize() {
		return dataWindowSize;
	}

	public void setDataWindowSize(int dataWindowSize) {
		this.dataWindowSize = dataWindowSize;
	}

	public String getStatFunction() {
		return statFunction;
	}

	public void setStatFunction(String statFunction) {
		this.statFunction = statFunction;
	}

	public String getStatFunctionAddition() {
		return statFunctionAddition;
	}

	public void setStatFunctionAddition(String statFunctionAddition) {
		this.statFunctionAddition = statFunctionAddition;
	}

	public String getDataWindowPriorityType() {
		return dataWindowPriorityType;
	}

	public void setDataWindowPriorityType(String dataWindowPriorityType) {
		this.dataWindowPriorityType = dataWindowPriorityType;
	}

}
