/*
 ***************************************************************************************
 * 
 * @Title:  StatusSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.status   
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
package io.github.junxworks.junx.stat.function.status;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 状态函数的切分块.
 *
 * @ClassName:  StatusSlicedBlock
 * @author: Michael
 * @date:   2017-6-29 14:16:06
 * @since:  v1.0
 */
public class StatusSlicedBlock extends BaseSlicedBlock {

	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (values.isEmpty()) {
			values.add(1);
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer(1);
		return bc.writeByte((byte) 1).toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		values.add(1);
	}

}
