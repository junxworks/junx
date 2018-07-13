/*
 ***************************************************************************************
 * 
 * @Title:  CountEqual.java   
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

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;
import io.github.junxworks.junx.stat.function.KeyCount;


/**
 * 相同计数函数，用于统计统计目标值的数量。比如：用户使用的每种交易数、用户使用的每种缴费类型数等等
 * 返回一个map对象，key是统计目标值，value是统计目标值的次数。例如统计用户使用不同IP的次数：
 * {10.111.125.131=3, 10.111.125.132=2, 10.111.125.133=1}
 * @author: Michael
 * @date:   2017-6-1 14:25:01
 * @since:  v1.0
 */
public class CountEqual extends BaseFunction {

	/**
	 * @see io.github.junxworks.junx.stat.function.StatFunc#getValue(long)
	 * @return Map<String,Integer> 例如{10.111.125.131=3, 10.111.125.132=2, 10.111.125.133=1}
	 */
	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		//各个切分块是分开累计，collection是所有block切分块的值集合，因此可能出现block中重复key，因此在汇总block数据的时候，需要过重复统计相同的key
		//这里通过stream的list转map，转map的时候，将重复key的count相加，得到最终的次数
		if(context==null){
				throw new UnsupportedParameterException("Parameter for %s function can not be null or parameter array length > 1",getClass().getSimpleName());
		}
		Map<String,Integer> resMap= collection.stream()
				.map(ceo -> (KeyCount) ceo)
				.collect(
						Collectors.toMap(
								KeyCount::getKey,
								KeyCount::getCount, 
								(newValue, oldValue) -> newValue+oldValue)
						);
		Integer res=resMap.get(context.getValue().toString());
		return res==null ? 0 : res;
	}

}
