package net.whmstve.thequestionlib.funcs;

import java.util.Objects;
/**
 * A predicate that takes four arguments and returns a boolean.
 */
@FunctionalInterface
public interface QuadPredicate<T, U, V, R>
{
    boolean test(T t, U u, V v, R r);

    default QuadPredicate<T, U, V, R> and(QuadPredicate<? super T, ? super U, ? super V, ? super R> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, R r) -> test(t, u, v,r) && other.test(t, u, v,r);
    }

    default QuadPredicate<T, U, V, R> negate() {return (T t, U u, V v, R r) -> !test(t, u, v, r);}

    default QuadPredicate<T, U, V, R> or(QuadPredicate<? super T, ? super U, ? super V, ? super R> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, R r) -> test(t, u, v, r) || other.test(t, u, v, r);
    }
}
