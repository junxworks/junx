/*
 ***************************************************************************************
 * 
 * @Title:  SequenceStrategy.java   
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
 * 按顺序选择
 *
 * @ClassName:  SequenceChooser
 * @author: Michael
 * @date:   2018-6-13 17:30:51
 * @since:  v1.0
 */
public class SequenceStrategy implements ChooseStrategy {

	@Override
	public Object choose(Object[] targets, ChooseContext context) {
		if (context.getChoosenCount() == targets.length) {
			return null;
		}
		int index = context.nextIndex();
		int targetSize = targets.length;
		context.addChoosenCount(); //已选择记录数+1
		if (isPowerOfTwo(targetSize)) {
			return targets[index & (targetSize - 1)];
		} else {
			return targets[Math.abs(index % targetSize)];
		}
	}

	private static boolean isPowerOfTwo(int val) {
		return (val & -val) == val;
	}
}
