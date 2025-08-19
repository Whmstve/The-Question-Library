package net.whmstve.thequestionlib.funcs;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

/**
 * Equivalent to {@link TriFunction}, except with nonnull contract.
 *
 * @see TriFunction
 */
@FunctionalInterface
public interface NonNullTriFunction<T, U, V, R> {
    @NotNull R apply(@NotNull T t, @NotNull U u, @NotNull V v);
}
