/*
 ***************************************************************************************
 * 
 * @Title:  CountEqualSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.countequal   
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
package io.github.junxworks.junx.stat.function.countequal;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 相同计数函数使用的切分块，累计某个时间单位内的统计值
 * 
 *
 * @author: Michael
 * @date:   2017-5-18 17:18:36
 * @since:  v1.0
 */
public class CountEqualSlicedBlock extends BaseSlicedBlock {
	/**
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			for (int i = 0, len = newData.length; i < len; i++) {
				String value = String.valueOf(newData[i].getValue());//统计目标值
				CountEqualObject _ceo = new CountEqualObject(value);
				if (values.contains(_ceo)) {
					for (Object obj : values) {
						CountEqualObject ceo = (CountEqualObject) obj;
						if (_ceo.equals(ceo)) {
							ceo.addCount();
							break;
						}
					}
				} else {
					values.add(_ceo);
				}
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer();
		bc.writeInt(values.size());
		for (int i = 0, len = values.size(); i < len; i++) {
			CountEqualObject ceo = (CountEqualObject) values.get(i);
			bc.writeString(ceo.getKey());
			bc.writeInt(ceo.getCount());
		}
		return bc.toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		int size = bc.readInt();
		for (int i = 0; i < size; i++) {
			CountEqualObject ceo = new CountEqualObject();
			ceo.setKey(bc.readString());
			ceo.setCount(bc.readInt());
			values.add(ceo);
		}
	}

}
