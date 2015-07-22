package leon.util;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:27:42 PM
 * @project userLogin
 * @author leon
 * @param <K>
 * @param <V>
 */
public class ExpiringLRUMap<K, V> {

	private ConcurrentMap<K, V> map;
	private ConcurrentMap<K, Long[]> expireMap;
	private int capacity;
	private long expireSecond;

	public ExpiringLRUMap(int capacity, long expireSecond) {
		this.capacity = capacity;
		this.expireSecond = expireSecond;

		this.map = new ConcurrentLinkedHashMap.Builder<K, V>().maximumWeightedCapacity(capacity).build();
		this.expireMap = new ConcurrentLinkedHashMap.Builder<K, Long[]>().maximumWeightedCapacity(capacity).build();
	}

	public V get(K k) {
		if (k == null) {
			return null;
		}
		Long[] expire = expireMap.get(k);
		if (expire != null && expire.length == 2) {
			// If expired: (now - createTime) > expireSecond*1000
			if ((System.currentTimeMillis() - expire[0]) > expire[1] * 1000) {
				remove(k);
			}
		} else {
			remove(k);
		}

		return map.get(k);
	}

	public int size() {
		return this.map.size();
	}

	public void put(K k, V v) {
		put(k, v, expireSecond);
	}

	/** expire:second */
	public void put(K k, V v, long expireSecond) {
		Long[] expire = new Long[] { System.currentTimeMillis(), expireSecond };
		this.map.put(k, v);
		this.expireMap.put(k, expire);

	}

	public V remove(K k) {
		this.expireMap.remove(k);
		return this.map.remove(k);
	}

	public void clear() {
		this.map.clear();
		this.expireMap.clear();
	}

	public boolean containsKey(K k) {
		return get(k) != null;
	}

	public Set<K> keySet() {
		return this.map.keySet();
	}

	public int getCapacity() {
		return this.capacity;
	}

}
