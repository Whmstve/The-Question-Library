package net.whmstve.thequestionlib.general.intr;

import net.whmstve.thequestionlib.funcs.QuadConsumer;
import net.whmstve.thequestionlib.funcs.QuadFunction;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A key-based, three-dimensional mapping structure (primary, secondary, tertiary) to a single value.
 * <p>
 * Similar to a 3D table or tensor, this structure allows associating a triple of keys with a value.
 * This is especially useful for multidimensional data storage or logic conditions involving combinations.
 *
 * @param <Primary>   the type of primary keys
 * @param <Secondary> the type of secondary keys
 * @param <Tertiary>  the type of tertiary keys
 * @param <Value>     the type of mapped values
 */
public interface Tensor<Primary, Secondary, Tertiary, Value> {

    // Containment

    /**
     * Returns {@code true} if this tensor containsCell a mapping for the specified keys.
     */
    boolean containsQuadrant(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey);

    /**
     * Returns {@code true} if this tensor containsCell any mappings with the specified primary key.
     */
    boolean containsPrimary(Primary primaryKey);

    /**
     * Returns {@code true} if this tensor containsCell any mappings with the specified secondary key.
     */
    boolean containsSecondary(Secondary secondaryKey);

    /**
     * Returns {@code true} if this tensor containsCell any mappings with the specified tertiary key.
     */
    boolean containsTertiary(Tertiary tertiaryKey);

    /**
     * Returns {@code true} if this tensor containsCell the specified value.
     */
    boolean containsValue(Value value);

    // Retrieval

    /**
     * Returns the value associated with the given keys, or {@code null} if no such mapping exists.
     */
    Value get(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey);

    /**
     * Returns {@code true} if this tensor containsCell no key-value mappings.
     */
    boolean isEmpty();

    /**
     * Returns the total number of key-value mappings in this tensor.
     */
    int size();

    // Equality and hash

    /**
     * Compares the specified object with this tensor for equality.
     * Returns {@code true} if the specified object is also a tensor and has equal quadrant mappings.
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this tensor, defined as the hash code of its quadrant set.
     */
    @Override
    int hashCode();

    // Mutators

    /**
     * Removes all mappings from this tensor.
     */
    void clear();

    /**
     * Associates the specified value with the specified keys.
     * If a mapping already exists, it is replaced.
     *
     * @return the previous value associated with the keys, or {@code null} if there was none
     */
    Value put(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey, Value value);

    /**
     * Associates the specified value with the specified keys if the tensor doesn't contain a
     * mapping for those keys, otherwise returning.
     *
     * @param primaryKey first key that the value should be associated with
     * @param secondaryKey second key that the value should be associated with
     * @param tertiaryKey third key that the value should be associated with
     * @param value value to be associated with the specified keys
     * @return the value previously associated with the keys, or {@code null} if no mapping existed
     *     for the keys
     */
    Value putIfAbsent(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey, Value value);

    /**
     * Copies all mappings from the specified tensor into this tensor.
     */
    void putAll(Tensor<? extends Primary, ? extends Secondary, ? extends Tertiary, ? extends Value> tensor);

    /**
     * Removes the mapping for the specified keys, if present.
     *
     * @return the value previously associated with the keys, or {@code null} if none
     */
    Value remove(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey);

    // Views

    /**
     * Returns a view of the mappings associated with the specified primary key,
     * as a table of secondary → tertiary → value.
     */
    Table<Secondary, Tertiary, Value> primary(Primary primaryKey);

    /**
     * Returns a view of the mappings associated with the specified secondary key,
     * as a table of primary → tertiary → value.
     */
    Table<Primary, Tertiary, Value> secondary(Secondary secondaryKey);

    /**
     * Returns a view of the mappings associated with the specified tertiary key,
     * as a table of primary → secondary → value.
     */
    Table<Primary, Secondary, Value> tertiary(Tertiary tertiaryKey);

    /**
     * Returns a set view of all key-value mappings in this tensor.
     * Each mapping is represented as a {@link Quadrant}.
     * <p>
     * Changes to the returned set affect the tensor and vice versa.
     */
    Set<Quadrant<Primary, Secondary, Tertiary, Value>> quadrantSet();

    /**
     * Returns a set of all primary keys contained in this tensor.
     */
    Set<Primary> primaryKeySet();

