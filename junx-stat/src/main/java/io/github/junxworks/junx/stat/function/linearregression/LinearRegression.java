package io.github.junxworks.junx.stat.function.linearregression;

import java.util.Collection;
import java.util.stream.DoubleStream;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import com.google.common.util.concurrent.AtomicDouble;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 线性回归分析，利用统计学方法建立一个表示变量之间相互关系的方程称为回归分析。
 * 只包含一个自变量和一个因变量的回归分析。简单线性回归方程的图形是一条直线。
 * 计算方法：最小二乘法
 * 返回上升或者下降趋势
 */
public class LinearRegression extends BaseFunction {

	@Override
	public Object getValue(Collection<?> data, StatContext context) throws Exception {
		if (data.size() == 1) {
			return 0;
		}
		double[] independentValues = new double[data.size()];
		//x就用int来填充
		for (int i = 1, len = data.size(); i <= len; i++) {
			independentValues[i - 1] = i;
		}
		double[] dependentValues = getFairDependentValues(data);
		return new Regression(dependentValues, independentValues).getRegressionModel()[1];
	}

	/**
	 * 避免因变量相差过大造成的误差
	 *
	 * @return the fair dependent values
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private double[] getFairDependentValues(Collection<?> data) throws NumberFormatException, Exception {
		//y值全部归类到10-100这个区间，便于公平统计

		double avg = data.stream().flatMapToDouble(d -> {
			double v = Double.valueOf(String.valueOf(d));
			v = v > 0 ? v : v * -1;
			return DoubleStream.of(v);
		}).average().getAsDouble();
		final AtomicDouble factor = new AtomicDouble(1);
		if (avg < 10) {
			//平均数小于10，则需要
			while (avg < 10) {
				factor.set(10 * factor.get());
				avg = factor.get() * avg;
			}
		} else if (avg > 100) {
			while (avg > 100) {
				factor.set(0.1 * factor.get());
				avg = factor.get() * avg;
			}
		}

		return data.stream().flatMapToDouble(d -> {
			double v = Double.valueOf(String.valueOf(d));
			if (factor.get() != 1) {
				v = v * factor.get();
			}
			return DoubleStream.of(v);
		}).toArray();
	}

	private static class Regression {
		/** The dependent values. 因变量,需要预测的变量*/
		private double[] dependentValues;

		/** The independent values. 自变量 ,用来预测因变量值的一个或多个变量*/
		private double[] independentValues;

		/**
		 * Instantiates a new regression equation.
		 *
		 * @param dependentValues the dependent values
		 * @param independentValues the independent values
		 */
		public Regression(double[] dependentValues, double[] independentValues) {
			this.dependentValues = dependentValues;
			this.independentValues = independentValues;
		}

		/**
		 * Gets the regression model. b0值代表y轴的截距，b1值代表斜率
		 * @return the regression model
		 */
		public double[] getRegressionModel() {
			if (dependentValues.length != independentValues.length) {
				return null;
			}
			Mean meanUtil = new Mean();
			double xmean = meanUtil.evaluate(independentValues);
			double ymean = meanUtil.evaluate(dependentValues);
			double numerator = 0d;
			double denominator = 0d;
			for (int i = 0; i < dependentValues.length; i++) {
				double x = independentValues[i];
				double y = dependentValues[i];
				numerator = numerator + (x - xmean) * (y - ymean);
				denominator = denominator + (x - xmean) * (x - xmean);
			}
			double b1 = numerator / denominator;
			double b0 = ymean - b1 * xmean;
			double[] model = { b0, b1 };
			return model;
		}

	}
}
