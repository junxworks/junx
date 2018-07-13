/*
 ***************************************************************************************
 * 
 * @Title:  Avg.java   
 * @Package io.github.junxworks.junx.stat.function.avg   
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
package io.github.junxworks.junx.stat.function.avg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 平均值统计函数
 *
 * @ClassName:  Avg
 * @author: Michael
 * @date:   2017-7-11 15:34:34
 * @since:  v1.0
 */
public class Avg extends BaseFunction {

	/**   
	 * <p>Title: getValue</p>   
	 * <p>Description: </p>   
	 * @param timestamp
	 * @return
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.function.StatFunc#getValue(long)
	 */
	@Override
	public Object getValue(Collection<?> data, StatContext context) throws Exception {
		int size = data.size();
		BigDecimal avg = new BigDecimal(0);
		if(size > 0) {
			BigDecimal sum = new BigDecimal(0);
			AvgObject avgObject = null;
			int count = 0;
			for(Object o : data) {
				avgObject = (AvgObject)o;
				sum = sum.add(BigDecimal.valueOf(avgObject.getSum()));
				count += avgObject.getCount();
			}
			avg = sum.divide(BigDecimal.valueOf(count),DEFAULT_SCALE,RoundingMode.HALF_UP);
		}
		return avg.doubleValue();
	}

}
