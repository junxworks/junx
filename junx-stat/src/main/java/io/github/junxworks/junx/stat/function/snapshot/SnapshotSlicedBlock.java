/*
 ***************************************************************************************
 * 
 * @Title:  SnapshotSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function.snapshot   
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
package io.github.junxworks.junx.stat.function.snapshot;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.BaseSlicedBlock;

/**
 * 快照函数切分块.
 *
 * @ClassName:  SnapshotSlicedBlock
 * @author: Michael
 * @date:   2017-6-29 9:58:17
 * @since:  v1.0
 */
public class SnapshotSlicedBlock extends BaseSlicedBlock {

	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData != null) {
			SnapshotObject currentSnapshot = null;
			if (values.size() > 0) {
				currentSnapshot = (SnapshotObject) values.get(0);
			}
			for (int i = 0, len = newData.length; i < len; i++) {
				DataBundle data = newData[i];
				SnapshotObject snapshot = new SnapshotObject();
				snapshot.setTimestamp(data.getTimestamp());
				snapshot.setValue(String.valueOf(data.getValue()));
				if (currentSnapshot == null) {
					currentSnapshot = snapshot;
				} else {
					if (currentSnapshot.getTimestamp() < snapshot.getTimestamp()) {
						currentSnapshot = snapshot;
					}
				}
			}
			if (values.isEmpty()) {
				values.add(currentSnapshot);
			} else {
				values.set(0, currentSnapshot);
			}
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (values.isEmpty()) {
			return null;
		}
		SnapshotObject so = (SnapshotObject) values.get(0);
		ByteContainer bc = new ByteContainer(32);
		bc.writeLong(so.getTimestamp());
		bc.writeString(so.getValue());
		return bc.toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes == null) {
			return;
		}
		ByteContainer bc = new ByteContainer(bytes);
		SnapshotObject so = new SnapshotObject();
		so.setTimestamp(bc.readLong());
		so.setValue(bc.readString());
		values.add(so);
	}

}
