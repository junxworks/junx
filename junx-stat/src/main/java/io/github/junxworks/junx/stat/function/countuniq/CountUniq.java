/*
 ***************************************************************************************
 * 
 * @Title:  CountUniq.java   
 * @Package io.github.junxworks.junx.stat.function.countuniq   
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
package io.github.junxworks.junx.stat.function.countuniq;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 唯一计数函数，用于统计去除重复后的统计目标数量。比如：用户交易的IP数、用户登陆的设备数量等等
 *
 * @author: Michael
 * @date:   2017-6-1 14:25:01
 * @since:  v1.0
 */
public class CountUniq extends BaseFunction {

	/**
	 * @see io.github.junxworks.junx.stat.function.StatFunc#getValue(long)
	 */
	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		//各个切分块是分开累计，collection是所有block切分块的值集合，因此可能出现block中重复属性，因此在汇总block数据的时候，需要过滤掉重复统计
		return collection.stream().distinct().count();  
	}

}
