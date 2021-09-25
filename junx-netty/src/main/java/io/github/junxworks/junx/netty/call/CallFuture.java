/*
 ***************************************************************************************
 * 
 * @Title:  CallFuture.java   
 * @Package io.github.junxworks.junx.netty.call   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:52:43   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.netty.call;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;
import io.netty.channel.ChannelHandlerContext;

public class CallFuture<T> implements Future<T> {
	// 因为请求和响应是一一对应的，因此初始化CountDownLatch值为1。
	private CountDownLatch latch = new CountDownLatch(1);

	// 需要响应线程设置的响应结果
	private T response;

	private Callback<T> callback;

	// Futrue的请求时间，用于计算Future是否超时
	private Clock clock = ClockUtils.createClock();

	/** 总超时时间，单位ms，防止系统假死后，sync对象无法从全局缓存中清除，timeout的上限不能超过这个值，默认1分钟. */
	private int deadline = 1000 * 60;

	private String uuid;

	public CallFuture(String uuid) {
		this.uuid = uuid;
	}

	public Callback<T> getCallback() {
		return callback;
	}

	public void setCallback(Callback<T> callback) {
		this.callback = callback;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	/**
	 * 让客户端放弃等待.
	 */
	public void giveup() {
		latch.countDown();
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		if (response != null) {
			return true;
		}
		return false;
	}

	// 获取响应结果，直到有结果才返回。
	@Override
	public T get() throws InterruptedException {
		latch.await();
		return this.response;
	}

	// 获取响应结果，直到有结果或者超过指定时间就返回。
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException {
		if (latch.await(timeout, unit)) {
			return this.response;
		}
		ReferenceManager.remove(uuid); // 超时后主动从缓存中去除记录
		return null;
	}

	// 用于设置响应结果，并且做countDown操作，通知请求线程
	public void setResponse(T response) {
		this.response = response;
		latch.countDown();
	}

	public boolean isDead() {
		return clock.countMillis() >= deadline;
	}

	public void callback(ChannelHandlerContext ctx, T t) {
		if (callback != null) {
			callback.callback(ctx, t);
		}
	}
}
