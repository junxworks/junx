/*
 ***************************************************************************************
 * 
 * @Title:  DataWindowBuilder.java   
 * @Package io.github.junxworks.junx.stat.datawindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:28   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow;

import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.timewindow.SlicedTimeBasedDataWindow;
import io.github.junxworks.junx.stat.datawindow.timewindow.TimeUnit;
import io.github.junxworks.junx.stat.datawindow.timewindow.TimeWindowDefinition;
import io.github.junxworks.junx.stat.function.FuncEnum;

/**
 * DataWindow 对象的工厂类.
 * 先已静态方法的方式提供接口，如有需求，数据窗口工厂可以修改成接口和实现类的方式。
 * 
 * @ClassName:  DataWindowFactory
 * @author: michael
 * @date:   2017-11-12 16:33:48
 * @since:  v1.0
 */
public class DataWindowBuilder {

	/**
	 * Creates a new DataWindow object.
	 *
	 * @param statDef the stat def
	 * @return the data window<?>
	 */
	public static DataWindow createDataWindow(StatDefinition statDef) throws Exception {
		//根据统计定义中的窗口类型和统计函数，确定一个数据窗口
		int winType = statDef.getDataWindowType();//窗口类型
		String func = statDef.getStatFunction();//统计函数
		switch (winType) {
			case DataWindowConstants.WIN_TYPE_TIME://时间窗口类型
				TimeUnit timeUnit =TimeUnit.valueOf(statDef.getDataWindowTimeUnit());
				TimeWindowDefinition windef = new TimeWindowDefinition(timeUnit, statDef.getDataWindowSize());
				FuncEnum funcEnum = FuncEnum.valueOf(func);
				SlicedTimeBasedDataWindow timeWindow = new SlicedTimeBasedDataWindow(windef, funcEnum.getSlicedBlockFactory());
				return timeWindow;
			default:
				throw new UndefinedDataWindowTypeException("Undefined data window type \"%d\"", winType);
		}
	}
}
