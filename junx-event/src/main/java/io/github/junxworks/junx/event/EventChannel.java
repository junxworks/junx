/*
 ***************************************************************************************
 * 
 * @Title:  EventChannel.java   
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
 * 事件处理的通道，此通道可以接收来自EventBus的消息，同时也可以往EventBus中发送消息。一个通道只能关联一个事件总线，并且只能订阅一个topic。<p/>
 * 每个通道有且只有一个{@link io.github.junxworks.junx.event.EventChannelHandler}。
 * 目前EventChannel的实现有两个类，跟事件总线{@link io.github.junxworks.junx.event.EventBus}类似，通道的两个实现一个是基于
 * 多线程的实现{@link io.github.junxworks.junx.event.impl.ExecutorEventChannel}，另外一个是基于单线程的实现{@link io.github.junxworks.junx.event.impl.SimpleEventChannel}，
 * 两者的区别就是在事件总线EventBus中，dispatcher调度的时候，ConcurrentEventChannel直接由当前线程提交到线程池中执行EventHandler方法，而SimpleEventChannel
 * 就直接在当前线程执行EventHandler方法。<br/>
 * 目前两类事件通道EventChannel可以跟两类事件总线EventBus组合使用，不同的组合有不同的效果：<br/>
 * 1、SimpleEventBus+SimpleEventChannel=事件同步提交，事件同步执行，事件总线在总体架构中只起到模块化松耦合的作用。<br/>
 * 2、SimpleEventBus+ConcurrentEventChannel=事件同步提交，事件异步执行，此功能是异步提交任务功能。<br/>
 * 3、DisruptorEventBus+SimpleEventChannel=事件异步提交，事件处理由单线程异步执行，效率比线程池和阻塞队列高很多<br/>
 * 4、DisruptorEventBus+ConcurrentEventChannel=事件异步提交，事件异步执行，由于DisruptorEventBus内部是单线程提交，
 * 		因此搭配{@link io.github.junxworks.junx.event.impl.ExecutorEventChannel}使用的时候要注意，ConcurrentEventChannel
 * 		内部使用的RejectedExecutionHandler策略是CallerRunsPolicy，由调度线程自己去执行任务，线程来回切换非常消耗CPU，
 * 		因此单线程调度的DisruptorEventBus可能会由于内部执行task而影响性能，因此不建议采用这种组合方式。
 * <br/>关于事件总线的说明，可以参考{@link EventBus}。
 * 
 * 新增{@link io.github.junxworks.junx.event.impl.DisruptorEventChannel}disruptor通道，跟ConcurrentEventChannel一样，属于异步通道，
 * 内部是采用单线程处理，效率非常高，推荐计算密集型模块使用此通道。
 * 
 * @author: Michael
 * @date:   2017-5-9 16:33:39
 * @since:  v1.0
 */
public interface EventChannel extends Lifecycle {

	/**
	 * 获取通道订阅的topic
	 *
	 * @return 通道订阅的topic
	 */
	public String getTopic();

	/**
	 * 返回通道名
	 *
	 * @return 通道名
	 */
	public String getChannelName();

	/**
	 * 从eventbus中注册完成后调用.
	 *
	 * @param bus the bus
	 */
	public void registered(EventBus bus);

	/**
	 * 从eventbus中注销过后调用
	 *
	 * @param bus the bus
	 */
	public void unregistered(EventBus bus);

	/**
	 * 总线启动后调用.
	 *
	 * @param 事件总线
	 */
	public void busStarted(EventBus bus);

	/**
	 * 总线停止后调用
	 *
	 * @param 事件总线
	 */
	public void busStopped(EventBus bus);

	/**
	 * 设置当前通道的eventhandler。
	 *
	 * @param handler 具体的事件处理类
	 */
	public void setEventHandler(EventChannelHandler handler);

	/**
	 * 处理事件，当订阅的主题有事件发生时候，会被调用此方法。
	 *
	 * @param 当前事件
	 */
	public void onEvent(EventContext event) throws Exception;

	/**
	 * 往事件总线中发布事件。
	 *
	 * @param event the event
	 */
	public void publish(String topic, EventContext event) throws Exception;

}
