/*
 ***************************************************************************************
 * 
 * @Title:  TimeBasedDataWindow.java   
 * @Package io.github.junxworks.junx.stat.datawindow.timewindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:28   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow;

/**
 * 基于事件时间撮的数据窗口接口定义.
 *
 * @author: michael
 * @date:   2017-5-17 15:36:00
 * @since:  v1.0
 */
public interface TimeBasedDataWindow extends DataWindow {

	/**
	 * 返回领跑时间
	 *
	 * @return 领跑时间
	 */
	public long getPacemakerTime();

	/**
	 * 设置领跑时间
	 *
	 * @param timestamp 领跑时间
	 */
	public void setPacemakerTime(long pacemakerTime);

	/**
	 * 返回过期时间点
	 *
	 * @return 过期时间点
	 */
	public long getExpireTimePoint();

	/**
	 * 设置过期时间点
	 *
	 * @param timestamp 过期时间点
	 */
	public void setExpireTimePoint(long expireTime);
}
