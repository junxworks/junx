/*
 ***************************************************************************************
 * 
 * @Title:  ProbabilityDistribution.java   
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;
import io.github.junxworks.junx.stat.function.KeyCount;

/**
 *概率分布函数,计算某一交易属性在随机围内的比率.
 *最大值是1，值应该是0~1之间的浮点数，包含0和1。
 *
 * @ClassName:  BinDist
 * @author: Michael
 * @date:   2017-6-26 14:59:20
 * @since:  v1.0
 */
public class ProbabilityDistribution extends BaseFunction{
	
	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		if( context==null){
			throw new UnsupportedParameterException("Parameter for %s function can not be null or parameter array length > 1",getClass().getSimpleName());
		}
		if(!collection.isEmpty()) {
			Map<String,Integer> resMap= collection.stream()
					.map(ceo -> (KeyCount) ceo)
					.collect(
							Collectors.toMap(
									KeyCount::getKey,
									KeyCount::getCount, 
									(newValue, oldValue) -> newValue+oldValue)
							);
			Integer targaetCount=resMap.get(context.getValue().toString());
			targaetCount= targaetCount==null ? 0 : targaetCount;
			int sum = resMap.values().stream().mapToInt(o->o).sum();
			BigDecimal _targaetCount = new BigDecimal(targaetCount);
			BigDecimal sumCount= new BigDecimal(sum);
			return _targaetCount.divide(sumCount,DEFAULT_SCALE,RoundingMode.HALF_UP).doubleValue();//保留10位小数
		}
		return 0;
	}

}
