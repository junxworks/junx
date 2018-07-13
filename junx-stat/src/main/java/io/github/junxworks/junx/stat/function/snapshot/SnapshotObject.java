/*
 ***************************************************************************************
 * 
 * @Title:  SnapshotObject.java   
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

/**
 * 存储快照的对象
 *
 * @ClassName:  SnapshotObject
 * @author: michael
 * @date:   2017-11-19 16:04:06
 * @since:  v1.0
 */
public class SnapshotObject {

	/** timestamp. */
	private long timestamp;

	/** value. */
	private String value;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
