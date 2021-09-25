/*
 ***************************************************************************************
 * 
 * @Title:  RingFiber.java   
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

import com.google.common.base.Objects;
import com.lmax.disruptor.Sequence;

/**
 * 一个环形缓存，每个dispatch id对象对应一个此对象，所有相同dispatch id的事件均在此等候选择
 *
 * @ClassName:  RingFiber
 * @author: Michael
 * @date:   2017-12-7 21:55:17
 * @since:  v1.0
 */
public class RingFiber<T> {

	private static final int DEFAULT_SIZE = 128;

	/**当前ringfiber关联的dispatch id. */
	private String dispatchID;

	/** 尾指针，指向最后一个有效index后的一个空尾. */
	private Sequence tail = new Sequence(-1);

	/** 当前指针，当前正在进行中的有效index. */
	private Sequence cursor = new Sequence(-1);

	/** 是否需要从全局缓存中驱逐此对象，默认是true. */
	private boolean shouldBeEvicted = true;

	/** 往此rf对象中插入事件的时候，首先要判断是否此对象已经被驱逐，如果已经被驱逐，则放弃插入. */
	private boolean evicted = false;

	/** 判断是否需要重新发布此ringfiber到事件总线中，如果事件总线中已经包含此ringfiber，则不需要重新发布. */
	private boolean needPublish = false;

	/** ringfiber的大小，必须是2的整数倍. */
	private int bufferSize;

	private int indexFactor;

	/** buffer. */
	private Object[] buffer;

	/** 用于同步操作时候的锁，每个RingBuffer对象都有一个锁. */
	private Object lock = new Object();

	private T current = null;

	/**
	 * 构造一个新的 ring fiber 对象.
	 *
	 * @param dispatchID the dispatch ID
	 */
	public RingFiber(String dispatchID) {
		this.dispatchID = dispatchID;
		this.bufferSize = DEFAULT_SIZE;
		init();
	}

	public RingFiber(String dispatchID, int bufferSize) {
		this.dispatchID = dispatchID;
		this.bufferSize = bufferSize;
		init();
	}

	private void init() {
		/*将size强制转换成2的倍数
		 if (size < 2) {
			size = 2;
		} else {
			size = ((int) (size / 2)) * 2;
		}
		*/
		buffer = new Object[bufferSize];
		indexFactor = bufferSize - 1;
	}

	/**
	 * 往ringfiber中插入对象
	 *
	 * @throws NoSpaceException the no space exception
	 */
	public void offer(T t) throws NoSpaceException {
		if (isFull()) {
			throw new NoSpaceException("RingFiber is full,dispatch id is \"%s\".", dispatchID);
		}
		buffer[(int) (tail.incrementAndGet() & indexFactor)] = t;
	}

	/**
	 * 返回当前cursor指向的对象.
	 *
	 * @return the io request
	 */
	public T current() {
		return current;
	}

	/**
	 * 申请一个对象，并且游标往下移动一位.
	 *
	 * @return the colander event
	 */
	@SuppressWarnings("unchecked")
	public T apply() {
		if (isEmpty()) {
			return null;
		}
		int index = (int) (cursor.incrementAndGet() & indexFactor);
		T t = (T) buffer[index];
		current = t;//当前取出的t
		buffer[index] = null;//移除
		return t;
	}

	public boolean isShouldBeEvicted() {
		return shouldBeEvicted;
	}

	public void setShouldBeEvicted(boolean shouldBeEvicted) {
		this.shouldBeEvicted = shouldBeEvicted;
	}

	public String getDispatchID() {
		return dispatchID;
	}

	public void setDispatchID(String dispatchID) {
		this.dispatchID = dispatchID;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int remain() {
		return (int) (tail.get() - cursor.get());
	}

	public boolean isEmpty() {
		return cursor.get() == tail.get();
	}

	public boolean isFull() {
		return tail.get() - cursor.get() == bufferSize;
	}

	public boolean isEvicted() {
		return evicted;
	}

	/**
	 * 驱逐本对象.
	 */
	public void evict() {
		evicted = true;
	}

	public Object getLockObject() {
		return lock;
	}

	public boolean isNeedPublish() {
		return needPublish;
	}

	public void setNeedPublish(boolean needPublish) {
		this.needPublish = needPublish;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(dispatchID);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		return obj != null && (obj instanceof RingFiber) && dispatchID.equals(((RingFiber) obj).getDispatchID());
	}
}