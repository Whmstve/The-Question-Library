package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.intr.AbstractTable;
import net.whmstve.thequestionlib.general.intr.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayTable<Row,Column,Value> extends AbstractTable<Row,Column,Value> implements Table<Row,Column,Value> {
    private final List<Node<Row,Column,Value>> nodes;

    public ArrayTable(){
        nodes = new ArrayList<>();
    }

    public ArrayTable(int initialCapacity){
        nodes = new ArrayList<>(initialCapacity);
    }

    public ArrayTable(Table<? extends Row, ? extends Column, ? extends Value> table){
        nodes = new ArrayList<>(table.cellSet().stream()
        .map(cell->new Node<>((Row)cell.getRowKey(),(Column)cell.getColumnKey(),(Value)cell.getValue()))
        .toList());
    }

    @Override
    public Value put(Row rowKey, Column columnKey, Value value) {
        for (Node<Row,Column,Value> node : nodes){
            if(node.matches(rowKey,columnKey)){
                return node.setValue(value);
            }
        }
        nodes.add(new Node<>(rowKey,columnKey,value));
        return null;
    }

    @Override
    public Set<Cell<Row, Column, Value>> cellSet() {
        return new HashSet<>(nodes);
    }

    static class Node<Row,Column,Value> extends AbstractCell<Row,Column,Value>{
        private final Row rowKey;
        private final Column columnKey;
        private final Value value;

        Node(Row rowKey, Column columnKey, Value value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.value = value;
        }

        @Override
        public Row getRowKey() {
            return rowKey;
        }

        @Override
        public Column getColumnKey() {
            return columnKey;
        }

        @Override
        public Value getValue() {
            return value;
        }

        private Value setValue(Value newValue){
            return changeValue((ref)->ref.getAndSet(newValue));
        }

        @Override
        protected final boolean matches(Row rowKey, Column columnKey) {
            return super.matches(rowKey, columnKey);
        }
    }
}
