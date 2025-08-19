package net.whmstve.thequestionlib.memory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
public class GlobalRequests {
    private static final Multimap<Class<?>,Object> memory = HashMultimap.create();

    public static <T,E> void make(Class<E> clazz, T target){
        memory.put(clazz,target);
    }

    public static <T,E> Collection<T> get(Class<E> clazz){
        return (Collection<T>) memory.get(clazz);
    }

    public static <E> void removeAll(Class<E> clazz){
        memory.removeAll(clazz);
    }

    public static <T,E> void remove(Class<E> clazz, T target){
        memory.remove(clazz,target);
    }

    public static <T,E> void forEach(Class<E> clazz, Consumer<? super T> action){
        ((Collection<T>) memory.get(clazz)).forEach(action);
    }

    public static void clear(){
        memory.clear();
    }

    public static boolean isEmpty(){
        return memory.isEmpty();
    }

    public static <E> boolean isEmpty(Class<E> clazz){
        return memory.get(clazz).isEmpty();
    }
}
