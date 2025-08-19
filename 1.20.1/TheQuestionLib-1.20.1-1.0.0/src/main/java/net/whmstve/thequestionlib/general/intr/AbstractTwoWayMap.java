package net.whmstve.thequestionlib.general.intr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractTwoWayMap<K,V> implements TwoWayMap<K,V>{
    @Override
    public void putAll(TwoWayMap<? extends K, ? extends V> twoWayMap) {
        for (TwoWayMap.Entry<? extends K, ? extends V> entry : entrySet()){
            put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        if(containsKey(key) || containsValue(value)){
            return put(key,value);
        }else{
            return false;
        }
    }

    @Override
    public K getKey(V value) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if (entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public K getKeyOrDefault(V value, K elsewise) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if (entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return elsewise;
    }

    @Override
    public V getValueOrDefault(K key, V elsewise) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return elsewise;
    }

    @Override
    public K removeValue(V value) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if(entry.getValue().equals(value)){
                K result = entry.getKey();
                entrySet().remove(entry);
                return result;
            }
        }
        return null;
    }

    @Override
    public V removeKey(K key) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if(entry.getKey().equals(key)){
                V result = entry.getValue();
                entrySet().remove(entry);
                return result;
            }
        }
        return null;
    }

    @Override
    public Map<K, V> forward() {
        return entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public Map<V, K> backward() {
        return entrySet().stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    }

    @Override
    public void clear() {
        entrySet().clear();
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean containsKey(K key) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if(entry.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (TwoWayMap.Entry<K,V> entry : entrySet()){
            if(entry.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public Set<K> keySet() {
        return new HashSet<>(entrySet().stream().map(Entry::getKey).toList());
    }

    @Override
    public Set<V> valueSet() {
        return new HashSet<>(entrySet().stream().map(Entry::getValue).toList());
    }
}
