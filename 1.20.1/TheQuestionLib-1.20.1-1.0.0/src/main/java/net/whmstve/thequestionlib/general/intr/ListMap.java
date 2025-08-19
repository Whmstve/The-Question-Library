package net.whmstve.thequestionlib.general.intr;

import java.util.Collection;
import java.util.Set;
import java.util.function.*;

public interface ListMap<K, V> {

    /** Returns the number of key-value mappings in this map. */
    int size();

    /** Returns true if this map contains no key-value mappings. */
    boolean isEmpty();

    /** Returns true if this map contains a mapping for the specified key. */
    boolean containsKey(K key);

    /** Returns true if this map maps one or more keys to the specified value. */
    boolean containsValue(V value);

    /** Returns the value to which the specified key is mapped, or null if none. */
    V get(K key);

    /** Associates the specified value with the specified key. */
    V put(K key, V value);

    /** Removes the mapping for the specified key from this map if present. */
    V remove(K key);

    /**
     * Removes the entry if it matches the predicate
     */
    boolean removeIf(BiPredicate<? super K, ? super V> keyLogic);

    /**
     * Removes the entry if the key matches the predicate
     */
    boolean removeIfKey(Predicate<? super K> keyLogic);

    /**
     * Removes the entry if the value matches the predicate
     */
    boolean removeIfValue(Predicate<? super V> keyLogic);

    /** Copies all of the mappings from the specified map to this map. */
    void putAll(ListMap<? extends K, ? extends V> m);

    /** Removes all of the mappings from this map. */
    void clear();

    /** Returns a set view of the keys contained in this map. */
    Set<K> keySet();

    /** Returns a collection view of the values contained in this map. */
    Collection<V> values();

    /** Returns a set view of the mappings contained in this map. */
    Set<Entry<K, V>> entrySet();

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.   Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     */
    default void forEach(BiConsumer<? super K, ? super V> action){
        entrySet().forEach(entry->action.accept(entry.getKey(),entry.getValue()));
    }
    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     */
    default V getOrDefault(K key, V otherValue){
        V value = get(key);
        return value != null ? value : otherValue;
    }

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * {@code null}, else returns the current value.
     */
    default V putIfAbsent(K key, V value){
        if(containsKey(key)){
            return get(key);
        }else{
            return put(key,value);
        }
    }

    /**
    * If the specified key is not already associated with a value (or is mapped
    * to {@code null}), attempts to compute its value using the given mapping
    * function and enters it into this map unless {@code null}.
    *
    * <p>If the mapping function returns {@code null}, no mapping is recorded.
    * If the mapping function itself throws an (unchecked) exception, the
    * exception is rethrown, and no mapping is recorded.
     **/
    default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V resultValue;
        if(containsKey(key)){
            return get(key);
        }else if((resultValue = mappingFunction.apply(key))!=null){
            return put(key,resultValue);
        }
        return null;
    }

    /**
     * If the value for the specified key is present and non-null, attempts to
     * compute a new mapping given the key and its current mapped value.
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     */
    default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if(containsKey(key)){
            V value = remappingFunction.apply(key,get(key));
            if(value!=null){
                put(key,value);
                return value;
            }else{
                remove(key);
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * Attempts to compute a mapping for the specified key and its current
     * mapped value (or {@code null} if there is no current mapping).
     * <p>If the remapping function returns {@code null}, the mapping is removed
     * (or remains absent if initially absent).  If the remapping function
     * itself throws an (unchecked) exception, the exception is rethrown, and
     * the current mapping is left unchanged.
     */
    default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V oldValue = get(key);
        V newValue = remappingFunction.apply(key, oldValue);
        if(newValue==null){
            if(oldValue!=null){
                remove(key);
                return oldValue;
            }else{
                return null;
            }
        }else{
            put(key,newValue);
            return newValue;
        }
    }

    /**
     * Retrieves all values in which the associated key passes the predicate.
     */
    default Collection<V> getAllValues(Predicate<? super K> logic){
        return entrySet().stream().filter(entry->logic.test(entry.getKey())).map(Entry::getValue).toList();
    }

    /**
     * A key-value pair.
     */
    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V value);
    }

    static <K,V> Entry<K,V> entry(K key, V value){
        return new KeyValueEntry<>(key, value);
    }

    final class KeyValueEntry<K,V> implements ListMap.Entry<K,V> {
        private final K key;
        private V value;

        KeyValueEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
}
