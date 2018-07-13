/*
 ***************************************************************************************
 * 
 * @Title:  TimeWindowDefinition.java   
 * @Package io.github.junxworks.junx.stat.datawindow.timewindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:30   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow.timewindow;

/**
 * 时间窗口的属性定义文件，定义了时间窗口的单位和周期
 *
 * @author: Michael
 * @date:   2017-5-17 16:19:00
 * @since:  v1.0
 */
public class TimeWindowDefinition {

	/** 时间单位. */
	private TimeUnit unit;

	/** 周期. */
	private int interval;

	public TimeWindowDefinition(TimeUnit unit, int interval) {
		this.unit = unit;
		this.interval = interval;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
