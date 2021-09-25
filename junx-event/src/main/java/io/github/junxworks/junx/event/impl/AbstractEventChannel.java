/*
 ***************************************************************************************
 * 
 * @Title:  AbstractEventChannel.java   
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

import io.github.junxworks.junx.core.lang.Initializable;
import io.github.junxworks.junx.core.lifecycle.Service;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventBus;
import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventChannelHandler;
import io.github.junxworks.junx.event.EventContext;

/**
 * 抽象的事件通道，所有事件通道的基类，详细介绍参考{@link io.github.junxworks.junx.event.EventChannel}
 *
 * @author: Michael
 * @date:   2017-5-10 15:45:49
 * @since:  v1.0
 */
public abstract class AbstractEventChannel extends Service implements EventChannel {
	/** 通道订阅的topic属性. */
	protected final String topic;

	/** 与此通道关联的事件总线. */
	protected EventBus bus;

	/** 实现处理类. */
	protected EventChannelHandler handler;

	/**
	 * 构造一个新的对象.
	 *
	 * @param topic the topic
	 */
	public AbstractEventChannel(String channelName, String topic) {
		this.name = channelName;
		this.topic = topic;
	}

	public void setBus(EventBus bus) {
		this.bus = bus;
	}

	public EventBus getBus() {
		return bus;
	}

	@Override
	public String getChannelName() {
		return getName();
	}

	public EventChannelHandler getHandler() {
		return handler;
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#registered(io.github.junxworks.junx.event.EventBus)
	 */
	@Override
	public void registered(EventBus bus) {
		if (this.bus != null) {
			throw new UnsupportedOperationException("This channel is already registered!");
		}
		setBus(bus);
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#publish(io.github.junxworks.junx.event.EventContext)
	 */
	@Override
	public void publish(String targetTopic, EventContext event) throws Exception {
		if (bus == null) {
			throw new UnsupportedOperationException("Can't publish event before register this channel to a event bus.");
		}
		if (StringUtils.isNull(targetTopic)) {
			throw new UnsupportedOperationException("Can't publish event on the topic \"null\".");
		}
		if (targetTopic.equals(topic)) {
			throw new UnsupportedOperationException("Can't publish event on the topic which subscribed by the channel itself.");
		}
		EventContext _event = null;
		//这里避免修改原event对象的topic，因为可能原event对象会被多个channel使用，如果是单线程通道还好说，就怕多个异步通道同时使用event，同时修改event对象的topic
		//因此这里如果发现目标topic与当前event不一致时，复制event的简单属性给新生成的event对象
		if (!targetTopic.equalsIgnoreCase(event.getTopic())) {
			_event = new EventContext(targetTopic);
			event.copy(_event);
		} else {
			_event = event;//有可能这个event是在handler内部新增的，这种情况直接使用event对象
		}
		if (bus != null) {
			_event.setSourceChannel(this.getChannelName());
			bus.publish(_event);
		}
	}

	@Override
	public String getTopic() {
		return topic;
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#unregistered(io.github.junxworks.junx.event.EventBus)
	 */
	@Override
	public void unregistered(EventBus bus) {
		setBus(null);
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#busStarted(io.github.junxworks.junx.event.EventBus)
	 */
	@Override
	public void busStarted(EventBus bus) {
		// TODO Auto-generated method stub
		if (handler instanceof Initializable) {
			try {
				((Initializable) handler).initialize();
			} catch (Exception e) {
				logger.error("EventChannel " + getChannelName() + "eventChannelHandler initialize failed.", e);
			}
		}
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#busStopped(io.github.junxworks.junx.event.EventBus)
	 */
	@Override
	public void busStopped(EventBus bus) {
		if (handler instanceof Initializable) {
			try {
				((Initializable) handler).destroy();
				;
			} catch (Exception e) {
				logger.error("EventChannel " + getChannelName() + "eventChannelHandler initialize failed.", e);
			}
		}
	}

	/**
	 * @see io.github.junxworks.junx.event.EventChannel#setEventHandler(com.lmax.disruptor.EventHandler)
	 */
	@Override
	public void setEventHandler(EventChannelHandler handler) {
		this.handler = handler;
		if (StringUtils.isNull(name)) {
			this.name = handler.getClass().getSimpleName() + "-channel";
		}
	}
}
