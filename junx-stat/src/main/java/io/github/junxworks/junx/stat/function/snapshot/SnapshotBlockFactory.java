/*
 ***************************************************************************************
 * 
 * @Title:  SnapshotBlockFactory.java   
 * @Package io.github.junxworks.junx.stat.function.snapshot   
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
package io.github.junxworks.junx.stat.function.snapshot;

import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlockFactory;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedTimeBasedDataWindow;

/**
 * SnapshotSlicedBlock 对象的工厂类.
 *
 * @ClassName:  SnapshotSlicedBlockFactory
 * @author: Michael
 * @date:   2017-6-29 9:58:10
 * @since:  v1.0
 */
public class SnapshotBlockFactory implements SlicedBlockFactory{

	@Override
	public SlicedBlock createBlock(SlicedTimeBasedDataWindow dataWindow) {
		return new SnapshotSlicedBlock();
	}

}
