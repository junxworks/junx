/*
 ***************************************************************************************
 * 
 * @Title:  DefaultExceptionHandler.java   
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.ExceptionHandler;

import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.event.EventContext;
import io.github.junxworks.junx.event.EventWrapper;

/**
 * 此类目前只负责打印各种异常日志
 *
 * @author: Michael
 * @date:   2017-5-9 19:25:08
 * @since:  v1.0
 */
public final class DefaultExceptionHandler implements ExceptionHandler<EventWrapper> {
	/** 常量 LOGGER. */
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	/**
	 * Fatal exception handler.
	 */
	public DefaultExceptionHandler() {
	}

	/**
	 * @see com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable, long, java.lang.Object)
	 */
	@Override
	public void handleEventException(final Throwable ex, final long sequence, final EventWrapper boat) {
		EventContext event = boat.get();
		if (event != null) {
			logger.error(StringUtils.format("Exception occurred when processing event[topic=\"%s\",channel=\"%s\"", event.getTopic(), event.getSourceChannel()), ex);
		} else {
			logger.error("Exception occurred when processing event", ex);
		}
	}

	/**
	 * @see com.lmax.disruptor.ExceptionHandler#handleOnStartException(java.lang.Throwable)
	 */
	@Override
	public void handleOnStartException(final Throwable ex) {
		logger.error("Exception during onStart()", ex);
	}

	/**
	 * @see com.lmax.disruptor.ExceptionHandler#handleOnShutdownException(java.lang.Throwable)
	 */
	@Override
	public void handleOnShutdownException(final Throwable ex) {
		logger.error("Exception during onShutdown()", ex);
	}
}
