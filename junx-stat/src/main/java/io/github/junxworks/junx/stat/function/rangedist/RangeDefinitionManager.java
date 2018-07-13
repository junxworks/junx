/*
 ***************************************************************************************
 * 
 * @Title:  RangeDefinitionManager.java   
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import io.github.junxworks.junx.core.exception.NullParameterException;
import io.github.junxworks.junx.core.exception.UnsupportedFormatException;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.StatDefinition;

/**
 * 根据funcAddtion字符串创建区间定义
 *
 * @ClassName: RangeDefCreator
 * @author: Michael
 * @date: 2017-11-24 13:46:08
 * @since: v1.0
 */
public class RangeDefinitionManager {

	/** 内部全局解析的区间定义，每个list都是根据特定的funcAddtion解析的.此处没有遍历map的需求，不控制并发 */
	private static final Map<String, List<RangeObject>> rangeDefs = Maps.newHashMap();

	/** 常量 timeCompareType. */
	private static final Set<String> timeCompareType = new HashSet<>(Arrays.asList(RangeDistributionConstants.RANGE_TYPE_MINUTE, RangeDistributionConstants.RANGE_TYPE_HOUR, RangeDistributionConstants.RANGE_TYPE_DAY, RangeDistributionConstants.RANGE_TYPE_WEEK, RangeDistributionConstants.RANGE_TYPE_MONTH));

	/** 常量 valueCompareType. */
	private static final Set<String> valueCompareType = new HashSet<>(Arrays.asList(RangeDistributionConstants.RANGE_TYPE_NUMBER));

	/**
	 * 根据给定值返回所在的区间对象，忽略重复区间
	 *
	 * @param statDef the stat def
	 * @param data the data
	 * @return range def 属性
	 */
	public static RangeObject getRangeDef(StatDefinition statDef, StatContext context) {
		if (statDef == null) {
			throw new NullParameterException("");
		}
		return getRangeDef(statDef.getStatFunctionAddition(), context);
	}

	/**
	 * 根据给定值返回所在的区间对象，忽略重复区间
	 *
	 * @param funcAddtion the func param
	 * @param data the data
	 * @return range def 属性
	 */
	public static RangeObject getRangeDef(String funcAddtion, StatContext context) {
		if (StringUtils.isNull(funcAddtion)) {
			throw new NullParameterException("");
		}
		List<RangeObject> rdfs = getRangeDefs(funcAddtion);
		if (rdfs != null && !rdfs.isEmpty()) {
			for (int i = 0, len = rdfs.size(); i < len; i++) {
				RangeObject rdf = rdfs.get(i);
				RangeCompare rco = new RangeCompare();
				rco.setTime(context.getTimestamp());
				rco.setValue(context.getValue());
				if (rdf.isIn(rco)) {
					return rdf;
				}
			}
		}
		return null;
	}

	/**
	 * 构造List<RangeDefinition>的时候，忽略线程并发，就算多几个list对象，也不会影响最终行为。
	 *
	 * @param funcAddtion the func param
	 * @return the list
	 */
	public static List<RangeObject> getRangeDefs(StatDefinition statDef) {
		if (statDef == null) {
			throw new NullParameterException("");
		}
		return getRangeDefs(statDef.getStatFunctionAddition());
	}

	/**
	 *
	 * @param funcAddtion the func param
	 * @return the list
	 */
	public static List<RangeObject> getRangeDefs(String funcAddtion) {
		if (StringUtils.isNull(funcAddtion)) {
			throw new NullParameterException("");
		}
		List<RangeObject> rdList = rangeDefs.get(funcAddtion);
		if (rdList == null) {
			synchronized (rangeDefs) {
				if ((rdList = rangeDefs.get(funcAddtion)) == null) {
					rdList = createRangeDefs(funcAddtion);
					rangeDefs.put(funcAddtion, rdList);
				}
			}
		}
		return rdList;
	}

	/**
	 * 是否属于时间比较类型
	 *
	 * @param type the type
	 * @return 返回布尔值 time compare type
	 */
	public static boolean isTimeCompareType(String type) {
		return timeCompareType.contains(type);
	}

	/**
	 * 是否属于值比较类型
	 *
	 * @param type the type
	 * @return 返回布尔值 time compare type
	 */
	public static boolean isValueCompareType(String type) {
		return valueCompareType.contains(type);
	}

	/**
	 * 构造区间定义
	 *
	 * @param funcAddtion 函数参数格式定义，rangeType:0~1|1~6|6~12  以此类推。
	 * @return the list
	 */
	public static List<RangeObject> createRangeDefs(String funcAddtion) {
		List<RangeObject> resList = new ArrayList<>();
		if (funcAddtion.endsWith(RangeDistributionConstants.RANGE_SEPARATOR)) {
			funcAddtion = funcAddtion.substring(0, funcAddtion.length() - 1);
		}
		String[] rangeDef = StringUtils.split(funcAddtion, RangeDistributionConstants.TYPE_SEPARATOR);
		if (rangeDef.length != 2) {
			throw new UnsupportedFormatException(funcAddtion);
		}
		String type = rangeDef[0];
		int compareType = 0;
		if (isTimeCompareType(type)) {
			compareType = RangeObject.COMPARE_TYPE_TIME;
		} else if (isValueCompareType(type)) {
			compareType = RangeObject.COMPARE_TYPE_VALUE;
		} else {
			throw new UndefinedRangeTypeException("Undefined range type %s", type);
		}
		String[] ranges = StringUtils.split(rangeDef[1], RangeDistributionConstants.RANGE_SEPARATOR);
		for (String range : ranges) {
			String[] valuePair = StringUtils.split(range, RangeDistributionConstants.NODE_SEPARATOR);
			RangeObject rdf = new RangeObject();
			rdf.setType(type);
			rdf.setCompareType(compareType);
			rdf.setStartValue(valuePair[0]);
			rdf.setEndValue(valuePair[1]);
			rdf.setRange(range);
			rdf.setFuncAddtion(funcAddtion);
			resList.add(rdf);
		}
		return resList;
	}
}
