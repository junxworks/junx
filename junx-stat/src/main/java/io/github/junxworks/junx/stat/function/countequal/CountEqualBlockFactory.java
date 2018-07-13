/*
 ***************************************************************************************
 * 
 * @Title:  CountEqualBlockFactory.java   
 * @Package io.github.junxworks.junx.stat.function.countequal   
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
package io.github.junxworks.junx.stat.function.countequal;

import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlockFactory;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedTimeBasedDataWindow;

/**
 * CountEqualBlock 对象的工厂类.
 *
 * @ClassName:  SumSlicedBlockFactory
 * @author: Michael
 * @date:   2017-5-19 16:18:55
 * @since:  v1.0
 */
public class CountEqualBlockFactory implements SlicedBlockFactory {

	/**
	 * @see io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlockFactory#createBlock()
	 */
	@Override
	public SlicedBlock createBlock(SlicedTimeBasedDataWindow dataWindow) {
		return new CountEqualSlicedBlock();
	}

}
