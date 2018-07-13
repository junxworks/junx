/*
 ***************************************************************************************
 * 
 * @Title:  SumSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.sum   
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
package io.github.junxworks.junx.stat.function.sum;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;


/**
 * 求和函数使用的切分块
 *
 * @author: michael
 * @date:   2017-5-18 17:18:36
 * @since:  v1.0
 */
public class SumSlicedBlock extends BaseSlicedBlock {

	@Override
	public void compose(DataBundle... newData) throws Exception {
		Object originalValue;
		if (values.isEmpty()) {
			originalValue = 0;
		} else {
			originalValue = values.get(0);
		}
		BigDecimal oValue = NumberUtils.createBigDecimal(originalValue.toString());
		if (newData != null) {
			for (int i = 0, len = newData.length; i < len; i++) {
				oValue = oValue.add(NumberUtils.createBigDecimal(String.valueOf(newData[i].getValue())));
			}
			String value = oValue.toString();
			if (values.isEmpty()) {
				values.add(value);
			} else {
				values.set(0, value);
			}
		}
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.Serializable#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer(8);
		bc.writeDouble(NumberUtils.createBigDecimal(values.get(0).toString()).doubleValue());
		return bc.toBytes();
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.Serializable#readBytes(byte[])
	 */
	@Override
	public void readBytes(byte[] bytes) {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		values.add(String.valueOf(bc.readDouble()));
	}
}
