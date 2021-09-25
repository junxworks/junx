/*
 ***************************************************************************************
 * 
 * @Title:  AbstractEventBus.java   
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

import java.util.List;

import com.google.common.collect.Lists;

import io.github.junxworks.junx.core.lifecycle.Lifecycle;
import io.github.junxworks.junx.core.lifecycle.Service;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventBus;
import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventDispatcher;

/**
 * 事件总线的基类，提供了一些基础方法，例如注册、注销通道。
 * 具体说明，请参考{@link io.github.junxworks.junx.event.EventBus}
 * 
 * @author: Michael
 * @date: 2017-5-10 15:40:00
 * @since: v1.0
 */
public abstract class AbstractEventBus extends Service implements EventBus {
	/** 内部调度器 */
	protected EventDispatcher dispatcher = new DefaultDispatcher();

	/** 所有通道对象都在此保存. */
	protected List<EventChannel> allChannels = Lists.newCopyOnWriteArrayList();

	@Override
	public abstract void publish(EventContext event) throws Exception;

	/**
	 * @see io.github.junxworks.junx.event.EventBus#registerChannel(java.lang.String,
	 *      io.github.junxworks.junx.event.EventChannel)
	 */
	@Override
	public void registerChannel(EventChannel channel) {
		channel.registered(this);
		dispatcher.registerChannel(channel);
		allChannels.add(channel);
		logger.info("Register channel \"{}\" on topic \"{}\" in EventBus.", channel.getChannelName(), channel.getTopic());
		if (this.isRunning() || this.isStarting()) {
			channel.busStarted(this);
			channel.start();
		}
	}

	/**
	 * @see io.github.junxworks.junx.event.EventBus#unregisterChannel(java.lang.String,
	 *      io.github.junxworks.junx.event.EventChannel)
	 */
	@Override
	public void unregisterChannel(EventChannel channel) {
		channel.unregistered(this);
		dispatcher.unregisterChannel(channel);
		allChannels.remove(channel);
		logger.info("Unregister channel \"{}\" on topic \"{}\" in EventBus", channel.getChannelName(), channel.getTopic());
		if (channel.getStatus() != Lifecycle.RUNNING && (this.isStopping() || this.isStopped())) {
			channel.busStopped(this);
			channel.stop();
		}
	}

	@Override
	protected void doStart() throws Throwable {
		for (int i = 0, len = allChannels.size(); i < len; i++) {
			EventChannel c = allChannels.get(i);
			try {
				c.busStarted(this);
			} catch (Exception e) {
				logger.error(StringUtils.format("Exception occurred when fire \"busStarted\" event on channel \"%s\".", c.getChannelName()), e);
			}
			try {
				c.start();
			} catch (Exception e) {
				logger.error(StringUtils.format("Exception occurred when start channel \"%s\".", c.getChannelName()), e);
			}
		}

	}

	@Override
	protected void doStop() throws Throwable {
		logger.info("Event Bus is Shutdown,handled {} events.", ((DefaultDispatcher) dispatcher).getHandledEvents());
		for (int i = 0, len = allChannels.size(); i < len; i++) {
			EventChannel c = allChannels.get(i);
			try {
				c.busStopped(this);
			} catch (Exception e) {
				logger.error("Exception occurred when fire \"busStopped\" event on channel \"{}\".", c.getChannelName());
			}
			try {
				c.stop();
			} catch (Exception e) {
				logger.error("Exception occurred when stop channel \"{}\".", c.getChannelName());
			}
		}
	}

}
