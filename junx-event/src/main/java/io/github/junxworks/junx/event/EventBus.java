/*
 ***************************************************************************************
 * 
 * @Title:  EventBus.java   
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

import io.github.junxworks.junx.core.lifecycle.Lifecycle;

/**
 * 事件总线的接口，通过此总线，可以发布与订阅事件，可以进行跨线程通信。<p/>
 * 目前提供两种事件总线的实现，一个实现是内部单线程调度的基于ringbuffer实现的{@link io.github.junxworks.junx.event.impl.DisruptorEventBus}，
 * 另外一个实现是基于单线程的{@link io.github.junxworks.junx.event.impl.SimpleEventBus}。下面详细介绍这两种事件总线的特性：<br/>
 * 1、DisruptorEventBus基于Disruptor高并发框架实现的跨线程通信事件总线。<br/>
 * &nbsp;&nbsp;这个总线是基于Disruptor框架封装的，内部有一个ringbuffer缓存与单线程调度器{@link io.github.junxworks.junx.event.impl.DisruptorEventProcessor},
 * 单线程调度器持续不断的从ringbuffer中抽取event事件，并且按照DisruptorEventBus设置的EventDispatcher分发器，分发到订阅了event关联topic的{@link io.github.junxworks.junx.event.EventChannel}中去。
 * <br/>&nbsp;&nbsp;DisruptorEventBus事件总线的好处是事件发布与当前的线程无关，线程提交到ringbuffer中就结束了，事件调度是异步执行的。因此此
 * 队列是异步队列。<br/>
 * 2、SimpleEventBus是直接由调用线程执行事件分发，属于当前线程直接调度，因此要注意线程同步。<br/>
 * &nbsp;&nbsp;事件总线的代码样例：<br/>
 * {@code
 * 	EventBus bus = new SimpleEventBus();   //此处只是举例，具体对象生成可以通过注入或者工厂类提供
		ExecutorEventChannel channel = new ExecutorEventChannel("testTopic"); //new一个事件通道对象
		channel.setName("testChannel");//设置一下事件通道的名称
		channel.setEventHandler(eventHandler); //设置事件通道的handler，具体事件是在handler里面处理
		bus.registerChannel(channel);//向bus注册通道
		bus.start();//启动bus，注意bus的启动是初始化bus的过程，只能执行一次，除非调用bus的stop方法，让bus停止后，再从新启动
 * }
 * <br/>
 * 由于DisruptorEventBus内部是单线程提交，因此搭配{@link io.github.junxworks.junx.event.impl.ExecutorEventChannel}使用的时候要注意，ExecutorEventChannel
 * 内部使用的RejectedExecutionHandler策略是CallerRunsPolicy，由调度线程自己去执行任务，线程来回切换非常消耗CPU，
 * 因此单线程调度的DisruptorEventBus可能会由于内部执行task而影响性能。建议SimpleEventBus搭配ExecutorEventChannel使用。
 * 
 * <br/>关于事件通道的说明，可以参考{@link EventChannel}。
 * @author: Michael
 * @date:   2017-5-9 15:34:14
 * @since:  v1.0
 */
public interface EventBus extends Lifecycle {

	/**
	 * Publish.
	 *
	 * @param event the event
	 */
	public void publish(EventContext event) throws Exception;

	/**
	 * 注册事件通道，通道可以复用，注册到多个主题中
	 *
	 * @param channel 通道对象
	 */
	public void registerChannel(EventChannel channel);

	/**
	 * 注销事件通道
	 *
	 * @param channel 通道对象
	 */
	public void unregisterChannel(EventChannel channel);
}
