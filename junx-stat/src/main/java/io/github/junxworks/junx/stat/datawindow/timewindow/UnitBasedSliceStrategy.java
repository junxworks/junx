/*
 ***************************************************************************************
 * 
 * @Title:  UnitBasedSliceStrategy.java   
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

import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * 所有基于单位时间切分策略的基类
 *
 * @author: Michael
 * @date:   2017-5-19 15:14:52
 * @since:  v1.0
 */
public abstract class UnitBasedSliceStrategy  implements SliceStrategy {

	/**
	 * @see SliceStrategy#slice(io.github.junxworks.junx.stat.datawindow.TimeBasedDataWindow)
	 */
	@Override
	public SlicedBlock slice(SlicedTimeBasedDataWindow dataWindow, long eventTimestamp) {
		SlicedBlockFactory factory = dataWindow.getBlockFactory();
		if (factory == null) {
			throw new BaseRuntimeException("The SlicedBlockFactory of SlicedTimeBasedDataWindow can't be null.");
		}
		//计算
		SlicedBlock block = factory.createBlock(dataWindow);
		block.setDefinition(new TimeWindowDefinition(dataWindow.getDefinition().getUnit(), 1));
		//根据事件时间戳，计算当前block所在的时间区间
		setExpireTimePoint(block,eventTimestamp);
		setPacemakerTime(block);
		block.setExpireTime(block.getPacemakerTime());
		return block;
	}
	
	/**
	 *生成block的过期时间点
	 *
	 * @param block the block
	 * @param eventTimestamp the event timestamp
	 */
	public abstract void setExpireTimePoint(SlicedBlock block,long eventTimestamp);
	
	/**
	 * 设置block的领跑时间属性，不能跨越到下一个时间，例如单位是1秒的话，那么第一个block的宽度是0-999，第二个是1000-1999。
	 * 如果单位是1天的话，那么第一个block为2017-05-19 00:00:00.000-2017-05-19 23:59:59.999，不能跨度到下一天
	 *
	 * @param block the block
	 */
	public abstract void setPacemakerTime(SlicedBlock block);
	
}
