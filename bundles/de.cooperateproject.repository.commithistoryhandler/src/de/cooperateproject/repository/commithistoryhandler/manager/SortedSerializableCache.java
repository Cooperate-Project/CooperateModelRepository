package de.cooperateproject.repository.commithistoryhandler.manager;

import java.io.File;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.google.common.collect.Sets;

public class SortedSerializableCache<K, V> extends SerializableCache<K, V> {

	private final TreeSet<K> sortedKeySet;
	
	public SortedSerializableCache(File location, Comparator<K> comparator) {
		super(location);
		this.sortedKeySet = new TreeSet<>(comparator);
		sortedKeySet.addAll(keySet());
	}

	@Override
	protected void contentChanged() {
		sortedKeySet.clear();
		sortedKeySet.addAll(keySet());
		super.contentChanged();
	}
	
	public NavigableSet<K> getKeysSorted() {
		return Sets.unmodifiableNavigableSet(sortedKeySet);
	}
	
}
