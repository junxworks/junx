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
