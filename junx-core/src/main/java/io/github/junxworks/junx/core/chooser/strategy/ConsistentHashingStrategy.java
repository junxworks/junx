/*
 ***************************************************************************************
 * 
 * @Title:  ConsistentHashingStrategy.java   
 * @Package io.github.junxworks.junx.core.chooser.strategy   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.chooser.strategy;

import io.github.junxworks.junx.core.chooser.ChooseContext;

/**
 * 基于一致性hash算法的负载均衡策略
 *
 * @ClassName:  ConsistentHashingStrategy
 * @author: Michael
 * @date:   2018-1-19 16:17:00
 * @since:  v4.4
 */
public class ConsistentHashingStrategy implements ChooseStrategy {

	@Override
	public Object choose(Object[] targets, ChooseContext context) {
		if (targets == null) {
			return null;
		}
		Object target = _choose(targets, context);
		for (; target == null && context.getChoosenSize() < targets.length;) {
			target = _choose(targets, context);
		}
		return target;
	}

	public Object _choose(Object[] targets, ChooseContext context) {
		context.addChoosenCount();
		int index = context.random().nextInt(targets.length);
		Object target = targets[index];
		if (!context.hasBeenChoosen(target)) {
			context.addChoosen(target);
			return target;
		}
		return null;
	}

}
