/*
 ***************************************************************************************
 * 
 * @Title:  StatFunctionCostTest.java   
 * @Package io.github.junxworks.junx.test.stat   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:14   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.stat;

import org.junit.Test;

import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;
import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.function.FuncDef;

/**
 * 函数效率测试，所有均是单线程的效率，按多core数（线程数）翻倍
 *
 * @ClassName:  StatFunctionCostTest
 * @author: Administrator
 * @date:   2017-11-22 20:15:41
 * @since:  v5.0
 */
public class StatFunctionCostTest extends StatTest {
	protected int defaultWinNum = 7;

	@Test
	public void sumCompose() throws Exception {
		Stat so = createStatObject(FuncDef.SUM, defaultWinNum);
		long time = System.currentTimeMillis();
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.compose(new DataBundle(time + i, 1.1f));
		}
		long cost = c.countMillis();
		System.out.println("sum compose " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void sumSerialize() throws Exception {
		Stat so = createStatObject(FuncDef.SUM, defaultWinNum);
		initSO(so, defaultWinNum);
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.toBytes();
		}
		long cost = c.countMillis();
		System.out.println("sum serialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void sumDeserialize() throws Exception {
		Stat so = createStatObject(FuncDef.SUM, defaultWinNum);
		initSO(so, defaultWinNum);
		byte[] bs = so.toBytes();
		StatDefinition model = createStatModel(FuncDef.SUM, defaultWinNum);
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so = Stat.create(model);
			so.readBytes(bs);
		}
		long cost = c.countMillis();
		System.out.println("sum deserialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void prob_distCompose() throws Exception {
		Stat so = createStatObject(FuncDef.PROB_DIST, defaultWinNum);
		long time = System.currentTimeMillis();
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.compose(new DataBundle(time + i, 1.1f));
		}
		long cost = c.countMillis();
		System.out.println("prob_dist compose " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void prob_distSerialize() throws Exception {
		Stat so = createStatObject(FuncDef.PROB_DIST, defaultWinNum);
		initSO(so, defaultWinNum);
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.toBytes();
		}
		long cost = c.countMillis();
		System.out.println("prob_dist serialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void prob_distDeserialize() throws Exception {
		Stat so = createStatObject(FuncDef.PROB_DIST, defaultWinNum);
		initSO(so, defaultWinNum);
		byte[] bs = so.toBytes();
		StatDefinition model = createStatModel(FuncDef.PROB_DIST, defaultWinNum);
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so = Stat.create(model);
			so.readBytes(bs);
		}
		long cost = c.countMillis();
		System.out.println("prob_dist deserialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void range_distCompose() throws Exception {
		StatDefinition model = createStatModel(FuncDef.RANGE_DIST, defaultWinNum);
		Stat so = Stat.create(model);
		long time = System.currentTimeMillis();
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.compose(new DataBundle(time + i, 1.1f));
		}
		long cost = c.countMillis();
		System.out.println("range_dist compose " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void range_distSerialize() throws Exception {
		StatDefinition model = createStatModel(FuncDef.RANGE_DIST, defaultWinNum);
		Stat so = Stat.create(model);
		initSO(so, defaultWinNum);
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so.toBytes();
		}
		long cost = c.countMillis();
		System.out.println("range_dist serialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}

	@Test
	public void range_distDeserialize() throws Exception {
		StatDefinition model = createStatModel(FuncDef.RANGE_DIST, defaultWinNum);
		Stat so = Stat.create(model);
		initSO(so, defaultWinNum);
		byte[] bs = so.toBytes();
		Clock c = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			so = Stat.create(model);
			so.readBytes(bs);
		}
		long cost = c.countMillis();
		System.out.println("range_dist deserialize " + count + " times cost:" + cost + " 单线程tps:" + (count / cost * 1000));
	}
}
