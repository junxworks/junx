/*
 ***************************************************************************************
 * 
 * @Title:  IdentifiableMessage.java   
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

public interface IdentifiableMessage {

	/**
	 * 返回全局唯一id属性.
	 *
	 * @return id 属性
	 */
	public String getUUID();
}
