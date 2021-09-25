/*
 ***************************************************************************************
 * 
 * @Title:  DisruptorEventProcessor.java   
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

import java.util.concurrent.atomic.AtomicBoolean;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.DataProvider;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.Sequencer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.TimeoutHandler;

import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventWrapper;

/**
 * 默认的Disruptor的处理类，这个类实现了runnable接口，在eventbus启动的时候，本类会在线程中运行，直到eventbus停止。
 * 此类是Disruptor的核心处理类，修改了Disruptor内部实现的{@link com.lmax.disruptor.BatchEventProcessor<T>}，采用其内部代码修改而来。
 * @author: Michael
 * @date:   2017-5-9 18:56:21
 * @since:  v1.0
 */
public class DisruptorEventProcessor implements EventProcessor {

	/** running. */
	protected final AtomicBoolean running = new AtomicBoolean(false);

	/** exception handler. */
	protected ExceptionHandler<? super EventWrapper> exceptionHandler = new DefaultExceptionHandler();

	/** data provider. */
	protected final DataProvider<EventWrapper> dataProvider;

	/** sequence barrier. */
	protected final SequenceBarrier sequenceBarrier;

	/** event handler. */
	protected final EventHandler<EventContext> eventHandler;

	/** sequence. */
	protected final Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

	/** timeout handler. */
	protected final TimeoutHandler timeoutHandler;

	/**
	 * Construct a {@link EventProcessor} that will automatically track the progress by updating its sequence when
	 * the {@link EventHandler#onEvent(Object, long, boolean)} method returns.
	 *
	 * @param dataProvider    to which events are published.
	 * @param sequenceBarrier on which it is waiting.
	 * @param eventHandler    is the delegate to which events are dispatched.
	 */
	public DisruptorEventProcessor(final DataProvider<EventWrapper> dataProvider, final SequenceBarrier sequenceBarrier, final EventHandler<EventContext> eventHandler) {
		this.dataProvider = dataProvider;
		this.sequenceBarrier = sequenceBarrier;
		this.eventHandler = eventHandler;

		if (eventHandler instanceof SequenceReportingEventHandler) {
			((SequenceReportingEventHandler<?>) eventHandler).setSequenceCallback(sequence);
		}

		timeoutHandler = (eventHandler instanceof TimeoutHandler) ? (TimeoutHandler) eventHandler : null;
	}

	@Override
	public Sequence getSequence() {
		return sequence;
	}

	/* (non-Javadoc)
	 * @see com.lmax.disruptor.EventProcessor#halt()
	 */
	@Override
	public void halt() {
		running.set(false);
		sequenceBarrier.alert();
	}

	@Override
	public boolean isRunning() {
		return running.get();
	}

	/**
	 * Set a new {@link ExceptionHandler} for handling exceptions propagated out of the {@link BatchEventProcessor}
	 *
	 * @param exceptionHandler to replace the existing exceptionHandler.
	 */
	public void setExceptionHandler(final ExceptionHandler<? super EventWrapper> exceptionHandler) {
		if (null == exceptionHandler) {
			throw new NullPointerException();
		}

		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * It is ok to have another thread rerun this method after a halt().
	 *
	 * @throws IllegalStateException if this object instance is already running in a thread
	 */
	@Override
	public void run() {
		if (!running.compareAndSet(false, true)) {
			throw new IllegalStateException("Thread is already running");
		}
		sequenceBarrier.clearAlert();

		notifyStart();

		EventWrapper boat = null;
		long nextSequence = sequence.get() + 1L;
		try {
			while (true) {
				try {
					final long availableSequence = sequenceBarrier.waitFor(nextSequence);

					while (nextSequence <= availableSequence) {
						boat = dataProvider.get(nextSequence);
						if (boat != null) {
							EventContext event = boat.get();
							try {
								eventHandler.onEvent(event, nextSequence, nextSequence == availableSequence);
							} catch (Exception e) {
								exceptionHandler.handleEventException(e, nextSequence, boat);
							} finally {
								boat.remove();
							}
						}
						nextSequence++;
					}
					sequence.set(availableSequence);
				} catch (final TimeoutException e) {
					notifyTimeout(sequence.get());
				} catch (final AlertException ex) {
					if (!running.get()) {
						break;
					}
				} catch (final Throwable ex) {
					exceptionHandler.handleEventException(ex, nextSequence, boat);
					sequence.set(nextSequence);
					nextSequence++;
				}
			}
		} finally {
			notifyShutdown();
			running.set(false);
		}
	}

	/**
	 * Notify timeout.
	 *
	 * @param availableSequence the available sequence
	 */
	protected void notifyTimeout(final long availableSequence) {
		try {
			if (timeoutHandler != null) {
				timeoutHandler.onTimeout(availableSequence);
			}
		} catch (Throwable e) {
			exceptionHandler.handleEventException(e, availableSequence, null);
		}
	}

	/**
	 * Notifies the EventHandler when this processor is starting up
	 */
	protected void notifyStart() {
		if (eventHandler instanceof LifecycleAware) {
			try {
				((LifecycleAware) eventHandler).onStart();
			} catch (final Throwable ex) {
				exceptionHandler.handleOnStartException(ex);
			}
		}
	}

	/**
	 * Notifies the EventHandler immediately prior to this processor shutting down
	 */
	protected void notifyShutdown() {
		if (eventHandler instanceof LifecycleAware) {
			try {
				((LifecycleAware) eventHandler).onShutdown();
			} catch (final Throwable ex) {
				exceptionHandler.handleOnShutdownException(ex);
			}
		}
	}
}
