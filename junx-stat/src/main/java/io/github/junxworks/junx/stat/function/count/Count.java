/*
 ***************************************************************************************
 * 
 * @Title:  Count.java   
 * @Package io.github.junxworks.junx.stat.function.count   
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
package io.github.junxworks.junx.stat.function.count;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 计数函数，记录交易属性满足某条件的次数
 *
 * @author: Michael
 * @date:   2017-6-1 14:25:01
 * @since:  v1.0
 */
public class Count extends BaseFunction {

	/**
	 * @see io.github.junxworks.junx.stat.function.StatFunc#getValue(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		Collection<Integer> c = (Collection<Integer>) collection;
		return c.stream().reduce(0, (sum, item) -> sum + item);
	}

}
