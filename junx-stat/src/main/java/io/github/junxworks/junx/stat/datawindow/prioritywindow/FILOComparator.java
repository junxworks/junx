/*
 ***************************************************************************************
 * 
 * @Title:  RecentlyComparator.java   
 * @Package io.github.junxworks.junx.stat.datawindow.prioritywindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-8-10 17:39:19   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow.prioritywindow;

import io.github.junxworks.junx.stat.datawindow.DataBundle;

/**
 * First in last out
 *
 * @ClassName:  RecentlyComparator
 * @author: Michael
 * @date:   2018-8-10 17:39:19
 * @since:  v1.0
 */
public class FILOComparator implements BundleComparator {

	@Override
	public int compare(DataBundle o1, DataBundle o2) {
		return o1.getTimestamp() >= o2.getTimestamp() ? 1 : -1;
	}

}
