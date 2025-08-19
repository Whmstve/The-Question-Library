package net.whmstve.thequestionlib.funcs;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * Equivalent to {@link BiFunction}, except with nonnull contract.
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface NonNullBiFunction<T, U, R> {
    @NotNull R apply(@NotNull T t, @NotNull U u);
}
