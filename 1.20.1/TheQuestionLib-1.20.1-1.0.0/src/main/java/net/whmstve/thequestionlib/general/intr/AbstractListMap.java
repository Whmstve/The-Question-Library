package net.whmstve.thequestionlib.general.intr;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractListMap<K,V> implements ListMap<K,V> {
    protected AbstractListMap() {}

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean containsKey(K key) {
        return entrySet().stream().anyMatch(entry->entry.getKey().equals(key));
    }

    @Override
    public boolean containsValue(V value) {
        return entrySet().stream().anyMatch(entry->entry.getValue().equals(value));
    }

    @Override
    public V get(K key) {
        for (Entry<K,V> entry : entrySet()){
            if(entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (Entry<K,V> entry : entrySet()){
            if(entry.getKey().equals(key)){
                return entry.setValue(value);
            }
        }
        entrySet().add(ListMap.entry(key, value));
        return null;
    }

    @Override
    public V remove(K key) {
        for (Entry<K,V> entry : entrySet()){
            if(entry.getKey().equals(key)){
                V val = entry.getValue();
                entrySet().remove(entry);
                return val;
            }
        }
        return null;
    }

    @Override
    public boolean removeIf(BiPredicate<? super K, ? super V> entryLogic) {
        return entrySet().removeIf(entry->entryLogic.test(entry.getKey(),entry.getValue()));
    }

    @Override
    public boolean removeIfKey(Predicate<? super K> keyLogic) {
        return entrySet().removeIf(entry->keyLogic.test(entry.getKey()));
    }

    @Override
    public boolean removeIfValue(Predicate<? super V> valueLogic) {
        return entrySet().removeIf(entry->valueLogic.test(entry.getValue()));
    }

    @Override
    public void putAll(ListMap<? extends K, ? extends V> m) {
        for(ListMap.Entry<? extends K, ? extends V> entry : m.entrySet()){
            put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void clear() {
        entrySet().clear();
    }

    @Override
    public Set<K> keySet() {
        return entrySet().stream().map(Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
    }

    @Override public abstract Set<Entry<K, V>> entrySet();
}
