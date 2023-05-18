/*
 ***************************************************************************************
 * 
 * @Title:  FuncEnum.java   
 * @Package io.github.junxworks.junx.stat.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function;

import io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory;
import io.github.junxworks.junx.stat.function.avg.Avg;
import io.github.junxworks.junx.stat.function.avg.AvgBlockFactory;
import io.github.junxworks.junx.stat.function.count.Count;
import io.github.junxworks.junx.stat.function.count.CountBlockFactory;
import io.github.junxworks.junx.stat.function.countequal.CountEqual;
import io.github.junxworks.junx.stat.function.countequal.CountEqualBlockFactory;
import io.github.junxworks.junx.stat.function.countuniq.CountUniq;
import io.github.junxworks.junx.stat.function.countuniq.CountUniqBlockFactory;
import io.github.junxworks.junx.stat.function.linearregression.LinearRegression;
import io.github.junxworks.junx.stat.function.linearregression.LinearRegressionBlockFactory;
import io.github.junxworks.junx.stat.function.max.Max;
import io.github.junxworks.junx.stat.function.max.MaxBlockFactory;
import io.github.junxworks.junx.stat.function.min.Min;
import io.github.junxworks.junx.stat.function.min.MinBlockFactory;
import io.github.junxworks.junx.stat.function.probdist.ProbDistBlockFactory;
import io.github.junxworks.junx.stat.function.probdist.ProbabilityDistribution;
import io.github.junxworks.junx.stat.function.rangedist.RangeDistBlockFactory;
import io.github.junxworks.junx.stat.function.rangedist.RangeDistribution;
import io.github.junxworks.junx.stat.function.snapshot.Snapshot;
import io.github.junxworks.junx.stat.function.snapshot.SnapshotBlockFactory;
import io.github.junxworks.junx.stat.function.status.Status;
import io.github.junxworks.junx.stat.function.status.StatusBlockFactory;
import io.github.junxworks.junx.stat.function.sum.Sum;
import io.github.junxworks.junx.stat.function.sum.SumBlockFactory;

/**
 * 函数类型枚举类.
 *
 * @ClassName:  FuncEnum
 * @author: Michael
 * @date:   2017-7-5 9:41:16
 * @since:  v1.0
 */
public enum FuncEnum {
	avg(new AvgBlockFactory(), new Avg(), Double.class),
	prob_dist(new ProbDistBlockFactory(), new ProbabilityDistribution(),Double.class),
	range_dist(new RangeDistBlockFactory(), new  RangeDistribution(),Double.class),
	count(new CountBlockFactory(), new Count(), Integer.class),
	count_equal(new CountEqualBlockFactory(), new  CountEqual(),Integer.class),
	count_uniq(new CountUniqBlockFactory(), new  CountUniq(),Long.class),
	max(new MaxBlockFactory(), new  Max(),Double.class),
	min(new MinBlockFactory(), new  Min(),Double.class),
	snapshot(new SnapshotBlockFactory(), new Snapshot(), String.class),
	status(new StatusBlockFactory(), new Status(),Integer.class),
	sum(new SumBlockFactory(), new Sum(), Double.class),
	liner_regression(new LinearRegressionBlockFactory(),new LinearRegression(),Double.class);

	private Class<?> resultType;

	/** 每个函数对应的切分块工厂. */
	private SlicedBlockFactory slicedBlockFactory;

	private Function function;

	private FuncEnum(SlicedBlockFactory factory, Function function, Class<?> resultType) {
		this.slicedBlockFactory = factory;
		this.function = function;
		this.resultType = resultType;
	}

	public SlicedBlockFactory getSlicedBlockFactory() {
		return slicedBlockFactory;
	}

	public Function getFunction() {
		return function;
	}

	public Class<?> getResultType() {
		return resultType;
	}

}
