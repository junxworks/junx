/*
 ***************************************************************************************
 * 
 * @Title:  EventChannelHandler.java   
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通道内使用的事件处理类，业务上实现这个接口即可。
 *
 * @author: Michael
 * @date:   2017-5-11 15:14:46
 * @since:  v1.0
 */
public interface EventChannelHandler {
	public static final Logger logger = LoggerFactory.getLogger(EventChannelHandler.class);

	/** 建议的统一异常处理的主题名. */
	public static final String EXCEPTION_TOPIC = "_topic_exception_";

	/** 建议的统一异常的参数名.. */
	public static final String EXCEPTION_PARAM = "_param_exception_";

	/**
	 * 处理事件
	 *
	 * @param event 当前事件上下文
	 * @param channel 事件通道
	 * @throws Exception channel必须捕获handle事件时候发生的异常
	 */
	public void handleEvent(EventContext event, EventChannel channel) throws Exception;
}
