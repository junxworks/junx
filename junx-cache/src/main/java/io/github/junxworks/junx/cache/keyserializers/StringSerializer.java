/*
 ***************************************************************************************
 * 
 * @Title:  StringSerializer.java   
 * @Package io.github.junxworks.junx.cache.keyserializers   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-19 18:15:58   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.keyserializers;

import java.io.UnsupportedEncodingException;

import io.github.junxworks.junx.cache.KeySerializer;
import io.github.junxworks.junx.core.exception.BaseRuntimeException;

/**
 * String直接getbytes
 *
 * @ClassName:  StringSerializer
 * @author: Michael
 * @date:   2018-7-19 18:15:58
 * @since:  v1.0
 */
public class StringSerializer implements KeySerializer {

	/** 常量 CHARSET. */
	private static final String CHARSET = "UTF-8";

	@Override
	public byte[] serialize(String key) {
		try {
			return key.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new BaseRuntimeException("Failed to serialize object using StringSerializer", e);
		}
	}

}
