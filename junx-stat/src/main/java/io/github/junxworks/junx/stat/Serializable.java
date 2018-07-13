/*
 ***************************************************************************************
 * 
 * @Title:  Serializable.java   
 * @Package io.github.junxworks.junx.stat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat;

/**
 * 序列化与反序列化接口
 *
 * @ClassName:  Serializable
 * @author: michael
 * @date:   2017-11-15 10:22:22
 * @since:  v1.0
 */
public interface Serializable {

	/**
	 * 序列化成byte数组
	 *
	 * @return the byte[]
	 */
	public byte[] toBytes() throws Exception;

	/**
	 * 从byte数组中初始化数据
	 *
	 * @param bytes the bytes
	 */
	public void readBytes(byte[] bytes) throws Exception;
}
