package io.github.junxworks.junx.test.stat;

import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.datawindow.DataWindowConstants;
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
		StatDefinition statModel = new StatDefinition() {

			@Override
			public int getDataWindowType() {
				return DataWindowConstants.WIN_TYPE_TIME;
			}

			@Override
			public int getDataWindowSize() {
				return winNum;
			}

			@Override
			public String getStatFunction() {
				return func;
			}

			@Override
			public String getDataWindowTimeUnit() {
				return TimeUnit.minute.toString();
			}

			@Override
			public String getStatFunctionAddition() {
				return "num:$~100|100~200|200~300|300~$";
			}

		};
		return statModel;
	}

	public void initSO(Stat so, int count) throws Exception {
		long time = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			so.compose(new DataBundle(time + 60000 * i, 1));
		}
	}
}
