package net.whmstve.thequestionlib.general.intr;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraftforge.common.util.TriPredicate;
import net.whmstve.thequestionlib.funcs.AtomicOperator;
import net.whmstve.thequestionlib.sttcs.Mathematics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class AbstractMultitable<R,C,V> implements Multitable<R,C,V>{
    private final TriPredicate<Cell<R,C,V>,R,C> matcher = (match, row, column)->match.getRowKey().equals(row) && match.getColumnKey().equals(column);

    protected AbstractMultitable(){}

    protected final boolean matchingCells(Cell<R,C,V> cell, R rowKey, C columnKey){
        return matcher.test(cell, rowKey, columnKey);
    }

    @Override
    public boolean containsCell(R rowKey, C columnKey) {
        return cellSet().stream().anyMatch(cell->matchingCells(cell,rowKey,columnKey));
    }

    @Override
    public boolean containsRow(R rowKey) {
        return cellSet().stream().anyMatch(cell->cell.getRowKey().equals(rowKey));
    }

    @Override
    public boolean containsColumn(C columnKey) {
        return cellSet().stream().anyMatch(cell->cell.getColumnKey().equals(columnKey));
    }

    @Override
    public boolean containsValue(V value) {
        return cellSet().stream().anyMatch(cell->cell.getValue().contains(value));
    }

    @Override
    public Collection<V> get(R rowKey, C columnKey) {
        return cellSet().stream().filter(cell->matchingCells(cell,rowKey,columnKey))
                .map(Cell::getValue).findFirst().orElse(null);
    }

    @Override
    public abstract boolean put(R rowKey, C columnKey, V value);

    @Override public boolean putIfAbsent(R rowKey, C columnKey, V value) {
        if(containsCell(rowKey,columnKey)){
            return false;
        }else{
            return put(rowKey,columnKey,value);
        }
    }

    @Override
    public void putAll(Multitable<? extends R, ? extends C, ? extends V> table) {
        table.forEach(this::put);
    }

    @Override
    public boolean remove(R rowKey, C columnKey) {
        for (Cell<R,C,V> cell : cellSet()){
            if(matcher.test(cell,rowKey,columnKey)){
                return cellSet().remove(cell);
            }
        }
        return false;
    }

    @Override
    public boolean remove(R rowKey, C columnKey, V value) {
        for (Cell<R,C,V> cell : cellSet()){
            if(matcher.test(cell,rowKey,columnKey)){
                return cell.getValue().remove(value);
            }
        }
        return false;
    }

    @Override
    public Multimap<C, V> row(R rowKey) {
        Multimap<C,V> result = HashMultimap.create();
        this.forEachRow(rowKey, result::put);
        return result;
    }

    @Override
    public Multimap<R, V> column(C columnKey) {
        Multimap<R,V> result = HashMultimap.create();
        this.forEachColumn(columnKey, result::put);
        return result;
    }

    @Override public abstract Set<Cell<R, C, V>> cellSet();

    @Override
    public Set<R> rowKeySet() {
        return cellSet().stream().map(Cell::getRowKey).collect(Collectors.toSet());
    }

    @Override
    public Set<C> columnKeySet() {
        return cellSet().stream().map(Cell::getColumnKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<Collection<V>> values() {
        return cellSet().stream().map(Cell::getValue).collect(Collectors.toList());
    }

    @Override
    public Map<R, Multimap<C, V>> rowMap() {
        Map<R, Multimap<C, V>> result = new HashMap<>();
        this.forEach((r, c, v) -> {
            Multimap<C, V> inner = result.computeIfAbsent(r,r1-> HashMultimap.create());
            inner.put(c,v);
            result.put(r,inner);
        });
        return result;
    }

    @Override
    public Map<C, Multimap<R, V>> columnMap() {
        Map<C, Multimap<R, V>> result = new HashMap<>();
        this.forEach((r, c, v) -> {
            Multimap<R, V> inner = result.computeIfAbsent(c,r1-> HashMultimap.create());
            inner.put(r,v);
            result.put(c,inner);
        });
        return result;
    }

    private void forEachRow(R rowKey, BiConsumer<? super C, ? super V> action){
        cellSet().stream().filter(cell->cell.getRowKey().equals(rowKey))
        .forEach(cell->cell.forEachValue(value->action.accept(cell.getColumnKey(),value)));
    }

    private void forEachColumn(C columnKey, BiConsumer<? super R, ? super V> action){
        cellSet().stream().filter(cell->cell.getColumnKey().equals(columnKey))
                .forEach(cell->cell.forEachValue(value->action.accept(cell.getRowKey(),value)));
    }

    @Override
    public int size() {
        return Mathematics.sum(cellSet().stream().map(cell->cell.getValue().size()).toList());
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public void clear() {
        cellSet().clear();
    }

    protected static abstract class AbstractCell<R,C,V> implements Cell<R,C,V> {
        protected boolean matches(R rowKey, C columnKey){
            return getRowKey().equals(rowKey) && getColumnKey().equals(columnKey);
        }
    }
}
