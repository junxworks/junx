/*
 ***************************************************************************************
 * 
 * @Title:  StatFunctionTest.java   
 * @Package io.github.junxworks.junx.test.stat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.stat.prioritywindow;

import org.junit.Test;

import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.datawindow.prioritywindow.Priorities;
import io.github.junxworks.junx.stat.function.FuncDef;

/**
 * 函数单元测试
 *
 * @ClassName:  StatFunctionTest
 * @author: Administrator
 * @date:   2017-11-22 20:15:27
 * @since:  v5.0
 */
public class StatFunctionTest extends StatTest {

	@Test
	public void linearRegression() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.LINEAR_REGRESSION, 5);
		statDef.setDataWindowPriorityType(Priorities.FIFO.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
//		so.compose(new DataBundle(timestamp + 200000, 2290.23999999999));
		so.compose(new DataBundle(timestamp + 300000, -543.66));
		so.compose(new DataBundle(timestamp + 400000, -334.85));
		so.compose(new DataBundle(timestamp + 500000, -117.82));
		so.compose(new DataBundle(timestamp + 600000, -134.99));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
	}

	@Test
	public void fifo() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.SUM, 3);
		statDef.setDataWindowPriorityType(Priorities.FIFO.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 0.5f));
		so.compose(new DataBundle(timestamp + 200000, 1));
		so.compose(new DataBundle(timestamp + 300000, 2));
		so.compose(new DataBundle(timestamp + 400000, 3));
		so.compose(new DataBundle(timestamp + 500000, 4));
		so.compose(new DataBundle(timestamp + 600000, 6));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
	}

	@Test
	public void filo() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.SUM, 3);
		statDef.setDataWindowPriorityType(Priorities.FILO.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 0.5f));
		so.compose(new DataBundle(timestamp + 200000, 1));
		so.compose(new DataBundle(timestamp + 300000, 2));
		so.compose(new DataBundle(timestamp + 400000, 3));
		so.compose(new DataBundle(timestamp + 500000, 4));
		so.compose(new DataBundle(timestamp + 600000, 6));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
	}

	@Test
	public void bigger() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.SUM, 5);
		statDef.setDataWindowPriorityType(Priorities.bigger.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 0.5f));
		so.compose(new DataBundle(timestamp + 200000, 1));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 200000, 2));
		so.compose(new DataBundle(timestamp + 200000, 3));
		so.compose(new DataBundle(timestamp + 200000, 4));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 300000, 6));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
	}

	@Test
	public void smaller() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.SUM, 6);
		statDef.setDataWindowPriorityType(Priorities.smaller.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 0.5f));
		so.compose(new DataBundle(timestamp + 200000, 1));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 200000, 2));
		so.compose(new DataBundle(timestamp + 200000, 3));
		so.compose(new DataBundle(timestamp + 200000, 4));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 300000, 6));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
	}

	@Test
	public void seriTest() throws Exception {
		StatDefinition statDef = createStatModel(FuncDef.SUM, 6);
		statDef.setDataWindowPriorityType(Priorities.smaller.toString());
		Stat so = Stat.create(statDef);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 0.5f));
		so.compose(new DataBundle(timestamp + 200000, 1));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 200000, 2));
		so.compose(new DataBundle(timestamp + 200000, 3));
		so.compose(new DataBundle(timestamp + 200000, 4));
		so.compose(new DataBundle(timestamp + 200000, 5));
		so.compose(new DataBundle(timestamp + 300000, 6));
		StatContext ctx = new StatContext();
		System.out.println(so.getValue(ctx));
		byte[] data = so.toBytes();
		System.out.println(data.length);
		StatDefinition statDef2 = createStatModel(FuncDef.SUM, 6);
		statDef2.setDataWindowPriorityType(Priorities.smaller.toString());
		Stat so2 = Stat.create(statDef2);
		so2.readBytes(data);
		System.out.println(so2.getValue(ctx));
	}

}
