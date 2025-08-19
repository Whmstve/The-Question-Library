package net.whmstve.thequestionlib.general.intr;

import com.google.common.base.Objects;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface Multitable<Row,Column,Value>{
    /**
     * Returns {@code true} if the table containsCell a mapping with the specified row and column keys.
     *
     * @param rowKey key of row to search for
     * @param columnKey key of column to search for
     */
    boolean containsCell(Row rowKey, Column columnKey);

    /**
     * Returns {@code true} if the table containsCell a mapping with the specified row key.
     *
     * @param rowKey key of row to search for
     */
    boolean containsRow(Row rowKey);

    /**
     * Returns {@code true} if the table containsCell a mapping with the specified column.
     *
     * @param columnKey key of column to search for
     */
    boolean containsColumn(Column columnKey);

    /**
     * Returns {@code true} if the table containsCell a mapping with the specified value.
     *
     * @param value value to search for
     */
    boolean containsValue(Value value);

    /**
     * Returns the values corresponding to the given row and column and row keys if any
     * mapping exists.
     *
     * @param rowKey key of row to search for
     * @param columnKey key of column to search for
     */
    Collection<Value> get(Row rowKey, Column columnKey);

    /** Returns {@code true} if the table containsCell no mappings. */
    boolean isEmpty();

    /** Returns the number of row key / column key / value mappings in the table. */
    int size();

    /**
     * Compares the specified object with this table for equality. Two tables are equal when their
     * cell views, as returned by {@link #cellSet}, are equal.
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code for this multitable. The hash code of a table is defined as the hash code of
     * its cell view, as returned by {@link #cellSet}.
     */
    @Override
    int hashCode();

    // Mutators

    /** Removes all mappings from the table. */
    void clear();

    /**
     * Associates the specified value with the specified keys. If the table already contained a
     * mapping for those keys, the value is added, othewise a list is created with the value.
     *
     * @param rowKey row key that the value should be associated with
     * @param columnKey column key that the value should be associated with
     * @param value value to be associated with the specified keys
     * @return the result of adding the value to a pre-existing or new list
     */
    boolean put(Row rowKey,  Column columnKey,  Value value);

    /**
     * Associates the specified value with the specified keys if the table doesn't contain a
     * mapping for those keys, otherwise returning false.
     *
     * @param rowKey row key that the value should be associated with
     * @param columnKey column key that the value should be associated with
     * @param value value to be associated with the specified keys
     * @return the result of adding the value to a pre-existing or new list
     */
    boolean putIfAbsent(Row rowKey,  Column columnKey,  Value value);

    /**
     * Copies all mappings from the specified table to this table. The effect is equivalent to calling
     * {@link #put} with each row key / column key / value mapping in {@code table}.
     *
     * @param table the multitable to add to this table
     */
    void putAll(Multitable<? extends Row, ? extends Column, ? extends Value> table);

    /**
     * Removes the mapping, if any, associated with the given keys.
     *
     * @param rowKey row key of mapping to be removed
     * @param columnKey column key of mapping to be removed
     * @return the result of removing the cell from the collection of values if any
     */
    boolean remove(Row rowKey, Column columnKey);

    /**
     * Removes the value from the collection, if any, associated with the given keys.
     *
     * @param rowKey row key of mapping to be removed
     * @param columnKey column key of mapping to be removed
     * @return the result of removing the cell from the collection of values if any
     */
    boolean remove(Row rowKey, Column columnKey, Value value);

    // Views

    /**
     * Returns a view of all mappings that have the given row key. For each row key / column key /
     * value mapping in the table with that row key, the returned table associates the column key with
     * the value. If no mappings in the table have the provided row key, an empty table is returned.
     *
     * <p>Changes to the returned table will update the underlying table, and vice versa.
     *
     * @param rowKey key of row to search for in the table
     * @return the corresponding table from column keys to values
     */
    Multimap<Column, Value> row(Row rowKey);

    /**
     * Returns a view of all mappings that have the given column key. For each row key / column key /
     * value mapping in the table with that column key, the returned table associates the row key with
     * the value. If no mappings in the table have the provided column key, an empty table is returned.
     *
     * <p>Changes to the returned table will update the underlying table, and vice versa.
     *
     * @param columnKey key of column to search for in the table
     * @return the corresponding table from row keys to values
     */
    Multimap<Row, Value> column( Column columnKey);

    /**
     * Returns a set of all row key / column key / value triplets. Changes to the returned set will
     * update the underlying table, and vice versa. The cell set does not support the {@code add} or
     * {@code addAll} methods.
     *
     * @return set of table cells consisting of row key / column key / value triplets
     */
    Set<Cell<Row,Column,Value>> cellSet();

    /**
     * Returns a set of row keys that have one or more values in the table. Changes to the set will
     * update the underlying table, and vice versa.
     *
     * @return set of row keys
     */
    Set<Row> rowKeySet();

    /**
     * Returns a set of column keys that have one or more values in the table. Changes to the set will
     * update the underlying table, and vice versa.
     *
     * @return set of column keys
     */
    Set<Column> columnKeySet();

    /**
     * Returns a collection of all values, which may contain duplicates. Changes to the returned
     * collection will update the underlying table, and vice versa.
     *
     * @return collection of collections of values
     */
    Collection<Collection<Value>> values();

    /**
     * Returns a view that associates each row key with the corresponding table from column keys to
     * values. Changes to the returned table will update this table. The returned table does not support
     * {@code put()} or {@code putAll()}, or {@code setValue()} on its entries.
     *
     * <p>In contrast, the maps returned by {@code rowMap().get()} have the same behavior as those
     * returned by {@link #row}. Those maps may support {@code setValue()}, {@code put()}, and {@code
     * putAll()}.
     *
     * @return a table view from each row key to a secondary table from column keys to values
     */
    Map<Row, Multimap<Column, Value>> rowMap();

    /**
     * Returns a view that associates each column key with the corresponding table from row keys to
     * values. Changes to the returned table will update this table. The returned table does not support
     * {@code put()} or {@code putAll()}, or {@code setValue()} on its entries.
     *
     * <p>In contrast, the maps returned by {@code columnMap().get()} have the same behavior as those
     * returned by {@link #column}. Those maps may support {@code setValue()}, {@code put()}, and
     * {@code putAll()}.
     *
     * @return a table view from each column key to a secondary table from row keys to values
     */
    Map<Column, Multimap<Row, Value>> columnMap();


    default void forEach(TriConsumer<? super Row, ? super Column, ? super Value> action){
        for (Cell<Row,Column,Value> cell : cellSet()){
            cell.forEachValue(value->{
                action.accept(cell.getRowKey(),cell.getColumnKey(),value);
            });
        }
    }

    /**
     * Calculates the value from the cell corresponding to the row and column keys, inputting a new value
     * if it's not null and if there already wasn't a cell present.
     * @return the corresponding value or a non-null mapped result of the BiFunction.
     */
    default Collection<Value> calculateIfAbsent(Row rowKey, Column columnKey, @NotNull BiFunction<? super Row, ? super Column, ? extends Value> mappingBiFunction){
        Collection<Value> v; Value newV;
        if((v = get(rowKey,columnKey))==null){
            if((newV = mappingBiFunction.apply(rowKey,columnKey)) != null){
                put(rowKey,columnKey,newV);
                return new ArrayList<>(List.of(newV));
            }
        }
        return v;
    }

    /**
     * Calculates a new value from the corresponding row and column keys if present,
     * replacing the value if the new value is not null otherwise removing the cell.
     * @return the new value or null if there is an already present value or the new value is null.
     */
    default Collection<Value> calculateIfPresent(Row rowKey, Column columnKey, @NotNull BiFunction<? super Row, ? super Column, ? extends Value> remappingFunction){
        Collection<Value> oldV; Value newV;
        if((oldV = get(rowKey,columnKey))!=null){
            if((newV = remappingFunction.apply(rowKey,columnKey))!=null){
                put(rowKey,columnKey,newV);
                return new ArrayList<>(List.of(newV));
            }else{
                remove(rowKey,columnKey);
                return get(rowKey,columnKey);
            }
        }else{
            return null;
        }
    }

    /**
     * Calculates a new value from the corresponding row and column keys whether the cell is present,
     * replacing the value if the new value is not null otherwise removing the cell.
     * @return the new value or null if there is an already present value or the new value is null.
     */
    default Collection<Value> calculate(Row rowKey, Column columnKey, @NotNull TriFunction<? super Row, ? super Column, ? super Collection<Value>, ? extends Value> remappingFunction){
        Collection<Value> oldV = get(rowKey,columnKey);
        Value newV = remappingFunction.apply(rowKey,columnKey,oldV);
        if(newV == null){
            if(oldV != null || containsCell(rowKey,columnKey)){
                remove(rowKey, columnKey);
                return null;
            }else{
                return null;
            }
        }else{
            Collection<Value> prev = get(rowKey,columnKey);
            put(rowKey,columnKey,newV);
            return prev;
        }
    }

    /**
     * Row key / column key / value triplet corresponding to a mapping in a table.
     */
    interface Cell<Row,Column,Value > {
        /** Returns the row key of this cell. */
        Row getRowKey();

        /** Returns the column key of this cell. */
        Column getColumnKey();

        /** Returns the value of this cell. */
        Collection<Value> getValue();

        /**
         * Compares the specified object with this cell for equality. Two cells are equal when they have
         * equal row keys, column keys, and values.
         */
        @Override
        boolean equals( Object obj);

        /**
         * Returns the hash code of this cell.
         *
         * <p>The hash code of a table cell is equal to {@link Objects#hashCode}{@code (e.getRowKey(),
         * e.getColumnKey(), e.getValue())}.
         */
        @Override
        int hashCode();

        default void forEachValue(Consumer<? super Value> action){
            getValue().forEach(action);
        }
    }
}
