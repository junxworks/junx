/*
 ***************************************************************************************
 * 
 * @Title:  RangeDistributionConstants.java   
 * @Package io.github.junxworks.junx.stat.function.rangedist   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 22:11:14   
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
 * 区间分布常量，用来拼接区间表达式，表达式格式：rangeType:1~2|3~4|4~5
 *
 * @ClassName: RangeDistributionConstants
 * @author: Michael
 * @date: 2018-5-14 16:15:42
 * @since: v1.0
 */
public class RangeDistributionConstants {
	
	/** 类型与区间定义分隔符. */
	public static final String TYPE_SEPARATOR = ":";

	/** 区间之间连接符. */
	public static final String RANGE_SEPARATOR = "|";

	/** 区间节点连接符. */
	public static final String NODE_SEPARATOR = "~";

	/** 小时类型. */
	public static final String RANGE_TYPE_MINUTE = "m";

	/** 小时类型. */
	public static final String RANGE_TYPE_HOUR = "h";

	/** 日期类型. */
	public static final String RANGE_TYPE_DAY = "d";

	/** 星期类型. */
	public static final String RANGE_TYPE_WEEK = "w";

	/** 月份类型. */
	public static final String RANGE_TYPE_MONTH = "mon";

	/** 数字类型. */
	public static final String RANGE_TYPE_NUMBER = "num";

	/** 区间代表无穷大或无穷小的字符. */
	public static final String RANGE_INFINITY = "$";

}
