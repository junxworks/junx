/*
 ***************************************************************************************
 * 
 * @Title:  Sortable.java   
 * @Package io.github.junxworks.junx.core.lifecycle   
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
package io.github.junxworks.junx.core.lifecycle;

/**
 * 提供一个优先级的接口，后面可以通过这个接口来进行排序。
 *
 * @author: Michael
 * @date:   2017-5-7 19:37:34
 * @since:  v1.0
 */
public interface Sortable {
	
	/**
	 * 返回 priority 属性，优先级值越小，越优先，0是最优先的，负数也当0.
	 *
	 * @return priority 属性
	 */
	public int getPriority();
}
