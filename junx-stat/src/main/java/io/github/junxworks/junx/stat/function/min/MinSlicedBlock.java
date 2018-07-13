/*
 ***************************************************************************************
 * 
 * @Title:  MinSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.min   
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
package io.github.junxworks.junx.stat.function.min;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 最小值切分块
 *
 * @ClassName:  MinSlicedBlock
 * @author: Michael
 * @date:   2017-7-11 15:38:19
 * @since:  v1.0
 */
public class MinSlicedBlock extends BaseSlicedBlock {

	/**   
	 * <p>Title: compose</p>   
	 * <p>Description: </p>   
	 * @param newData
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		BigDecimal minValue = null;
		if (!values.isEmpty()) {
			minValue = NumberUtils.createBigDecimal(values.get(0).toString());
		}
		if (newData != null) {
			BigDecimal tempValue = null;
			for (DataBundle timeValuePair : newData) {
				tempValue = NumberUtils.createBigDecimal(String.valueOf(timeValuePair.getValue()));
				if (minValue == null) {
					minValue = tempValue;
				} else if (minValue.compareTo(tempValue) > 0) {
					minValue = tempValue;
				}
			}
			if (values.isEmpty()) {
				values.add(minValue.doubleValue());
			} else {
				values.set(0, minValue.doubleValue());
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if(values.isEmpty()){
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
