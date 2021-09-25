/*
 ***************************************************************************************
 * 
 * @Title:  DisruptorEventChannel.java   
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

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.github.junxworks.junx.core.exception.FatalException;
import io.github.junxworks.junx.core.executor.TaskThreadFactory;
import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventWrapper;

/**
 * 基于disruptor实现的异步通道，内部采用单线程执行handler，在CPU密集型计算中，效率最高
 * 详细介绍参考{@link io.github.junxworks.junx.event.EventChannel}
 *
 * @author: Michael
 * @date:   2017-5-10 9:50:02
 * @since:  v1.0
 */
public class DisruptorEventChannel extends AbstractEventChannel {

	/** 默认缓存队列大小. */
	private int DEFAULT_EVENT_BUFFERSIZE = 1024 * 8;

	/** ringbuffer的缓存队列大小，建议通过配置实现. */
	private int bufferSize = 0;

	/** 内部Disruptor对象. */
	private Disruptor<EventWrapper> disruptor;

	/**参考DisruptorEventBus{@link DisruptorEventBus}的等待策略说明*/
	private WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();

	public int getBufferSize() {
		return bufferSize == 0 ? DEFAULT_EVENT_BUFFERSIZE : bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public DisruptorEventChannel(String channelName, String topic) {
		super(channelName, topic);
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doStart() throws Throwable {
		try {
			disruptor = new Disruptor<EventWrapper>(new DisruptorEventFactory(), getBufferSize(), new TaskThreadFactory(getName(), true, Thread.NORM_PRIORITY), ProducerType.MULTI, BLOCKING_WAIT);
			SequenceBarrier barrier = disruptor.getRingBuffer().newBarrier(new Sequence[0]);
			BatchEventProcessor<?> processor = new BatchEventProcessor(disruptor.getRingBuffer(), barrier, new EventHandler<EventWrapper>() {
				@Override
				public void onEvent(EventWrapper boat, long sequence, boolean endOfBatch) throws Exception {
					EventContext context = boat.remove();
					context.setSequence(sequence);
					context.setEndOfBatch(endOfBatch);
					handler.handleEvent(context, DisruptorEventChannel.this);
				}
			});
			processor.setExceptionHandler(new DisruptorExceptionHandler());
			disruptor.handleEventsWith(processor); //只需要设置一个处理器，不要设置多个，总线只负责分发
			disruptor.start();
		} catch (Throwable e) {
			throw new FatalException(StringUtils.format("Exception accurred when start DisruptorEventChannel \"%s\" on topic \"%s\"", getServiceName(), topic), e);
		}
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {
		disruptor.shutdown();
	}

	@Override
	public void onEvent(EventContext event) throws Exception {
		if (isRunning()) {
			long seq = disruptor.getRingBuffer().next();
			try {
				EventWrapper e = disruptor.getRingBuffer().get(seq);
				e.setEvent(event);
			} finally {
				disruptor.getRingBuffer().publish(seq);
			}
		} else {
			throw new io.github.junxworks.junx.core.exception.UnsupportedOperationException("Can't publish event when DisruptorEventChannel's status is \"%s\"", getStatusName());
		}

	}

}
