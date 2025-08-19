package net.whmstve.thequestionlib.general.intr;

import net.whmstve.thequestionlib.funcs.AtomicOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class AbstractTable<Row,Column,Value> implements Table<Row,Column,Value> {
    protected AbstractTable(){}

    @Override
    public boolean containsCell(Row rowKey, Column columnKey) {
        return containsColumn(columnKey) && containsRow(rowKey);
    }

    @Override
    public boolean containsRow(Row rowKey) {
        return cellSet().stream().anyMatch(cell->cell.getRowKey().equals(rowKey));
    }

    @Override
    public boolean containsColumn(Column columnKey) {
        return cellSet().stream().anyMatch(cell->cell.getColumnKey().equals(columnKey));
    }

    @Override
    public boolean containsValue(Value value) {
        return cellSet().stream().anyMatch(cell->cell.getValue().equals(value));
    }

    @Override
    public Value get(Row rowKey, Column columnKey) {
        return cellSet().stream().filter(cell->cell.getRowKey().equals(rowKey) && cell.getColumnKey().equals(columnKey))
                .map(Cell::getValue).findFirst().orElse(null);
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public int size() {
        return cellSet().size();
    }

    @Override
    public void clear() {
        cellSet().clear();
    }

    @Override public abstract Value put(Row rowKey, Column columnKey, Value value);

    @Override
    public Value putIfAbsent(Row rowKey, Column columnKey, Value value) {
        if(containsCell(rowKey,columnKey)){
            return null;
        }else{
            return put(rowKey,columnKey,value);
        }
    }

    @Override
    public void putAll(Table<? extends Row, ? extends Column, ? extends Value> table) {
        for (Cell<? extends Row, ? extends Column, ? extends Value> cell : table.cellSet()){
            put(cell.getRowKey(),cell.getColumnKey(),cell.getValue());
        }
    }

    @Override
    public Value remove(Row rowKey, Column columnKey) {
        for (Cell<Row,Column,Value> cell : cellSet()){
            if(cell.getRowKey().equals(rowKey) && cell.getColumnKey().equals(columnKey)){
                Value value = cell.getValue();
                cellSet().remove(cell);
                return value;
            }
        }
        return null;
    }

    @Override
    public Map<Column, Value> row(Row rowKey) {
        return cellSet().stream().filter(cell->cell.getRowKey().equals(rowKey))
                .collect(Collectors.toMap(Cell::getColumnKey, Cell::getValue));
    }

    @Override
    public Map<Row, Value> column(Column columnKey) {
        return cellSet().stream().filter(cell->cell.getColumnKey().equals(columnKey))
                .collect(Collectors.toMap(Cell::getRowKey, Cell::getValue));
    }

    @Override
    public Set<Row> rowKeySet() {
        return cellSet().stream().map(Cell::getRowKey).collect(Collectors.toSet());
    }

    @Override
    public Set<Column> columnKeySet() {
        return cellSet().stream().map(Cell::getColumnKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<Value> values() {
        return cellSet().stream().map(Cell::getValue).collect(Collectors.toList());
    }

    @Override
    public Map<Row, Map<Column, Value>> rowMap() {
        return cellSet().stream()
                .collect(Collectors.groupingBy(
                        Cell::getRowKey,
                        Collectors.toMap(Cell::getColumnKey, Cell::getValue)
                ));
    }

    @Override
    public Map<Column, Map<Row, Value>> columnMap() {
        return cellSet().stream()
                .collect(Collectors.groupingBy(
                        Cell::getColumnKey,
                        Collectors.toMap(Cell::getRowKey, Cell::getValue)
                ));
    }

    protected static abstract class AbstractCell<Row,Column,Value> implements Cell<Row,Column,Value>{
        protected final Value changeValue(@NotNull AtomicOperator<Value> operator){
            return operator.applyAsAtomic(new AtomicReference<>(getValue()));
        }
        protected boolean matches(Row rowKey, Column columnKey){
            return getRowKey().equals(rowKey) && getColumnKey().equals(columnKey);
        }
    }
}
