/*
 ***************************************************************************************
 * 
 * @Title:  ChooseStrategy.java   
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
 * 负载均衡策略，基于优先级、顺序、一致性哈希等算法
 *
 * @ClassName:  BalanceStrategy
 * @author: Michael
 * @date:   2018-1-19 15:40:36
 * @since:  v4.4
 */
public interface ChooseStrategy {

	/**
	 * 寻找下一个server
	 *
	 * @param servers the servers
	 * @param context the context
	 * @return the server info
	 */
	public Object choose(Object[] targets, ChooseContext context);
}
