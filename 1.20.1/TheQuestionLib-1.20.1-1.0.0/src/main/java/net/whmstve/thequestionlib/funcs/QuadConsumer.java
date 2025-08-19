package net.whmstve.thequestionlib.funcs;

@FunctionalInterface
public interface QuadConsumer<K, V, S, T> {
    /**
     * Performs the operation given the specified arguments.
     * @param k the first input argument
     * @param v the second input argument
     * @param s the third input argument
     * @param t the fourth input argument
     */
    void accept(K k, V v, S s, T t);
}