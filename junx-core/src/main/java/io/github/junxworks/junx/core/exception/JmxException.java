/*
 ***************************************************************************************
 * 
 * @Title:  JmxException.java   
 * @Package io.github.junxworks.junx.core.exception   
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
package io.github.junxworks.junx.core.exception;

/**  
 * @ClassName:  JmxException   
 * @Description:jmx访问异常   
 * @author: Sanji
 * @date:   2017年5月27日 上午11:01:47   
 * 
 **/
public class JmxException extends BaseRuntimeException {

	/**
	 * 构造一个新的 jmx exception 对象.
	 *
	 * @param msg the msg
	 */
	public JmxException(String msg) {
		super(msg);
	}
	
	/**
	 * 构造一个新的 jmx exception 对象.
	 *
	 * @param ex the ex
	 */
	public JmxException(Throwable ex) {
		super(ex);
	}
	
	/**
	 * 构造一个新的 jmx exception 对象.
	 *
	 * @param msg the msg
	 * @param ex the ex
	 */
	public JmxException(String msg, Throwable ex) {
		super(msg, ex);
	}

	/**
	 * 构造一个新的 jmx exception 对象.
	 *
	 * @param msg the msg
	 * @param args the args
	 */
	public JmxException(String msg, Object... args) {
		super(msg, args);
	}
}
