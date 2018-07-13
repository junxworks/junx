/*
 ***************************************************************************************
 * 
 * @Title:  Status.java   
 * @Package io.github.junxworks.junx.stat.function.status   
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
package io.github.junxworks.junx.stat.function.status;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 状态函数，用于记录某个属性处于某种状态的统计函数.
 *
 * @ClassName:  Status
 * @author: Michael
 * @date:   2017-6-29 14:13:41
 * @since:  v1.0
 */
public class Status extends BaseFunction{

	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		return collection.size() > 0 ? 1 : 0;
	}

}
