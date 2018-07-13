/*
 ***************************************************************************************
 * 
 * @Title:  ProbObject.java   
 * @Package io.github.junxworks.junx.stat.function.probdist   
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
package io.github.junxworks.junx.stat.function.probdist;

import io.github.junxworks.junx.stat.function.KeyCount;

/**
 * 概率分布函数累计使用对象
 *
 * @ClassName:  ProbeObject
 * @author: Michael
 * @date:   2017-11-22 16:57:47
 * @since:  v1.0
 */
public class ProbObject implements KeyCount{

	/** 键值. */
	private String key;

	/** 计数. */
	private int count = 1;

	public ProbObject(String key) {
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

	public void setCount(int count) {
		this.count = count;
	}

	public void add() {
		count++;
	}

}
