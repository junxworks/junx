/*
 ***************************************************************************************
 * 
 * @Title:  SliceStrategy.java   
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

import io.github.junxworks.junx.stat.datawindow.SlicedBlock;

/**
 * 时间窗口切分策略，对一个大的时间窗口进行切分，切分成小的模块
 *
 * @author: Michael
 * @date:   2017-5-15 17:04:06
 * @since:  v1.0
 */
public interface SliceStrategy {
	
	/**
	 * 根据指定时间窗口的定义对象，和当前事件的发生时间，切分指定时间窗口
	 *
	 * @param dataWindow 时间窗口
	 * @param eventTimestamp 事件发生时间
	 * @return 切分块
	 * @throws Exception the exception
	 */
	public SlicedBlock slice(SlicedTimeBasedDataWindow dataWindow, long eventTimestamp) throws Exception;
}
