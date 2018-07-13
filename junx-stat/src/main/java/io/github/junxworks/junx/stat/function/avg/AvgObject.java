/*
 ***************************************************************************************
 * 
 * @Title:  AvgObject.java   
 * @Package io.github.junxworks.junx.stat.function.avg   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:30   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function.avg;

/**
 * 存储单个切分块中的数值总和和个数的信息实体类
 *
 * @ClassName:  AvgObject
 * @author: Michael
 * @date:   2017-7-11 15:35:14
 * @since:  v1.0
 */
public class AvgObject {
	/**单个切分块的合计值*/
	private double sum;
	/**单个切分块的数的个数*/
	private int count;
	
	/**
	 * 构造一个新的 avg object 对象.
	 *
	 * @param sum the sum
	 * @param count the count
	 */
	public AvgObject(double sum, int count) {
		this.sum = sum;
		this.count = count;
	}
	
	/**
	 * 构造一个新的 avg object 对象.
	 */
	public AvgObject() {
	}

	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
