package techit.util;

import java.util.LinkedHashMap;
import java.util.Map;


public class ResponseMap<K, V> {

	private Map<K, V> map;

	public ResponseMap() {
		this.map = new LinkedHashMap<>();
	}

	public ResponseMap<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	public Map<K, V> getMap() {
		return this.map;
	}
}
