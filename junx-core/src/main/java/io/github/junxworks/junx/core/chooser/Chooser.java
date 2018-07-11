/*
 ***************************************************************************************
 * 
 * @Title:  Chooser.java   
 * @Package io.github.junxworks.junx.core.chooser   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.chooser;

import java.util.Collection;

import io.github.junxworks.junx.core.chooser.strategy.Strategy;

/**
 * 选择器
 * 非线程安全
 *
 * @ClassName:  Balancer
 * @author: Michael
 * @date:   2018-1-19 15:39:28
 * @since:  v4.4
 */
public class Chooser<T> {

	/**目标对象集合. */
	private T[] targets;

	/** 负载均衡策略，默认随机顺序. */
	private Strategy strategy = Strategy.Seqence;

	private ChooseContext context = new ChooseContext();

	@SuppressWarnings("unchecked")
	public Chooser(Collection<T> availableTargets) {
		targets = (T[]) availableTargets.toArray();
	}

	public Chooser(T[] availableTargets) {
		targets = availableTargets;
	}

	public ChooseContext getContext() {
		return context;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public T[] getTargets() {
		return targets;
	}

	public void setTargets(T[] targets) {
		this.targets = targets;
	}

	/**
	 * 选择一个可用的server
	 *
	 * @param context the context
	 * @return the server info
	 */
	@SuppressWarnings("unchecked")
	public T next() {
		if (targets == null && targets.length == 0) {
			return null;
		}
		return (T) strategy.getStrategy().choose(targets, context);
	}

}
