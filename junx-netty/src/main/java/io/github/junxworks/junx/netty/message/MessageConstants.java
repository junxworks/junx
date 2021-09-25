/*
 ***************************************************************************************
 * 
 * @Title:  MessageConstants.java   
 * @Package io.github.junxworks.junx.netty.message   
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
package io.github.junxworks.junx.netty.message;

public class MessageConstants {

	/*************************************************STATUS_CODE*******************************************************/
	/** 状态码：正常. */
	public static final short STATUS_CODE_SUCCESS = 200;

	/** 状态码：服务器内部执行异常 */
	public static final short STATUS_CODE_SERVER_INTERNAL_ERROR = 500;

	/** 状态码：服务器忙 */
	public static final short STATUS_CODE_SERVER_BUSY = 503;

	/** 状态码：没有在请求的现时内返回应答，服务器放弃执行请求. */
	public static final short STATUS_CODE_REQUEST_ABANDONED = 510;

	/**************************************************MESSAGE_TYPE******************************************************/
	/** 正常转发报文. */
	public static final byte MESSAGE_TYPE_NORMAL = 1;

	/** 心跳报文报文. */
	public static final byte MESSAGE_TYPE_HEARTBEAT = 99;

}
