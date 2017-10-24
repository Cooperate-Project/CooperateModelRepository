package de.cooperateproject.repository.commithistoryhandler.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SerializableCache<K, V> implements Map<K, V> {

	private final File location;
	private final Map<K, V> cache;
	
	public SerializableCache(File location) {
		this.location = location;
		cache = createCache(location);
	}

	@SuppressWarnings("unchecked")
	private static <K, V> Map<K, V> createCache(File location) {
		if (location.exists()) {
			try (FileInputStream fis = new FileInputStream(location)) {
				try (ObjectInputStream ois = new ObjectInputStream(fis)) {
					return (Map<K,V>)ois.readObject();									
				}
			} catch (Exception e) {
				location.delete();
			}
		}
		return new HashMap<>();
	}
	
	protected void contentChanged() {
		writeBack();
	}
	
	protected void writeBack() {
		try (FileOutputStream fos = new FileOutputStream(location)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(cache);
			}
		} catch (Exception e) {
			// ignore and try again later
		}
	}
	
	public V put(K key, V value) {
		V result = cache.put(key, value);		
		contentChanged();
		return result;
	}
	
	public V remove(Object key) {
		V result = cache.remove(key);
		contentChanged();
		return result;
	}
	
	public void putAll(Map<? extends K, ? extends V> m) {
		cache.putAll(m);
		contentChanged();
	}
	
	public void clear() {
		cache.clear();
		contentChanged();
	}

	public int size() {
		return cache.size();
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	public V get(Object key) {
		return cache.get(key);
	}

	public Set<K> keySet() {
		return cache.keySet();
	}

	public Collection<V> values() {
		return cache.values();
	}

	public Set<Entry<K, V>> entrySet() {
		return cache.entrySet();
	}

	public boolean equals(Object o) {
		return cache.equals(o);
	}

	public int hashCode() {
		return cache.hashCode();
	}

}
