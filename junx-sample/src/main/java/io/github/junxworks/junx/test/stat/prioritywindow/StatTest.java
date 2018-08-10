package io.github.junxworks.junx.test.stat.prioritywindow;

import io.github.junxworks.junx.stat.Stat;
import io.github.junxworks.junx.stat.StatDefinition;
import io.github.junxworks.junx.stat.datawindow.DataWindowConstants;

public class StatTest {

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
	protected StatDefinition createStatModel(String func, int winSize) {
		StatDefinition statModel = new StatDefinition();
		statModel.setDataWindowSize(winSize);
		statModel.setStatFunction(func);
		statModel.setDataWindowType(DataWindowConstants.WIN_TYPE_PRIORITY);
		return statModel;
	}

}