    /**
     * Returns a set of all secondary keys contained in this tensor.
     */
    Set<Secondary> secondaryKeySet();

    /**
     * Returns a set of all tertiary keys contained in this tensor.
     */
    Set<Tertiary> tertiaryKeySet();

    /**
     * Returns a collection of all values contained in this tensor.
     * May contain duplicate values.
     */
    Collection<Value> values();

    // Nested map views

    /**
     * Returns a nested map view of this tensor:
     * primary → (secondary → (tertiary → value)).
     */
    Map<Primary, Map<Secondary, Map<Tertiary, Value>>> primaryMap();

    /**
     * Returns a nested map view of this tensor:
     * secondary → (primary → (tertiary → value)).
     */
    Map<Secondary, Map<Primary, Map<Tertiary, Value>>> secondaryMap();

    /**
     * Returns a nested map view of this tensor:
     * tertiary → (primary → (secondary → value)).
     */
    Map<Tertiary, Map<Primary, Map<Secondary, Value>>> tertiaryMap();

    // Functional utilities

    /**
     * Performs the given action for each mapping in this tensor.
     */
    default void forEach(QuadConsumer<? super Primary, ? super Secondary, ? super Tertiary, ? super Value> action) {
        for (Quadrant<Primary, Secondary, Tertiary, Value> quadrant : quadrantSet()) {
            action.accept(quadrant.getPrimaryKey(), quadrant.getSecondaryKey(), quadrant.getTertiaryKey(), quadrant.getValue());
        }
    }

    /**
     * If no mapping is present for the given keys, computes a value using the given function and inserts it.
     *
     * @return the current or computed value, or {@code null} if the function returns null
     */
    default Value considerIfAbsent(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey,
                                   @NotNull TriFunction<? super Primary, ? super Secondary, ? super Tertiary, ? extends Value> mappingFunction) {
        Value v;
        if ((v = get(primaryKey, secondaryKey, tertiaryKey)) == null) {
            Value newV = mappingFunction.apply(primaryKey, secondaryKey, tertiaryKey);
            if (newV != null) {
                put(primaryKey, secondaryKey, tertiaryKey, newV);
                return newV;
            }
        }
        return v;
    }

    /**
     * If a mapping is present for the given keys, computes a new value using the given function.
     * Replaces the old value if the new value is non-null; otherwise removes the mapping.
     *
     * @return the new value, or {@code null} if removed
     */
    default Value considerIfPresent(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey,
                                    @NotNull TriFunction<? super Primary, ? super Secondary, ? super Tertiary, ? extends Value> remappingFunction) {
        Value oldV;
        if ((oldV = get(primaryKey, secondaryKey, tertiaryKey)) != null) {
            Value newV = remappingFunction.apply(primaryKey, secondaryKey, tertiaryKey);
            if (newV != null) {
                put(primaryKey, secondaryKey, tertiaryKey, newV);
                return newV;
            } else {
                remove(primaryKey, secondaryKey, tertiaryKey);
                return null;
            }
        }
        return null;
    }

    /**
     * Computes a new value for the given keys, regardless of whether a value is currently present.
     * <p>
     * If the result is non-null, replaces the old value. If the result is null, removes the mapping.
     *
     * @return the new value, or {@code null} if removed
     */
    default Value consider(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey,
                           @NotNull QuadFunction<? super Primary, ? super Secondary, ? super Tertiary, ? super Value, ? extends Value> remappingFunction) {
        Value oldV = get(primaryKey, secondaryKey, tertiaryKey);
        Value newV = remappingFunction.apply(primaryKey, secondaryKey, tertiaryKey, oldV);
        if (newV == null) {
            if (oldV != null || containsQuadrant(primaryKey, secondaryKey, tertiaryKey)) {
                remove(primaryKey, secondaryKey, tertiaryKey);
            }
            return null;
        } else {
            put(primaryKey, secondaryKey, tertiaryKey, newV);
            return newV;
        }
    }

    /**
     * Represents a single key-value mapping (quad) in a {@link Tensor}.
     */
    interface Quadrant<Primary, Secondary, Tertiary, Value> {
        Primary getPrimaryKey();
        Secondary getSecondaryKey();
        Tertiary getTertiaryKey();
        Value getValue();

        @Override
        boolean equals(Object obj);

        @Override
        int hashCode();
    }
}
