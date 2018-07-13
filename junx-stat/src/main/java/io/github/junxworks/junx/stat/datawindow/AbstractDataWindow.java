/*
 ***************************************************************************************
 * 
 * @Title:  AbstractDataWindow.java   
 * @Package io.github.junxworks.junx.stat.datawindow   
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
package io.github.junxworks.junx.stat.datawindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 所有数据窗口的基类.
 *
 * @author: Michael
 * @date:   2017-5-18 15:49:27
 * @since:  v1.0
 */
public abstract class AbstractDataWindow implements DataWindow {

	/** logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 默认都是永久存储 
	 * @see DataWindow#storageType()
	 */
	public String storageType() {
		return DataWindowConstants.STORAGE_TYPE_ETERNAL;
	}
}
