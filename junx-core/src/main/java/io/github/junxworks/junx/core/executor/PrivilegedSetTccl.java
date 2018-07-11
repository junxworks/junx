/*
 ***************************************************************************************
 * 
 * @Title:  PrivilegedSetTccl.java   
 * @Package io.github.junxworks.junx.core.executor   
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
package io.github.junxworks.junx.core.executor;

import java.security.PrivilegedAction;

/**
 * 类访问安全控制相关类，目前用于线程工厂{@link io.github.junxworks.junx.core.executor.TaskThreadFactory}。
 *
 * @author: Michael
 * @date:   2017-5-8 18:17:44
 * @since:  v1.0
 */
public class PrivilegedSetTccl implements PrivilegedAction<Void> {

	/** cl. */
	private ClassLoader cl;

	/**
	 * 构造一个新的对象.
	 *
	 * @param cl the cl
	 */
	public PrivilegedSetTccl(ClassLoader cl) {
		this.cl = cl;
	}

	/**
	 * @see java.security.PrivilegedAction#run()
	 */
	@Override
	public Void run() {
		Thread.currentThread().setContextClassLoader(cl);
		return null;
	}
}