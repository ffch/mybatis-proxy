package cn.pomit.mybatis.transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionManager {

	private static final ThreadLocal<Map<Object, Object>> resources = new ThreadLocal<>();
	
	public static Object getResource(Object actualKey) {
		Map<Object, Object> map = resources.get();
		if (map == null) {
			return null;
		}
		Object value = map.get(actualKey);
		return value;
	}
	
	public static void bindResource(Object actualKey, Object value) throws IllegalStateException {
		Map<Object, Object> map = resources.get();
		// set ThreadLocal Map if none found
		if (map == null) {
			map = new HashMap<>();
			resources.set(map);
		}
		map.put(actualKey, value);
	}
	
	public static Object unbindResource(Object actualKey) {
		Map<Object, Object> map = resources.get();
		if (map == null) {
			return null;
		}
		Object value = map.remove(actualKey);
		// Remove entire ThreadLocal if empty...
		if (map.isEmpty()) {
			resources.remove();
		}
		return value;
	}

}
