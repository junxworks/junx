/*
 ***************************************************************************************
 * All rights Reserved, Designed By www.zzcx.com.cn
 * @Title:  StatEngineTopics.java   
 * @Package cn.com.zzcx.rtrc.core.eval.stat
 * @Description: (用一句话描述该文件做什么)   
 * @author: songpeng
 * @date:   2017-6-1 16:34:11   
 * @version V1.0 
 * @Copyright: 2017 智卓创新(北京)信息产业股份有限公司. All rights reserved.
 * 注意：本内容仅限于公司内部使用，禁止外泄以及用于其他的商业目
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.stat;

import org.junit.Test;

import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
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

	/**
	 * 求和函数
	 * @throws Exception 
	 */
	@Test
	public void sum() throws Exception {
		StatDefinition model = createStatModel(FuncDef.SUM);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 200000, 1), new DataBundle(timestamp + 200000, 2.3f));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 平均数
	 * @throws Exception 
	 */
	@Test
	public void avg() throws Exception {
		StatDefinition model = createStatModel(FuncDef.AVG);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 200000, 2.4f));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 计数
	 * @throws Exception 
	 */
	@Test
	public void count() throws Exception {
		StatDefinition model = createStatModel(FuncDef.COUNT);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void count_equal() throws Exception {
		StatDefinition model = createStatModel(FuncDef.COUNT_EQUAL);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		so.compose(new DataBundle(timestamp + 300000, 1));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		ctx.setValue(1);
		System.out.println(so.getValue(ctx));
		ctx.setValue(2.3);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void count_uniq() throws Exception {
		StatDefinition model = createStatModel(FuncDef.COUNT_UNIQ);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		so.compose(new DataBundle(timestamp + 300000, 1));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void max() throws Exception {
		StatDefinition model = createStatModel(FuncDef.MAX);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.52f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		so.compose(new DataBundle(timestamp + 300000, 1));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void min() throws Exception {
		StatDefinition model = createStatModel(FuncDef.MIN);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		so.compose(new DataBundle(timestamp + 300000, 1));
		so.compose(new DataBundle(timestamp + 400000, 0.1));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void snapshot() throws Exception {
		StatDefinition model = createStatModel(FuncDef.SNAPSHOT);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 200000);
		System.out.println(so.getValue(ctx));
		so.compose(new DataBundle(timestamp + 300000, 2.1f));
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
		so.compose(new DataBundle(timestamp + 200000, 5));
		System.out.println(so.getValue(ctx));
		so.compose(new DataBundle(timestamp + 400000, 5));
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 相同计数
	 * @throws Exception 
	 */
	@Test
	public void status() throws Exception {
		StatDefinition model = createStatModel(FuncDef.STATUS);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 概率分布
	 * @throws Exception 
	 */
	@Test
	public void prob_dist() throws Exception {
		StatDefinition model = createStatModel(FuncDef.PROB_DIST);
		Stat so = Stat.create( model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 1.5f));
		so.compose(new DataBundle(timestamp + 70000, 1));
		so.compose(new DataBundle(timestamp + 70001, 2));
		so.compose(new DataBundle(timestamp + 200000, 2.3f));
		so.compose(new DataBundle(timestamp + 300000, 1));
		so.compose(new DataBundle(timestamp + 400000, 0.1));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		ctx.setValue(1);
		System.out.println(so.getValue(ctx));
	}

	/**
	 * 区间分布
	 * @throws Exception 
	 */
	@Test
	public void rang_dist() throws Exception {
		StatDefinition model = createStatModel(FuncDef.RANGE_DIST);
		Stat so = Stat.create(model);
		long timestamp = System.currentTimeMillis();
		so.compose(new DataBundle(timestamp, 50f));
		so.compose(new DataBundle(timestamp + 70000, 200));
		so.compose(new DataBundle(timestamp + 70001, 250));
		so.compose(new DataBundle(timestamp + 200000, 350f));
		so.compose(new DataBundle(timestamp + 300000, 10));
		so.compose(new DataBundle(timestamp + 400000, 300));
		StatContext ctx = new StatContext();
		ctx.setTimestamp(timestamp + 500000);
		ctx.setValue(1);
		System.out.println(so.getValue(ctx));
	}

}
