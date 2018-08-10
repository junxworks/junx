/*
 ***************************************************************************************
 * 
 * @Title:  Priorities.java   
 * @Package io.github.junxworks.junx.stat.datawindow.prioritywindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-8-10 18:02:28   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow.prioritywindow;

/**
 * 优先级比较枚举类
 *
 * @ClassName:  Priorities
 * @author: Michael
 * @date:   2018-8-10 18:02:28
 * @since:  v1.0
 */
public enum Priorities {

	/** 先进先出. */
	FIFO(new FIFOComparator()),
	/** 先进后出. */
	FILO(new FILOComparator()),
	/** 值越大优先级越高. */
	bigger(new BiggerStayComparator()),
	/** 值越小优先级越高. */
	smaller(new SmallerStayComparator());

	/** comparator. */
	private BundleComparator comparator;

	/**
	 * 构造一个新的 priorities 对象.
	 *
	 * @param comparator the comparator
	 */
	private Priorities(BundleComparator comparator) {
		this.comparator = comparator;
	}

	public BundleComparator getComparator() {
		return comparator;
	}

}
