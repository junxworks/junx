/*
 ***************************************************************************************
 * 
 * @Title:  DataWindowConstants.java   
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

/**
 * 数据窗口类型定义
 *
 * @ClassName: DataWindowConstants
 * @author: michael
 * @date: 2017-11-12 16:26:23
 * @since: v1.0
 */
public class DataWindowConstants {

	/***********************************************
	 * 数据窗口定义
	 ***********************************************/
	/** 时间窗口. */
	public static final int WIN_TYPE_TIME = 1;

	/** 长度窗口. */
	public static final int WIN_TYPE_LEN = 2;

	/***********************************************
	 * 数据窗口持久化类型定义，持久化时候可以区分，
	 * 这个只是做参考，并不是强制。
	 ***********************************************/
	/** 数据窗口的数据存储类型，永久. */
	public static final String STORAGE_TYPE_ETERNAL = "_eternal_";

	/** 数据窗口的数据存储类型，临时存储. */
	public static final String STORAGE_TYPE_TEMPORARY = "_temporary_";
}
