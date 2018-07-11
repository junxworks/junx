/*
 ***************************************************************************************
 * 
 * @Title:  ProgressListener.java   
 * @Package io.github.junxworks.junx.core.tracker   
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
package io.github.junxworks.junx.core.tracker;

/**
 * 进度监听器，监听跟踪器内部的进度事件，进行对应的处理，进度监听器里面不要执行耗时的方法。
 * 进度监听器内部进行异常捕获，最好不要抛出到外面，干扰正常业务流转。
 *
 * @see ProgressEvent
 */
public interface ProgressListener {
	
	/**
	 * 内部控制异常捕获 .
	 *
	 * @param phase the phase
	 * @throws Exception the exception
	 */
	public void progress(Phase phase);
}
