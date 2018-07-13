/*
 ***************************************************************************************
 * 
 * @Title:  ProbDistSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.probdist   
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
package io.github.junxworks.junx.stat.function.probdist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 概率分布函数累计块
 *
 * @ClassName:  ProbDistSlicedBlock
 * @author: michael
 * @date:   2017-11-22 16:55:07
 * @since:  v1.0
 */
public class ProbDistSlicedBlock extends BaseSlicedBlock {

	protected Map<String, ProbObject> values = new HashMap<>();

	/**
	 * Compose.
	 *
	 * @param newData the new data
	 * @throws Exception the exception
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			for (DataBundle data : newData) {
				if (data.getValue() == null) {
					continue;
				}
				String key = data.getValue().toString();
				ProbObject probObj = values.get(key);
				if (probObj != null) {
					probObj.add();
				} else {
					probObj = new ProbObject(key);
				}
				values.put(key, probObj);
			}
		}
	}

	/**
	 * To bytes.
	 *
	 * @return the byte[]
	 * @throws Exception the exception
	 */
	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		ByteContainer bc = new ByteContainer();
		bc.writeShort((short) values.size());
		for (Map.Entry<String, ProbObject> entry : values.entrySet()) {
			bc.writeString(entry.getKey());
			ProbObject po = entry.getValue();
			bc.writeString(po.getKey());
			bc.writeInt(po.getCount());
		}
		return bc.toBytes();
	}

	/**
	 * Read bytes.
	 *
	 * @param bytes the bytes
	 * @throws Exception the exception
	 */
	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		short size = bc.readShort();
		values = new HashMap<>(size * 10 / 7);
		for (int i = 0; i < size; i++) {
			String key = bc.readString();
			String pKey = bc.readString();
			int count = bc.readInt();
			ProbObject po = new ProbObject(pKey);
			po.setCount(count);
			values.put(key, po);
		}
	}

	@Override
	public Collection<?> getData() {
		return values.values();
	}
}
