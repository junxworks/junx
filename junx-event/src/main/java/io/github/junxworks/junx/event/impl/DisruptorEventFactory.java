/*
 ***************************************************************************************
 * 
 * @Title:  DisruptorEventFactory.java   
 * @Package io.github.junxworks.junx.event.impl   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:47:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.event.impl;

import com.lmax.disruptor.EventFactory;

import io.github.junxworks.junx.event.EventWrapper;

/**
 * DisruptorEvent 对象的工厂类，只负责生产空的{@link io.github.junxworks.junx.event.EventWrapper}对象，
 * 此对象会保持在RingBuffer中，因此只负责运输所需要的对象，当processor获取值过后，此对象会被清空内部数据。
 *
 * @ClassName:  DisruptorEventFactory
 * @author: Michael
 * @date:   2017-5-9 18:12:55
 * @since:  v1.0
 */
public class DisruptorEventFactory implements EventFactory<EventWrapper> {

	/**
	 * @see com.lmax.disruptor.EventFactory#newInstance()
	 */
	@Override
	public EventWrapper newInstance() {
		return new EventWrapper();
	}

}
