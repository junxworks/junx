/*
 ***************************************************************************************
 * 
 * @Title:  RangeDistSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.rangedist   
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
package io.github.junxworks.junx.stat.function.rangedist;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;
import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 区间分布切分块
 *
 * @ClassName:  BinDistRangeSlicedBlock
 * @author: Michael
 * @date:   2017-7-14 15:33:59
 * @since:  v1.0
 */
public class RangeDistSlicedBlock extends BaseSlicedBlock {
	protected Map<String, RangeStat> values = new HashMap<>();

	/**   
	 * <p>Title: compose</p>   
	 * <p>Description: </p>   
	 * @param newData
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(Object[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			String funcAddtion = newData[0].getFunctionAddition();
			if (StringUtils.isNull(funcAddtion)) {
				throw new UnsupportedParameterException("Function addition can not be null for function RangeDistribution.");
			}
			List<RangeObject> rdfs = RangeDefinitionManager.getRangeDefs(funcAddtion);
			for (int x = 0, _len = newData.length; x < _len; x++) {
				DataBundle data = newData[x];
				for (int i = 0, len = rdfs.size(); i < len; i++) {
					RangeObject rdf = rdfs.get(i);
					RangeCompare rco = new RangeCompare();
					rco.setTime(data.getTimestamp());
					rco.setValue(data.getValue());
					if (rdf.isIn(rco)) {
						String range = rdf.getRange();
						RangeStat ro = values.get(range);
						if (ro == null) {
							values.put(range, new RangeStat(range));
						} else {
							ro.add();
						}
						break;
					}
				}
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
		for (Map.Entry<String, RangeStat> entry : values.entrySet()) {
			bc.writeString(entry.getKey());
			RangeStat po = entry.getValue();
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
			RangeStat po = new RangeStat(pKey);
			po.setCount(count);
			values.put(key, po);
		}
	}

	@Override
	public Collection<?> getData() {
		return values.values();
	}
}
