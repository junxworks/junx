/*
 ***************************************************************************************
 * 
 * @Title:  AbstractRingFiberKeeper.java   
 * @Package io.github.junxworks.junx.core.ringfiber   
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
package io.github.junxworks.junx.core.ringfiber;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.junxworks.junx.core.exception.UnsupportedParameterException;

public abstract class AbstractRingFiberKeeper<T> implements RingFiberKeeper<T> {

	/** 整个系统的rf对象上限，一共允许这么多dispatch id排队 */
	protected int rfObjectThreshold;

	/** 当前rf的数量. */
	private AtomicInteger currentRFCount = new AtomicInteger(0);

	/**初始化大小 */
	private int rfKeeperInitSize;

	private int ringFiberBufferSize = 128;

	@Override
	public void initialize() throws Exception {
		rfObjectThreshold = 10000000;
		rfKeeperInitSize = 10000;
	}

	public int getRingFiberBufferSize() {
		return ringFiberBufferSize;
	}

	public void setRingFiberBufferSize(int ringFiberBufferSize) {
		this.ringFiberBufferSize = ringFiberBufferSize;
	}

	public int getRfKeeperInitSize() {
		return rfKeeperInitSize;
	}

	public void setRfKeeperInitSize(int rfKeeperInitSize) {
		this.rfKeeperInitSize = rfKeeperInitSize;
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public RingFiber<T> getRingFiber(String dispatchId, T t) throws Exception {
		RingFiber<T> rf = fetchRingFiber(dispatchId);
		if (rf == null) {
			//单线程处理整个逻辑
			if (currentRFCount.get() > rfObjectThreshold) {
				throw new NoSpaceException("RingFiber keeper threshold is %d,there is no space for new RingFiber object.", rfObjectThreshold);
			}
			rf = new RingFiber<>(dispatchId, ringFiberBufferSize); //只有单线程来操作这个
			storeRingFiber(dispatchId, rf);
			rf.offer(t);
			rf.setNeedPublish(true);
			currentRFCount.incrementAndGet();
		} else {
			//多线程处理，需要考虑同步
			synchronized (rf.getLockObject()) {
				if (rf.isEvicted()) {//已经被驱逐了，立马更换新的rf对象
					rf = new RingFiber<>(dispatchId);
					storeRingFiber(dispatchId, rf);
					rf.offer(t);
					rf.setNeedPublish(true);
					currentRFCount.incrementAndGet();
				} else {
					rf.setNeedPublish(false);
					rf.offer(t);
				}
			}
		}
		return rf;
	}

	@Override
	public RingFiber<T> evict(RingFiber<T> ringFiber) throws Exception {
		//多线程处理，需要考虑同步
		if (ringFiber == null) {
			throw new UnsupportedParameterException("null");
		}
		RingFiber<T> oldOne = null;
		synchronized (ringFiber.getLockObject()) {
			if (ringFiber.isEmpty()) {//已经被驱逐了，立马更换新的rf对象
				oldOne = removeRingFiber(ringFiber.getDispatchID());
				if (oldOne != null) {
					currentRFCount.decrementAndGet();
					oldOne.evict();
				}
			}
		}
		return oldOne;
	}

	protected abstract RingFiber<T> fetchRingFiber(String dispatchId) throws Exception;

	protected abstract void storeRingFiber(String dispatchId, RingFiber<T> rf) throws Exception;

	protected abstract RingFiber<T> removeRingFiber(String dispatchId) throws Exception;

}
