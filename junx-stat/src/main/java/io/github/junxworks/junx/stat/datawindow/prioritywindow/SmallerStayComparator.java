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

import org.apache.commons.lang3.math.NumberUtils;

import io.github.junxworks.junx.stat.datawindow.DataBundle;

/**
 * smaller value out
 *
 * @ClassName:  RecentlyComparator
 * @author: Michael
 * @date:   2018-8-10 17:39:19
 * @since:  v1.0
 */
public class SmallerStayComparator implements BundleComparator {

	@Override
	public int compare(DataBundle o1, DataBundle o2) {
		return NumberUtils.createBigDecimal(o1.getValue().toString()).subtract(NumberUtils.createBigDecimal(o2.getValue().toString())).doubleValue() > 0 ? -1 : 1;
	}

}
