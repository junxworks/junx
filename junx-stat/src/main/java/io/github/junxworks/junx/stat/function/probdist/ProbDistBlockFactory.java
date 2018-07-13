/*
 ***************************************************************************************
 * 
 * @Title:  ProbDistBlockFactory.java   
 * @Package io.github.junxworks.junx.stat.function.probdist   
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
package io.github.junxworks.junx.stat.function.probdist;

import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlockFactory;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedTimeBasedDataWindow;

/**
 * 概率分布累计块 对象的工厂类.
 *
 * @ClassName:  BinDistBlockFactory
 * @author: Michael
 * @date:   2017-6-26 15:04:21
 * @since:  v1.0
 */
public class ProbDistBlockFactory implements SlicedBlockFactory{

	@Override
	public SlicedBlock createBlock(SlicedTimeBasedDataWindow dataWindow) {
		return new ProbDistSlicedBlock();
	}

}
