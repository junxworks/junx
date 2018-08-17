/*
 ***************************************************************************************
 * 
 * @Title:  Stat.java   
 * @Package io.github.junxworks.junx.stat   
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
package io.github.junxworks.junx.stat;

import java.util.Collection;

import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.datawindow.DataWindow;
import io.github.junxworks.junx.stat.function.FuncEnum;
import io.github.junxworks.junx.stat.function.Function;

/**  
 * 统计对象，外部操作统计数据的门面对象
 * @ClassName:  StatObject   
 * @Description:  内部使用的统计对象
 * @author: Michael
 * @date:   2017年4月13日 下午2:46:29   
 * 
 **/
public class Stat {

	/** 统计定义. */
	private StatDefinition statDef;

	/** 数据窗口，由外部初始化. */
	private DataWindow data;

	/** 统计函数. */
	private Function function;

	/**
	 * 构造一个新的 stat object 对象.
	 *
	 * @param uniqueId the quoter id
	 * @param statDef the stat def
	 * @throws Exception 
	 */
	public static Stat create(StatDefinition statDef) throws Exception {
		Stat stat = new Stat();
		stat.setStatDef(statDef);
		stat.init();
		return stat;
	}

	/**
	 * 初始化
	 * @throws Exception 
	 */
	private void init() throws Exception {
		function = FuncEnum.valueOf(statDef.getStatFunction()).getFunction();
		data = DataWindowBuilder.createDataWindow(statDef);
	}

	public StatDefinition getStatDef() {
		return statDef;
	}

	public void setStatDef(StatDefinition statDef) {
		this.statDef = statDef;
	}

	public DataWindow getData() {
		return data;
	}

	public void setData(DataWindow data) {
		this.data = data;
	}

	/**
	 * 累计接口.
	 *
	 * @throws Exception the exception
	 */
	public void compose(DataBundle... bundles) throws Exception {
		if (bundles.length == 0) {
			return;
		}
		for (int i = 0, len = bundles.length; i < len; i++) {
			bundles[i].setFunctionAddition(statDef.getStatFunctionAddition());
		}
		data.compose(bundles);
	}

	/**
	 * 返回数据窗口的存储类型，不同存储类型需要不同处理，目前一共就两类数据窗口存储类型，持久化和非持久化（临时）
	 *
	 * @return the string
	 */
	public String storageType() {
		return data.storageType();
	}

	/**
	 * Clear.
	 */
	public void clear() {
		data.clear();
	}

	/**
	 * 获取统计值
	 *
	 * @param bundle 用于窗口数据传输的对象
	 * @para currentValue 部分函数计算时候需要用到当前值，例如二项分布、区间分布
	 * @return value 属性
	 * @throws Exception the exception
	 */
	public Object getValue(StatContext context) throws Exception {
		try {
			context.setFunctionAddition(statDef.getStatFunctionAddition());
			Collection<?> datas = data.extractData(context);
			return function.getValue(datas, context);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 将数据窗口序列化成byte数组
	 *
	 * @return the byte[]
	 * @throws Exception the exception
	 */
	public byte[] toBytes() throws Exception {
		return data.toBytes();
	}

	/**
	 * 将byte数组反序列化成数据窗口
	 *
	 * @param bytes the bytes
	 * @throws Exception the exception
	 */
	public void readBytes(byte[] bytes) throws Exception {
		if (bytes != null) {
			data.readBytes(bytes);
		}
	}
}
