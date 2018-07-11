/*
 ***************************************************************************************
 * 
 * @Title:  DefaultDispatcher.java   
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventDispatcher;

/**
 * 事件调度器实现，只根据topic选择对应的channel，如果一个topic包含有多个channel，
 * 那么则会将event深克隆多份，分别发送至每个channel。
 * 
 *
 * @author: Michael
 * @date: 2017-5-9 18:59:23
 * @since: v1.0
 */
public class DefaultDispatcher implements EventDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(DefaultDispatcher.class);

	private Map<String, List<EventChannel>> topics = Maps.newHashMap();

	/** 已经分派过的事件数，可以用于统计。 */
	private AtomicLong handledEvents = new AtomicLong();

	private long warningEvents = 0;

	public long getHandledEvents() {
		return handledEvents.get();
	}

	/**
	 * @see io.github.junxworks.junx.event.EventDispatcher#registerChannel(java.lang.String,
	 *      io.github.junxworks.junx.event.EventChannel)
	 */
	@Override
	public synchronized void registerChannel(EventChannel channel) {
		Map<String, List<EventChannel>> newMap = Maps.newHashMap();
		newMap.putAll(topics);
		String topic = channel.getTopic();
		List<EventChannel> eventChannels = newMap.get(topic);
		if (eventChannels == null) {
			eventChannels = Lists.newCopyOnWriteArrayList();
			newMap.put(topic, eventChannels);
		}
		eventChannels.add(channel);
		topics = newMap;
	}

	/**
	 * @see io.github.junxworks.junx.event.EventDispatcher#unregisterChannel(java.lang.String,
	 *      io.github.junxworks.junx.event.EventChannel)
	 */
	@Override
	public synchronized void unregisterChannel(EventChannel channel) {
		Map<String, List<EventChannel>> newMap = Maps.newHashMap();
		newMap.putAll(topics);
		String topic = channel.getTopic();
		List<EventChannel> eventChannels = newMap.get(topic);
		if (eventChannels != null) {
			eventChannels.remove(channel);
			if (eventChannels.isEmpty()) {
				newMap.remove(topic);
			}
		}
		topics = newMap;
	}

	/**
	 * @see io.github.junxworks.junx.event.EventDispatcher#dispatch(io.github.junxworks.junx.event.EventContext)
	 */
	@Override
	public void dispatch(EventContext event) {
		EventChannel channel = null;
		try {
			handledEvents.incrementAndGet();
			String topic = event.getTopic();
			List<EventChannel> channels = topics.get(topic);
			if (channels != null && !channels.isEmpty()) {
				if (channels.size() == 1) {// 如果当前topic只有一个channel，大部分情况下是1，直接取第一个channel，发送事件
					channel = channels.get(0);
					channel.onEvent(event);
				} else {
					// 如果当前topic只关联了多个channel，循环往每个通道发送事件
					for (int i = 0, len = channels.size(); i < len; i++) {
						try {
							channel = channels.get(i);
							// channel.onEvent(event.deepCopy());
							channel.onEvent(event);// 暂时不对event对象进行深克隆，始终使用同一个对象，要注意多线程并发控制
						} catch (Exception e) {
							throw e;
						}
					}
				}
			} else {
				warningEvents++;
				if (warningEvents % 1000 == 1) { //1000条打印一次
					logger.warn(StringUtils.format("There is no channel subscribed topic \"%s\",total handled %d wrong messages.", topic, warningEvents));
				}
			}
		} catch (Throwable e) {
			throw new BaseRuntimeException("Exception occurred when channel \"" + channel.getChannelName() + "\" handle event", e);
		}
	}

}
