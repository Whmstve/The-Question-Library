package net.whmstve.thequestionlib.general.intr;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractBag<E> implements Bag<E>, Collection<E> {
    protected AbstractBag() {}

    @Override public abstract int size();

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean contains(Object o) {
        for(E e : this){
            if(e.equals(o)){
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public abstract Iterator<E> iterator();

    @NotNull
    @Override
    public abstract Object @NotNull [] toArray();

    @NotNull
    @Override
    public abstract  <T> T @NotNull [] toArray(@NotNull T @NotNull [] a);

    @Override public abstract <T> T[] toArray(IntFunction<T[]> generator);

    @Override
    public boolean add(E e) {
        return add(e,1);
    }

    @Override public abstract boolean remove(Object o);

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c){
            if (!contains(o)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        boolean flag = false;
        for (E e : c){
            if(add(e)){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean flag = false;
        for (Object o : c){
            if(contains(o)){
                flag = true;
                remove(o);
            }
        }
        return flag;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        boolean flag = false;
        for (E e : this){
            if(filter.test(e)){
                flag = true;
                remove(e);
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.removeIf(e -> !c.contains(e));
    }

    @Override public abstract void clear();

    @Override
    public void forEach(Consumer<? super E> action) {
        for(E e : this){
            action.accept(e);
        }
    }

    @Override public abstract Spliterator<E> spliterator();

    @Override public abstract Stream<E> stream();

    @Override public abstract Stream<E> parallelStream();

    @Override
    public abstract boolean add(E e, int i);

    @Override
    public int getCount(E e) {
        int value = 0;
        if(contains(e)){
            for (E e_ : this){
                if(e_.equals(e)){
                    value++;
                }
            }
        }
        return value;
    }

    @Override public abstract boolean remove(E e, int i);

    @Override public abstract Set<E> uniqueSet();
}
