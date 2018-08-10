/*
 ***************************************************************************************
 * 
 * @Title:  RangeDistBlockFactory.java   
 * @Package io.github.junxworks.junx.stat.function.rangedist   
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
package io.github.junxworks.junx.stat.function.rangedist;

import io.github.junxworks.junx.stat.datawindow.DataWindow;
import io.github.junxworks.junx.stat.datawindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory;

/**
 * BinDistRangeBlock 对象的工厂类.
 *
 * @ClassName:  BinDistRangeBlockFactory
 * @author: Michael
 * @date:   2017-7-14 15:34:00
 * @since:  v1.0
 */
public class RangeDistBlockFactory implements SlicedBlockFactory {

	/**   
	 * <p>Title: createBlock</p>   
	 * <p>Description: </p>   
	 * @param dataWindow
	 * @return   
	 * @see io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory#createBlock(io.github.junxworks.junx.stat.datawindow.timewindow.DataWindow)
	 */
	@Override
	public SlicedBlock createBlock(DataWindow dataWindow) {
		return new RangeDistSlicedBlock();
	}

}
