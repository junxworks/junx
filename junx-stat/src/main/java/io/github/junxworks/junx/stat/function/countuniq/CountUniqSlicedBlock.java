/*
 ***************************************************************************************
 * 
 * @Title:  CountUniqSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.countuniq   
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
package io.github.junxworks.junx.stat.function.countuniq;

import io.github.junxworks.junx.core.util.ByteUtils;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 唯一计数函数使用的切分块，累计某个时间单位内的统计值
 * 
 *
 * @author: Michael
 * @date:   2017-5-18 17:18:36
 * @since:  v1.0
 */
public class CountUniqSlicedBlock extends BaseSlicedBlock {

	/**
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			for (int i = 0, len = newData.length; i < len; i++) {
				String value = String.valueOf(newData[i].getValue());
				if (!values.contains(value)) {//唯一计数实现的地方，当且仅当当前模块不包含指定值的时候，才加入到当前的list中
					values.add(value);
				}
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		return ByteUtils.list2bytes(values);
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		values.addAll(ByteUtils.bytes2List(bytes));
	}

}
