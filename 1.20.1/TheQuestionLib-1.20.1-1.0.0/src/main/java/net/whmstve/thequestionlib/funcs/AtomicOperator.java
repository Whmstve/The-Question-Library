package net.whmstve.thequestionlib.funcs;

import java.util.concurrent.atomic.AtomicReference;

@FunctionalInterface
public interface AtomicOperator<E>{
    E applyAsAtomic(AtomicReference<E> target);
}