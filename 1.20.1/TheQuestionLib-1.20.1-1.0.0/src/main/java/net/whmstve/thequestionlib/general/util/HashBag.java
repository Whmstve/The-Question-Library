package net.whmstve.thequestionlib.general.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.whmstve.thequestionlib.general.intr.AbstractBag;
import net.whmstve.thequestionlib.general.intr.Bag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HashBag<E> extends AbstractBag<E> implements Bag<E>, Collection<E> {
    private final BiMap<E,Integer> content;

    public HashBag(){
        content = HashBiMap.create();
    }

    public HashBag(int initialCapacity){
        content = HashBiMap.create(initialCapacity);
    }

    public HashBag(Collection<? extends E> collection){
        content = HashBiMap.create(
          collection.stream().collect(Collectors.toMap(
                  value->value,
                  value->(int)collection.stream().filter(v->v.equals(value)).count()
          ))
        );
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return content.keySet().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return content.keySet().toArray();
    }

    @Override
    public <T> @NotNull T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return content.keySet().toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return content.keySet().toArray(generator);
    }

    @Override
    public boolean remove(Object o) {
        if(content.containsKey(o)){
            content.remove(o);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        content.clear();
    }

    @Override
    public Spliterator<E> spliterator() {
        return content.keySet().spliterator();
    }

    @Override
    public Stream<E> stream() {
        return content.keySet().stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return content.keySet().parallelStream();
    }

    @Override
    public boolean add(E e, int i) {
        content.put(e,content.computeIfAbsent(e,ee->0)+i);
        return true;
    }

    @Override
    public boolean remove(E e, int i) {
        if(content.containsKey(e)){
            content.put(e,content.get(e)-i);
            if(content.get(e)<=0){
                content.remove(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public Set<E> uniqueSet() {
        return content.keySet().stream().filter(e->content.get(e)==1).collect(Collectors.toSet());
    }
}
