/*
 ***************************************************************************************
 * 
 * @Title:  AvgSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.avg   
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
package io.github.junxworks.junx.stat.function.avg;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 平均数函数切分块
 *
 * @ClassName:  AvgSlicedBlock
 * @author: Michael
 * @date:   2017-7-11 15:36:43
 * @since:  v1.0
 */
public class AvgSlicedBlock extends BaseSlicedBlock {

	/**   
	 * <p>Title: compose</p>   
	 * <p>Description: </p>   
	 * @param newData
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		AvgObject avgObject = null;
		if (values.isEmpty()) {
			avgObject = new AvgObject(0, 0);
		} else {
			avgObject = (AvgObject) values.get(0);
		}
		if (newData != null) {
			BigDecimal sumValue = BigDecimal.valueOf(avgObject.getSum());
			for (DataBundle data : newData) {
				sumValue = sumValue.add(NumberUtils.createBigDecimal(String.valueOf(data.getValue())));
			}
			avgObject.setSum(sumValue.doubleValue());
			avgObject.setCount(avgObject.getCount() + newData.length);
			if (values.isEmpty()) {
				values.add(avgObject);
			} else {
				values.set(0, avgObject);
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		AvgObject ao = (AvgObject) values.get(0);
		ByteContainer bc = new ByteContainer(12);
		bc.writeInt(ao.getCount());
		bc.writeDouble(ao.getSum());
		return bc.toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		AvgObject ao = new AvgObject();
		ao.setCount(bc.readInt());
		ao.setSum(bc.readDouble());
		values.add(ao);
	}
}
