/*
 ***************************************************************************************
 * 
 * @Title:  BaseSlicedBlock.java   
 * @Package io.github.junxworks.junx.stat.function   
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
package io.github.junxworks.junx.stat.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.timewindow.TimeWindowDefinition;

/**
 * 切分块的基类
 *
 * @ClassName:  BaseSlicedBlock
 * @author: michael
 * @date:   2017-11-13 10:08:56
 * @since:  v1.0
 */
public abstract class BaseSlicedBlock implements SlicedBlock {

	/** 本对象过期时间 */
	protected long expireTime;

	/** 过期时间点. */
	protected long expireTimePoint;

	/** 领跑时间. */
	protected long pacemakerTime;

	/** 时间窗口定义，包含单位，周期等. */
	protected TimeWindowDefinition definition;

	/** values. */
	protected List<Object> values = new ArrayList<>();

	@Override
	public long getCreateTime() {
		return 0;
	}

	@Override
	public long getExpireTime() {
		return expireTime;
	}

	@Override
	public void setExpireTime(long timestamp) {
		this.expireTime = timestamp;
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.datawindow.timewindow.ExpirableObject#isExpired(long)
	 */
	@Override
	public boolean isExpired(long timestamp) {
		return getExpireTime() <= timestamp; //等于也算过期
	}

	@Override
	public TimeWindowDefinition getDefinition() {
		return definition;
	}

	@Override
	public void setDefinition(TimeWindowDefinition definition) {
		this.definition = definition;
	}

	@Override
	public long getPacemakerTime() {
		return pacemakerTime;
	}

	@Override
	public void setPacemakerTime(long pacemakerTime) {
		this.pacemakerTime = pacemakerTime;
		this.expireTime = pacemakerTime;
	}

	@Override
	public long getExpireTimePoint() {
		return expireTimePoint;
	}

	@Override
	public void setExpireTimePoint(long expireTime) {
		this.expireTimePoint = expireTime;
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#extractData(java.lang.Object[])
	 */
	@Override
	public Collection<?> extractData(StatContext context) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<?> getData() {
		return values;
	}

	@Override
	public void setData(Collection<?> data) {
		if (values.size() <= 0) {
			values.addAll(data);
		}
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String storageType() {
		throw new UnsupportedOperationException();
	}
}
