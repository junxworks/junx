/*
 ***************************************************************************************
 * 
 * @Title:  Initializable.java   
 * @Package io.github.junxworks.junx.core.lang   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.lang;

/**
 * 可以初始化和销毁的接口
 *
 * @author: Michael
 * @date:   2017-5-9 15:00:09
 * @since:  v1.0
 */
public interface Initializable {
	
	/**
	 * 初始化.
	 * @throws Exception 
	 */
	public void initialize() throws Exception;

	/**
	 * 销毁.
	 */
	public void destroy() throws Exception;
}
