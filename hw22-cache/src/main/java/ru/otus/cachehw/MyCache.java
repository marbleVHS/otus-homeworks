package ru.otus.cachehw;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> internalMap = new WeakHashMap<>();

    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, "put");
        }
        internalMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, internalMap.get(key), "remove");
        }
        internalMap.remove(key);
    }

    @Override
    public V get(K key) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, internalMap.get(key), "get");
        }
        return internalMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
