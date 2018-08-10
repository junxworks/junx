/*
 ***************************************************************************************
 * 
 * @Title:  EternalSliceStrategy.java   
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

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.stat.datawindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory;

/**
 * 所有基于单位时间切分策略的基类
 *
 * @author: Michael
 * @date:   2017-5-19 15:14:52
 * @since:  v1.0
 */
public class EternalSliceStrategy implements SliceStrategy {

	/**
	 * @see SliceStrategy#slice(io.github.junxworks.junx.stat.datawindow.TimeBasedDataWindow)
	 */
	@Override
	public SlicedBlock slice(SlicedTimeBasedDataWindow dataWindow, long eventTimestamp) {
		SlicedBlockFactory factory = dataWindow.getBlockFactory();
		if (factory == null) {
			throw new BaseRuntimeException("The SlicedBlockFactory of SlicedTimeBasedDataWindow can't be null.");
		}
		return factory.createBlock(dataWindow);
	}
}
