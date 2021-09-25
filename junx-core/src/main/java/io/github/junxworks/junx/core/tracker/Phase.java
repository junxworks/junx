/*
 ***************************************************************************************
 * 
 * @Title:  Phase.java   
 * @Package io.github.junxworks.junx.core.tracker   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.tracker;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 处理阶段，或者步骤
 *
 * @ClassName:  Phase
 * @author: Michael
 * @date:   2018-6-11 11:37:51
 * @since:  v1.0
 */
public class Phase {

	/** 阶段索引. */
	@JSONField(serialize = false)
	private int index;

	/** 阶段名称. */
	@JSONField(ordinal = 0)
	private String name;

	/** 时间偏移量. */
	@JSONField(ordinal = 1)
	private long timeOffset;

	/** 阶段附加信息. */
	@JSONField(ordinal = 2)
	private String additional;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

}
