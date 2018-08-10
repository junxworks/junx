/*
 ***************************************************************************************
 * 
 * @Title:  YearSliceStrategy.java   
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

import java.util.Date;

import io.github.junxworks.junx.core.util.DateUtils;
import io.github.junxworks.junx.stat.datawindow.SlicedBlock;

/**
 * 基于年的时间窗口切分策略，每个切分块就是1年
 *
 * @author: Michael
 * @date:   2017-5-17 17:55:02
 * @since:  v1.0
 */
public class YearSliceStrategy extends UnitBasedSliceStrategy {

	private static final String FORMAT = "yyyy";

	@Override
	public void setExpireTimePoint(SlicedBlock block, long eventTimestamp) {
		String year = DateUtils.formatDate(eventTimestamp, FORMAT);
		Date firstDay = DateUtils.parseDate(year, FORMAT);//每月第一天的0点
		block.setExpireTimePoint(firstDay.getTime());//根据事件时间戳计算出当前block的过期时间点
	}

	@Override
	public void setPacemakerTime(SlicedBlock block) {
		String year = DateUtils.formatDate(block.getExpireTimePoint(), FORMAT);//每年第一天的0点
		Date firstDay = DateUtils.parseDate(year, FORMAT);
		Date firstDayOfNextYear = DateUtils.addYears(firstDay, 1);
		block.setPacemakerTime(block.getExpireTimePoint() + firstDayOfNextYear.getTime() - 1); //少一毫秒
	}

}
