/*
 ***************************************************************************************
 * 
 * @Title:  KeyCount.java   
 * @Package io.github.junxworks.junx.stat.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:28   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function;

/**
 * 支持键值和计数的对象接口
 *
 * @ClassName:  KeyCount
 * @author: Michael
 * @date:   2017-11-24 15:45:23
 * @since:  v1.0
 */
public interface KeyCount {
	
	/**
	 * 返回 key 属性.
	 *
	 * @return key 属性
	 */
	public String getKey();

	/**
	 * 返回 count 属性.
	 *
	 * @return count 属性
	 */
	public int getCount();
}
