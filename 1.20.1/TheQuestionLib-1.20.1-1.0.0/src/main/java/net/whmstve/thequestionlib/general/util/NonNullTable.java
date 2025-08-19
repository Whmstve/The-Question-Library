package net.whmstve.thequestionlib.general.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.whmstve.thequestionlib.funcs.NonNullBiFunction;
import net.whmstve.thequestionlib.general.intr.AbstractTable;
import net.whmstve.thequestionlib.general.intr.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class NonNullTable<R,C,V> extends AbstractTable<R,C,V> implements Table<R,C,V> {
    private final Table<R,C,V> table;
    @Nullable
    private final V defaultValue;

    public static <R,C,V> NonNullTable<R,C,V> create(){return new NonNullTable<>(HashMultimap.create(), null);}

    public static <R,C,V> NonNullTable<R,C,V> createWithCapacity(int pInitialCapacity){return new NonNullTable<>(pInitialCapacity,HashMultimap.create(),null);}

    public static <R,C,V> NonNullTable<R,C,V> withSize(int pSize, @NotNull V pDefaultValue){
        return new NonNullTable<>(pSize,HashMultimap.create(),pDefaultValue);
    }

    public static <R,C,V> NonNullTable<R,C,V> of(Multimap<R,C> pRowColumnKeys, V pDefaultValue, NonNullBiFunction<R,C,V> pValueMapper){
        NonNullTable<R,C,V> nonNullTable = new NonNullTable<>(pRowColumnKeys,pDefaultValue);
        nonNullTable.replaceDefaults(pValueMapper);
        return nonNullTable;
    }

    protected NonNullTable(Multimap<R,C> rowColumns, @Nullable V defaultValue){
        this.defaultValue = defaultValue;
        this.table = new ArrayTable<>();
        rowColumns.forEach((row,column)->this.table.put(row,column,defaultValue));
    }

    protected NonNullTable(int pSize, Multimap<R,C> rowColumns, @Nullable V defaultValue){
        this.defaultValue = defaultValue;
        this.table = new ArrayTable<>(pSize);
        rowColumns.forEach((row,column)->this.table.put(row,column,defaultValue));
    }

    private void replaceDefaults(NonNullBiFunction<R,C,V> pValueMapper){
        table.forEach((r, c, v) -> table.put(r,c,pValueMapper.apply(r,c)));
    }

    @Override
    public V put(R rowKey, C columnKey, @NotNull V value) {
        return table.put(rowKey,columnKey,value);
    }

    @Override
    public V putIfAbsent(R rowKey, C columnKey, @NotNull V value) {
        return table.putIfAbsent(rowKey, columnKey, value);
    }

    @Override
    public Set<Cell<R, C, V>> cellSet() {
        return table.cellSet();
    }
}
