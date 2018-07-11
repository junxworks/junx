/*
 ***************************************************************************************
 * 
 * @Title:  RingFiberKeeper.java   
 * @Package io.github.junxworks.junx.core.ringfiber   
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
package io.github.junxworks.junx.core.ringfiber;

import io.github.junxworks.junx.core.lang.Initializable;

/**
 * RingFiber对象的构造者与保管者，所有RingFiber均在此注册与保存
 * 
 * @ClassName:  RingFiberDetector
 * @author: Michael
 * @date:   2017-12-9 10:57:06
 * @since:  v1.0
 */
public interface RingFiberKeeper<T> extends Initializable {

	/**
	 * 根据dispatch id获取RingFiber对象
	 *
	 * @param dispatchId the dispatch id
	 * @return ring fiber 属性
	 * @throws Exception the exception
	 */
	public RingFiber<T> getRingFiber(String dispatchID, T t) throws Exception;

	/**
	 * 驱逐某个RingFiber对象.
	 *
	 * @param ringFiber the ring fiber
	 * @return the ring fiber
	 */
	public RingFiber<T> evict(RingFiber<T> ringFiber) throws Exception;
}
