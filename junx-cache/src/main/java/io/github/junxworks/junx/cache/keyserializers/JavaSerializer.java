/*
 ***************************************************************************************
 * 
 * @Title:  JavaSerializer.java   
 * @Package io.github.junxworks.junx.cache.keyserializers   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-19 18:15:46   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.keyserializers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import io.github.junxworks.junx.cache.KeySerializer;
import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * java本身的序列化
 *
 * @ClassName:  JavaSerializer
 * @author: Michael
 * @date:   2018-7-19 18:15:46
 * @since:  v1.0
 */
public class JavaSerializer implements KeySerializer {

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.cache.KeySerializer#serialize(java.lang.String)
	 */
	@Override
	public byte[] serialize(String key) {
		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024); ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);) {
			objectOutputStream.writeObject(key);
			objectOutputStream.flush();
			return byteStream.toByteArray();
		} catch (Throwable ex) {
			throw new BaseRuntimeException("Failed to serialize object using JavaSerializer", ex);
		}
	}

}
