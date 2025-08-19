package net.whmstve.thequestionlib.general.intr;

import java.util.Collection;
import java.util.Set;

public interface Bag<E> extends Collection<E> {
    boolean add(E e, int i);
    int getCount(E e);
    boolean remove(E e, int i);
    Set<E> uniqueSet();
}
