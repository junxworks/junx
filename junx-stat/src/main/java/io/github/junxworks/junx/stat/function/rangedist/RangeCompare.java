/*
 ***************************************************************************************
 * 
 * @Title:  RangeCompare.java   
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

/**
 * 用于区间分布的区间比较，目前只有时间和值两种区间划分
 *
 * @ClassName:  RangeCompareObject
 * @author: Michael
 * @date:   2018-5-18 15:58:44
 * @since:  v1.0
 */
public class RangeCompare {

	/** time. */
	private long time;

	/** value. */
	private Object value;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
