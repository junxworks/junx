/*
 ***************************************************************************************
 * 
 * @Title:  EventContext.java   
 * @Package io.github.junxworks.junx.test.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel;

import java.util.HashMap;
import java.util.Map;

/**
 * 整个函数运行时的上下文，包含所需参数
 * @author michael
 *
 */
public class EventContext {
	private static final ThreadLocal<EventContext> local = new ThreadLocal<>();

	private Map<String, Object> fields = new HashMap<>();

	private Map<Integer, Object> stats = new HashMap<>();

	public static void setContext(EventContext e) {
		local.set(e);
	}

	public static void removeContext() {
		local.remove();
	}

	public static EventContext currentContext() {
		return local.get();
	}

	public Object getValue(String fieldName) {
		return fields.get(fieldName);
	}

	public void putField(String fieldName, Object value) {
		fields.put(fieldName, value);
	}

	public Object getStat(int id) {
		return stats.get(id);
	}

	public void putStat(int id, Object value) {
		stats.put(id, value);
	}
}
