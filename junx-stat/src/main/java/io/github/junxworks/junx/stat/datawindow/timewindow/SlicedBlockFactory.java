/*
 ***************************************************************************************
 * 
 * @Title:  SlicedBlockFactory.java   
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
 * SlicedBlock 对象的工厂类.
 *
 * @ClassName:  SlicedBlockFactory
 * @author: Michael
 * @date:   2017-5-19 16:19:06
 * @since:  v1.0
 */
public interface SlicedBlockFactory{
	
	/**
	 * 生产每个函数自身的时间窗口切分块，每个切分块内部的累计算法由函数自身去实现
	 *
	 * @param dataWindow 时间窗口对象
	 * @return the sliced block
	 */
	public SlicedBlock createBlock(SlicedTimeBasedDataWindow dataWindow);
}
