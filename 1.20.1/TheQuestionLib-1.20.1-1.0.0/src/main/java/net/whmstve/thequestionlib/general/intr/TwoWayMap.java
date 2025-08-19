package net.whmstve.thequestionlib.general.intr;

import java.util.Map;
import java.util.Set;

public interface TwoWayMap<K,V>{
    boolean put(K key, V value);
    boolean putIfAbsent(K key, V value);
    void putAll(TwoWayMap<? extends K, ? extends V> twoWayMap);
    K getKey(V value);
    K getKeyOrDefault(V value, K elsewise);
    V getValue(K key);
    V getValueOrDefault(K key, V elsewise);
    K removeValue(V value);
    V removeKey(K key);
    boolean containsKey(K key);
    boolean containsValue(V value);
    void clear();
    int size();
    Map<K,V> forward();
    Map<V,K> backward();
    boolean isEmpty();
    Set<K> keySet();
    Set<V> valueSet();
    Set<Entry<K,V>> entrySet();

    interface Entry<K,V>{
        K getKey();
        V getValue();
    }
}
