/*
 ***************************************************************************************
 * 
 * @Title:  ThreadService.java   
 * @Package io.github.junxworks.junx.core.lifecycle   
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
package io.github.junxworks.junx.core.lifecycle;

import com.esotericsoftware.minlog.Log;

import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 继承自{@link io.github.junxworks.junx.core.lifecycle.Service}类，主要为生命周期对象提供了线程化的模板，
 * 此类也是属于模板设计模式。启动此类后，会生成一个非守护线程，可以通过{@link #setDaemon(boolean)}方法
 * 设置此对象是不是守护线程。
 * 子类需要实现{@link #onStart()}、{@link #onRun()}、{@link #onStop()}方法，{@link #onStart()}是在主线程中调用，
 * {@link #onRun()}、{@link #onStop()}是在线程服务启动过后调用的，他们使用的classloader可能不一样，这里需要注意。
 * {@link #onStart()}放在主线程中去执行，是因为可以及时发现线程服务是否有启动成功，如果是子线程中去执行，那么只能
 * 通过线程间通信，告知主线程执行情况。
 * @author: Michael
 * @date:   2017-5-7 17:46:14
 * @since:  v1.0
 */
public abstract class ThreadService extends Service implements Runnable {

	/** thread. */
	protected Thread thread;

	/** daemon. */
	protected boolean daemon;

	/** delay. */
	private int delay = -1;

	public Thread getThread() {
		return thread;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	protected void doStart() throws Throwable {
		thread = new Thread(this);
		//		try {
		onStart();
		//		} catch (Throwable e) {
		//			logger.error("Exceptions occurred when start Service[name={" + getServiceName() + "}].", e);
		//		}
		if (daemon) {
			thread.setDaemon(daemon);
		}
		thread.setName(this.getServiceName());
		thread.start();
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	protected void doStop() throws Throwable {
		onStop();

		thread.interrupt();

		try {
			thread.join();
		} catch (InterruptedException ignore) {
		} finally {
			thread = null;
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}

		while (isStarting() || isRunning()) {
			try {
				onRun();
			} catch (Throwable ex) {
				Log.error(StringUtils.format("Exception accurred when thread service \"%s\" running", getServiceName()), ex);
			}
		}
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	/**
	 * On start.
	 *
	 * @throws Throwable the throwable
	 */
	protected abstract void onStart() throws Throwable;

	/**
	 * On stop.
	 *
	 * @throws Throwable the throwable
	 */
	protected abstract void onStop() throws Throwable;

	/**
	 * On run.
	 *
	 * @throws Throwable the throwable
	 */
	protected abstract void onRun() throws Throwable;
}