package net.whmstve.thequestionlib.general.util;

import net.minecraftforge.common.util.NonNullFunction;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class NonNullMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {
    private final Map<K,V> map;
    @Nullable
    private final V defaultValue;

    public static <K,V> NonNullMap<K,V> create(){return new NonNullMap<>(Lists.newArrayList(), null);}

    public static <K,V> NonNullMap<K,V> createWithCapacity(int pInitialCapacity) {
        return new NonNullMap<>(com.google.common.collect.Lists.newArrayListWithCapacity(pInitialCapacity), null);
    }

    public static <K,V> NonNullMap<K,V> withSize(int pSize, @NotNull V pDefaultValue){
        return new NonNullMap<>(com.google.common.collect.Lists.newArrayListWithCapacity(pSize), pDefaultValue);
    }

    public static <K,V> NonNullMap<K,V> of(List<K> pKeys, V pDefaultValue, NonNullFunction<K,V> pValueMapper){
        NonNullMap<K,V> nonNullMap = new NonNullMap<>(pKeys,pDefaultValue);
        nonNullMap.replaceDefaults(pValueMapper);
        return nonNullMap;
    }

    protected NonNullMap(List<K> keys, @Nullable V defaultValue){
        this.defaultValue = defaultValue;
        if(defaultValue!=null){
            this.map = new HashMap<>(keys.stream().collect(Collectors.toMap(key -> key, key -> defaultValue)));
        }else{
            this.map = new HashMap<>();
        }
    }

    private void replaceDefaults(NonNullFunction<K,V> pValueMapper){
        map.keySet().forEach(key-> map.put(key,pValueMapper.apply(key)));
    }

    @Override
    public V put(K key, @NotNull V value) {
        return map.put(key, value);
    }

    @Nullable
    @Override
    public V putIfAbsent(K key, @NotNull V value) {
        return map.putIfAbsent(key,value);
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public void clearKeys(){
        if(this.defaultValue==null){
            map.clear();
        }else{
            replaceDefaults(k->this.defaultValue);
        }
    }
}
