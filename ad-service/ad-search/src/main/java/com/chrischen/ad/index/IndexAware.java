package com.chrischen.ad.index;

/**
 * Created by Chris Chen
 */
public interface IndexAware<K, V> {
    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
