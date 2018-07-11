/*
 ***************************************************************************************
 * 
 * @Title:  DisruptorExceptionHandler.java   
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

public class DisruptorExceptionHandler implements ExceptionHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(DisruptorExceptionHandler.class);

	@Override
	public void handleEventException(final Throwable ex, final long sequence, final Object event) {
		logger.error("Exception processing: " + sequence + " " + event, ex);
	}

	@Override
	public void handleOnStartException(final Throwable ex) {
		logger.error("Exception during onStart()", ex);
	}

	@Override
	public void handleOnShutdownException(final Throwable ex) {
		logger.error("Exception during onShutdown()", ex);
	}
}