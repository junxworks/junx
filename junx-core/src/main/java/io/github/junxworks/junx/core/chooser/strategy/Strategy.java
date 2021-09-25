/*
 ***************************************************************************************
 * 
 * @Title:  Strategy.java   
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

public enum Strategy {
	CHash(new ConsistentHashingStrategy()), //一致性哈希
	Seqence(new SequenceStrategy()); //顺序

	private ChooseStrategy strategy;

	private Strategy(ChooseStrategy strategy) {
		this.strategy = strategy;
	}

	public ChooseStrategy getStrategy() {
		return strategy;
	}

}
