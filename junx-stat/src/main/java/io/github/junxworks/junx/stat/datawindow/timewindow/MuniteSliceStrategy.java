/*
 ***************************************************************************************
 * 
 * @Title:  MuniteSliceStrategy.java   
 * @Package io.github.junxworks.junx.stat.datawindow.timewindow   
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
package io.github.junxworks.junx.stat.datawindow.timewindow;

import io.github.junxworks.junx.core.util.DateUtils;

/**
 * 基于分钟的时间窗口切分策略，每个切分块就是1分钟
 *
 * @author: Michael
 * @date:   2017-5-17 17:55:02
 * @since:  v1.0
 */
public class MuniteSliceStrategy extends UnitBasedSliceStrategy {
	private static final String FORMAT = "yyyy-MM-dd HH:mm";

	@Override
	public void setExpireTimePoint(SlicedBlock block, long eventTimestamp) {
		block.setExpireTimePoint(DateUtils.parseDate(DateUtils.formatDate(eventTimestamp, FORMAT), FORMAT).getTime());//根据事件时间戳计算出当前block的过期时间点
	}

	@Override
	public void setPacemakerTime(SlicedBlock block) {
		block.setPacemakerTime(block.getExpireTimePoint() + TimeUnit.minute.getMillis() - 1); //少一毫秒
	}

}
