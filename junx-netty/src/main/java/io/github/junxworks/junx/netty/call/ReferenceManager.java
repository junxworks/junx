/*
 ***************************************************************************************
 * 
 * @Title:  ReferenceManager.java   
 * @Package io.github.junxworks.junx.netty.call   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:52:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.netty.call;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import io.github.junxworks.junx.core.lifecycle.ThreadService;
import io.netty.channel.ChannelHandlerContext;

/**
 * 针对关联请求的管理类，负责异步请求处理过程中的request与response对应关系，并且将过期的request清理掉
 *
 * @ClassName:  ReferenceManager
 * @author: Michael
 * @date:   2018-7-6 10:31:02
 * @since:  v1.0
 */
public class ReferenceManager extends ThreadService {

	/** 默认处理队列的大小. */
	private static final int DEFAULT_INIT_CAPACITY = 100000;

	/** 常量 futuresCache. */
	private static final Map<String, CallFuture<?>> futuresCache = new ConcurrentHashMap<>(DEFAULT_INIT_CAPACITY);

	/** 检查过期请求. */
	private int checkTimeoutInterval = 10;

	/**
	 * Prepare.
	 *
	 * @param <T> the generic type
	 * @param uuid the uuid
	 * @return the call future
	 */
	public static <T> CallFuture<T> prepare(String uuid) {
		CallFuture<T> sync = new CallFuture<>(uuid);
		futuresCache.put(uuid, sync);
		return sync;
	}

	/**
	 * Sets the result.
	 *
	 * @param <T> the generic type
	 * @param id the id
	 * @param response the response
	 * @return true代表是同步，false代表是异步
	 */
	@SuppressWarnings("unchecked")
	public static <T> void future(String uuid, ChannelHandlerContext ctx, T t) {
		CallFuture<T> cf = (CallFuture<T>) futuresCache.remove(uuid);
		if (cf != null) {
			cf.setResponse(t);
			cf.callback(ctx, t);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param <T> the generic type
	 * @param uuid the uuid
	 * @return the call future
	 */
	@SuppressWarnings("unchecked")
	public static <T> CallFuture<T> remove(String uuid) {
		return (CallFuture<T>) futuresCache.remove(uuid);
	}

	public int getCheckTimeoutInterval() {
		return checkTimeoutInterval;
	}

	public void setCheckTimeoutInterval(int checkTimeoutInterval) {
		this.checkTimeoutInterval = checkTimeoutInterval;
	}

	@Override
	protected void onStart() throws Throwable {
		this.daemon = true;
	}

	@Override
	protected void onStop() throws Throwable {
		futuresCache.clear();
	}

	@Override
	protected void onRun() throws Throwable {
		try {
			Thread.sleep(checkTimeoutInterval * 1000);
		} catch (Exception e) {
		}
		if (!futuresCache.isEmpty()) {
			Iterator<Entry<String, CallFuture<?>>> it = futuresCache.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, CallFuture<?>> en = it.next();
				CallFuture<?> cf = en.getValue();
				if (cf.isDead()) {
					cf.giveup();
					if (cf.getCallback() != null) {
						try {
							cf.getCallback().dead();
						} catch (Exception e) {
						}
					}
					it.remove();
					continue;
				}
			}
		}
	}

}
