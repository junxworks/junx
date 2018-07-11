/*
 ***************************************************************************************
 * 
 * @Title:  ChooseContext.java   
 * @Package io.github.junxworks.junx.core.chooser   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:35   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.chooser;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import io.github.junxworks.junx.core.util.ObjectUtils;

/**
 * 负载均衡器的上下文，一次完整的发送消息过程中，有且仅有一个此对象。
 * 完整的发送消息过程定义为：第一次就发送成功或者经过若干次重试后发送成功或者经过若干次重试最终发送失败的过程。
 *
 * @ClassName:  BalancerInfo
 * @author: Michael
 * @date:   2018-1-19 15:28:19
 * @since:  v4.4
 */
public class ChooseContext {

	/** 当前选择器的索引取随机数. */
	private int beginIndex = (int) (System.currentTimeMillis() & 99999999);

	/** 分发ID，由外部入参传入. */
	private String chooseId = "none";

	/** 用于一致性hash计算的种子. */
	private Random random;

	/** 记录已经被选择过的对象. */
	private Set<Object> choosentargets = new HashSet<>();

	/** 已经进行选择的次数. */
	private int choosenCount = 0;

	/**
	 * 获取当前索引
	 *
	 * @return the int
	 */
	public int nextIndex() {
		return beginIndex++;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public int getChoosenCount() {
		return choosenCount;
	}

	public void addChoosenCount() {
		choosenCount++;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public void setChooseId(String chooseId) {
		this.chooseId = chooseId;
	}

	/**
	 * Random.
	 *
	 * @return the random
	 */
	public Random random() {
		if (random != null) {
			return random;
		}
		long hashId = (ObjectUtils.hash(chooseId) >>> 1) % 53777;
		random = new Random(hashId);
		return random;
	}

	/**
	 * 将选中的server加入到一个set中，用于下一次过滤
	 *
	 * @param server the server
	 */
	public void addChoosen(Object target) {
		choosentargets.add(target);
	}

	/**
	 * 将选中的server加入到一个set中，用于下一次过滤
	 *
	 * @param server the server
	 */
	public boolean hasBeenChoosen(Object target) {
		return choosentargets.contains(target);
	}

	public int getChoosenSize() {
		return choosentargets.size();
	}

}
