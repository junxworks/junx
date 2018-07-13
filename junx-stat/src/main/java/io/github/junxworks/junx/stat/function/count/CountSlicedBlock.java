/*
 ***************************************************************************************
 * 
 * @Title:  CountSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.count   
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
package io.github.junxworks.junx.stat.function.count;

import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.core.util.NumberUtils;

/**
 * 计数函数使用的切分块，累计某个时间单位内的统计值
 * 
 *
 * @author: Michael
 * @date:   2017-5-18 17:18:36
 * @since:  v1.0
 */
public class CountSlicedBlock extends BaseSlicedBlock {

	/**
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			int count = 0;
			if (!values.isEmpty()) {
				count = NumberUtils.getInteger(values.get(0), 0);
			}
			count += newData.length;
			if (values.isEmpty()) {
				values.add(count);
			} else {
				values.set(0, count);
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer(4);
		return bc.writeInt(NumberUtils.getInteger(values.get(0), 0)).toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		values.add(bc.readInt());
	}

}
