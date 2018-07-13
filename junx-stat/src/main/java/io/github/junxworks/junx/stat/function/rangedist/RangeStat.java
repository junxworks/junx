/*
 ***************************************************************************************
 * 
 * @Title:  RangeStat.java   
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

import io.github.junxworks.junx.stat.function.KeyCount;

/**
 * 用来记录区间数据的对象
 *
 * @ClassName:  RangeObject
 * @author: Michael
 * @date:   2017-11-24 15:42:25
 * @since:  v1.0
 */
public class RangeStat implements KeyCount {

	/** 区间key. */
	private String range;

	/** 计数. */
	private int count = 1;

	public RangeStat(String range) {
		this.range = range;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void add() {
		count++;
	}

	@Override
	public String getKey() {
		return range;
	}

}
