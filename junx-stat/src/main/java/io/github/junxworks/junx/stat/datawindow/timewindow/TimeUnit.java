/*
 ***************************************************************************************
 * 
 * @Title:  TimeUnit.java   
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
 * 时间单位枚举类.
 *
 * @author: Michael
 * @date:   2017-5-19 16:23:02
 * @since:  v1.0
 */
public enum TimeUnit {

	/** 秒. */
	second("秒",1000, new SecondSliceStrategy()),
	
	/** 分. */
	minute("分",60*second.getMillis(), new MuniteSliceStrategy()),
	
	/** 时. */
	hour("时",60*minute.getMillis(),new HourSliceStrategy()),
	
	/** 日. */
	day("日",24*hour.getMillis() ,new DaySliceStrategy()),
	
	/** 周. */
	week("周",7*day.getMillis() ,new WeekSliceStrategy()), 
	
	/** 月. */
	month("月", -1,new MonthSliceStrategy()),
	
	/** 年. */
	year("年",-1,new YearSliceStrategy()), 
	
	/** 永久. */
	eternal("永久",-1,new EternalSliceStrategy());

	/** 当前枚举对象名称. */
	private String name;
	
	/** 当前枚举对象1个单位的时间跨度，单位是毫秒。月、年、永久为-1. */
	private long millis;
	
	/** 切分策略，每个时间单位上有自己的切分策略. */
	private SliceStrategy strategy;

	/**
	 * 构造一个新的对象.
	 *
	 * @param name the name
	 * @param millis the millis
	 * @param strategy the strategy
	 */
	private TimeUnit(String name,long millis, SliceStrategy strategy) {
		this.name = name;
		this.strategy = strategy;
		this.millis=millis;
	}

	public String getName() {
		return name;
	}

	public SliceStrategy getStrategy() {
		return strategy;
	}

	public long getMillis() {
		return millis;
	}

}
