/*
 ***************************************************************************************
 * 
 * @Title:  Tracker.java   
 * @Package io.github.junxworks.junx.core.tracker   
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
package io.github.junxworks.junx.core.tracker;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;

/**
 * 整体进度记录，可用作日志打印，状态收集
 *
 * @ClassName:  Progress
 * @author: Michael
 * @date:   2018-6-7 19:31:49
 * @since:  v1.0
 */
public class Tracker {

	private static final Logger log = LoggerFactory.getLogger(Tracker.class);

	/** clock. */
	private Clock clock = ClockUtils.createClock();

	/** 当前阶段. */
	private volatile Phase currentPhase;

	private ArrayList<Phase> phases;

	private ProgressListener listener;

	public Tracker() {
	}

	public Tracker(PhaseInitializer phaseInitializer) {
		phases = phaseInitializer.initPhase();
	}

	public Clock getClock() {
		return clock;
	}

	public ArrayList<Phase> getPhases() {
		return phases;
	}

	public ProgressListener getListener() {
		return listener;
	}

	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}

	public long getCreateTime() {
		return clock.createTimeMillis();
	}

	/**
	 * 当前消耗的总毫秒
	 *
	 * @return the long
	 */
	public long currentCost() {
		return clock.countMillis();
	}

	/**
	 * 设置阶段时间偏移
	 *
	 * @param phase the phase
	 * @return the progress
	 * @throws Exception 
	 */
	public Phase progress(int index) {
		this.currentPhase = phases.get(index);
		if (currentPhase == null) {
			throw new BaseRuntimeException("The phase index \"%d\" does't exist.", index);
		}
		currentPhase.setTimeOffset(clock.countMillis());
		if (listener != null) {
			listener.progress(currentPhase);
		}
		return currentPhase;
	}

	/**
	 * Current phase.
	 *
	 * @return the int
	 */
	public Phase currentPhase() {
		return currentPhase;
	}

	/**
	 * Current phase name.
	 *
	 * @return the string
	 */
	public String currentPhaseName() {
		return currentPhase.getName();
	}

	public String getPhaseStats() {
		return JSON.toJSONString(phases);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" RcvTime:").append(getCreateTime()).append(" TotalCost:").append(currentCost()).append(" Phases:").append(this.getPhaseStats());
		return sb.toString();
	}

	public void log() {
		log.info(this.toString());
	}

}
