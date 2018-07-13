/*
 ***************************************************************************************
 * 
 * @Title:  Sum.java   
 * @Package io.github.junxworks.junx.stat.function.sum   
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

package io.github.junxworks.junx.stat.function.sum;

import java.math.BigDecimal;
import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 求和函数，对某属性累加
 *
 * @author: michael
 * @date: 2017-6-1 14:25:14
 * @since: v1.0
 */
public class Sum extends BaseFunction {

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.function.BaseStatFunc#getValue(java.util.Collection, java.lang.Object[])
	 */
	@Override
	public Object getValue(Collection<?> data, StatContext context) throws Exception {
		if (data == null || data.isEmpty()) {
			return 0;
		}
		BigDecimal sum = new BigDecimal(0);// 默认是0
		for (Object o : data) {
			sum = sum.add(new BigDecimal(o.toString())); // 将所有block中的值全部合计
		}
		return Double.valueOf(sum.toString());
	}

}
