/*
 ***************************************************************************************
 * 
 * @Title:  Max.java   
 * @Package io.github.junxworks.junx.stat.function.max   
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
package io.github.junxworks.junx.stat.function.max;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 最大值统计函数
 *
 * @ClassName:  Max
 * @author: Michael
 * @date:   2017-7-11 15:33:15
 * @since:  v1.0
 */
public class Max extends BaseFunction {
	
	/**   
	 * <p>Title: getValue</p>   
	 * <p>Description: </p>   
	 * @param timestamp
	 * @return
	 * @throws Exception   
	 * @see io.github.junxworks.junx.stat.function.StatFunc#getValue(long)
	 */
	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		if(collection.size() > 0) {
			return collection.stream().distinct().max((o1,o2) -> (double)o1 > (double)o2 ? 1:-1).get();				
		} else {
			return 0.0;
		}
	}

}
