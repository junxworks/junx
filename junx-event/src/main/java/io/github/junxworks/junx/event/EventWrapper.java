/*
 ***************************************************************************************
 * 
 * @Title:  EventWrapper.java   
 * @Package io.github.junxworks.junx.event   
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
package io.github.junxworks.junx.event;

/**
 * Disruptor内部传递使用的对象，用于承载Event对象，每次取出实际event对象后，会把当前boat对象中的关联置空，有利于GC。
 * 此对象会一直保持在Disruptor的buffer中。
 *
 * @param <T> Event类的子类
 * @author: Michael
 * @date:   2017-5-9 15:29:48
 * @since:  v1.0
 */
public class EventWrapper {

	/** 具体的事件对象. */
	private EventContext event;

	/**
	 * 构造一个新的对象.
	 */
	public EventWrapper() {
	}

	/**
	 * 根据event入参创建boat对象.
	 *
	 * @param event the event
	 */
	public EventWrapper(EventContext event) {
		this.event = event;
	}

	/**
	 * 返回boat中的event
	 *
	 * @return the event
	 */
	public EventContext get() {
		return event;
	}

	/**
	 * 从boat中移除event
	 *
	 * @return the t
	 */
	public EventContext remove() {
		try {
			return event;
		} finally {
			this.event = null;
		}
	}

	
	/**
	 * 设置 event 属性.
	 *
	 * @param event the event
	 */
	public void setEvent(EventContext event) {
		this.event = event;
	}
}
