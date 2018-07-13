/*
 ***************************************************************************************
 * 
 * @Title:  ExpirableObject.java   
 * @Package io.github.junxworks.junx.stat.datawindow.timewindow   
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
package io.github.junxworks.junx.stat.datawindow.timewindow;

/**
 * 定义了一个可过期的数据对象，
 *
 * @author: Michael
 * @date:   2017-5-15 16:43:03
 * @since:  v1.0
 */
public interface ExpirableObject {

	/**
	 * 获得生成时间.
	 *
	 * @return creates the time 属性
	 */
	public long getCreateTime();

	/**
	 * 返回过期时间属性.
	 *
	 * @return 过期时间戳
	 */
	public long getExpireTime();

	/**
	 * 设置过期时间戳属性.
	 *
	 * @param timestamp 过期时间戳
	 */
	public void setExpireTime(long timestamp);

	/**
	 * 根据给的的时间戳，判断当前对象是否过期
	 *
	 * @param timestamp 给定时间戳
	 * @return 如果当前对象基于给定的时间戳，判断出已经过期，那么返回true，否则返回false
	 */
	public boolean isExpired(long timestamp);
}
