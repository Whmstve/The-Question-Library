package net.whmstve.thequestionlib.general.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypewrittenList<E> extends ArrayList<E> {
    private final Class<E> clazz;

    public TypewrittenList(Class<E> clazz,int initialCapacity) {
        super(initialCapacity);
        this.clazz=clazz;
    }

    public TypewrittenList(Class<E> clazz) {
        super();
        this.clazz=clazz;
    }

    public TypewrittenList(Class<E> clazz, @NotNull Collection<? extends E> c) {
        super(c);
        this.clazz=clazz;
    }

    public Class<E> getWrittenClass(){
        return this.clazz;
    }

    public List<E> normalize(){
        return new ArrayList<>(this);
    }
}
