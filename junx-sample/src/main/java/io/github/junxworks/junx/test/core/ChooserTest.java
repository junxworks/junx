/*
 ***************************************************************************************
 * 
 * @Title:  ChooserTest.java   
 * @Package io.github.junxworks.junx.test.core   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-10-10 10:55:35   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.core;

import org.junit.Test;

import io.github.junxworks.junx.core.chooser.Chooser;
import io.github.junxworks.junx.core.chooser.strategy.Strategy;

/**
 * 选择算法测试
 *
 * @ClassName:  ChooserTest
 * @author: Michael
 * @date:   2018-10-10 10:53:30
 * @since:  v1.0
 */
public class ChooserTest {

	private String[] aa = new String[] { "a", "b", "c", "d" };

	/**
	 * 一致性哈希
	 */
	@Test
	public void consistentHash() {
		Chooser<String> c = new Chooser<String>(aa);
		c.setStrategy(Strategy.CHash);
		c.getContext().setChooseId("1234561123");
		String s = null;
		while ((s = c.next()) != null) {
			System.out.println("ch:" + s);
		}
	}

	/**
	 * 随机起点的顺序选择
	 */
	@Test
	public void randomSequence() {
		Chooser<String> c = new Chooser<String>(aa);
		String s = null;
		while ((s = c.next()) != null) {
			System.out.println("rs:" + s);
		}
	}
}
