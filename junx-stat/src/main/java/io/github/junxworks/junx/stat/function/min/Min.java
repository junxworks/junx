/*
 ***************************************************************************************
 * 
 * @Title:  Min.java   
 * @Package io.github.junxworks.junx.stat.function.min   
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
package io.github.junxworks.junx.stat.function.min;

import java.util.Collection;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 最小值函数
 *
 * @ClassName:  Min
 * @author: Michael
 * @date:   2017-7-11 15:37:42
 * @since:  v1.0
 */
public class Min extends BaseFunction {
	
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
			return collection.stream().distinct().min((o1,o2) -> Double.valueOf(o1.toString()) > Double.valueOf(o2.toString()) ? 1:-1).get();				
		} else {
			return 0.0;
		}
	}

}
