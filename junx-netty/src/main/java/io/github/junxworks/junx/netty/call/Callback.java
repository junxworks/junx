/*
 ***************************************************************************************
 * 
 * @Title:  Callback.java   
 * @Package io.github.junxworks.junx.netty.call   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:52:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.netty.call;

import io.netty.channel.ChannelHandlerContext;

/**
 * 回调函数接口，回调是在netty的work线程中执行，所以不要执行有长时间耗时的操作
 *
 * @param <T> the generic type
 * @ClassName:  Callback
 * @author: Michael
 * @date:   2018-6-1 11:15:36
 * @since:  v1.0
 */
public interface Callback<T> {
	
	/**
	 * 正常应答时候的Callback.
	 *
	 * @param ctx the ctx
	 * @param t the t
	 */
	public void callback(ChannelHandlerContext ctx, T t) ;
	
	
	/**
	 * 当服务器响应超时时候的处理.
	 */
	public void dead();
}
