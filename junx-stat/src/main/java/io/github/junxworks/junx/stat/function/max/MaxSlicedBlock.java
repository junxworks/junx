/*
 ***************************************************************************************
 * 
 * @Title:  MaxSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.max   
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
package io.github.junxworks.junx.stat.function.max;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 最大值切分块
 *
 * @ClassName:  MaxSlicedBlock
 * @author: Michael
 * @date:   2017-7-11 15:34:03
 * @since:  v1.0
 */
public class MaxSlicedBlock extends BaseSlicedBlock {

	/**   
	 * <p>Title: compose</p>   
	 * <p>Description: </p>   
	 * @param newData
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		Object maxValue;
		if (values.isEmpty()) {
			maxValue = 0;
		} else {
			maxValue = values.get(0);
		}
		BigDecimal mValue = NumberUtils.createBigDecimal(maxValue.toString());
		if (newData != null) {
			BigDecimal tempValue = null;
			for (DataBundle data : newData) {
				tempValue = NumberUtils.createBigDecimal(String.valueOf(data.getValue()));
				if (mValue.compareTo(tempValue) < 0) {
					mValue = tempValue;
				}
			}
			if (values.isEmpty()) {
				values.add(mValue.doubleValue());
			} else {
				values.set(0, mValue.doubleValue());
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer(8);
		return bc.writeDouble(NumberUtils.toDouble(values.get(0).toString())).toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		values.add(bc.readDouble());
	}

}
