/*
 ***************************************************************************************
 * 
 * @Title:  AvgBlockFactory.java   
 * @Package io.github.junxworks.junx.stat.function.avg   
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
package io.github.junxworks.junx.stat.function.avg;

import io.github.junxworks.junx.stat.datawindow.DataWindow;
import io.github.junxworks.junx.stat.datawindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory;

/**
 * AvgBlock 对象的工厂类.
 *
 * @ClassName:  AvgBlockFactory
 * @author: Michael
 * @date:   2017-7-11 15:35:03
 * @since:  v1.0
 */
public class AvgBlockFactory implements SlicedBlockFactory {
	
	@Override
	public SlicedBlock createBlock(DataWindow dataWindow) {
		return new AvgSlicedBlock();
	}

}
