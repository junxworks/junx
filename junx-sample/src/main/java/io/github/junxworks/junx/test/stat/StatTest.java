/*
 ***************************************************************************************
 * 
 * @Title:  StatTest.java   
 * @Package io.github.junxworks.junx.test.stat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:16   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.stat;

import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.datawindow.timewindow.TimeUnit;

public class StatTest {

	protected int count = 10000;

	protected Stat createStatObject(String func, int winNum) throws Exception {
		StatDefinition model = createStatModel(func, winNum);
		return Stat.create(model);
	}

	/**
	 * Creates the stat model.
	 *
	 * @param func the func
	 * @return the statistics
	 */
	protected StatDefinition createStatModel(String func) {
		return createStatModel(func, 10);
	}

	/**
	 * Creates the stat model.
	 *
	 * @param func the func
	 * @return the statistics
	 */
	protected StatDefinition createStatModel(String func, int winNum) {
		StatDefinition statModel = new StatDefinition();
		statModel.setDataWindowSize(winNum);
		statModel.setStatFunction(func);
		statModel.setDataWindowTimeUnit(TimeUnit.minute.toString());
		statModel.setStatFunctionAddition("num:$~100|100~200|200~300|300~$");
		return statModel;
	}

	public void initSO(Stat so, int count) throws Exception {
		long time = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			so.compose(new DataBundle(time + 60000 * i, 1));
		}
	}
}
