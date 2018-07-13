/*
 ***************************************************************************************
 * 
 * @Title:  CountEqualObject.java   
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

import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.stat.function.KeyCount;

/**
 * 用于存放唯一计数统计值的对象，每个统计目标值一个对象.
 *
 * @author: Michael
 * @date:   2017-6-1 15:35:43
 * @since:  v1.0
 */
public class CountEqualObject implements KeyCount{

	/** 统计目标值. */
	private String key;

	/** 次数. */
	private int count = 1;
	
	public CountEqualObject() {
	}

	public CountEqualObject(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		count++;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof CountEqualObject) && StringUtils.notNull(((CountEqualObject) obj).getKey()) && ((CountEqualObject) obj).getKey().equals(key);
	}

}
