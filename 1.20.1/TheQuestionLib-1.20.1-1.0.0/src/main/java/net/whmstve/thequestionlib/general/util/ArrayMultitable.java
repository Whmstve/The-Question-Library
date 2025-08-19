package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.intr.AbstractMultitable;
import net.whmstve.thequestionlib.general.intr.Multitable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ArrayMultitable<R,C,V> extends AbstractMultitable<R,C,V> implements Multitable<R,C,V> {
    private final List<Node<R,C,V>> nodes;

    private ArrayMultitable(){
        nodes = new ArrayList<>();
    }

    private ArrayMultitable(int initialCapacity){
        nodes = new ArrayList<>(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    private ArrayMultitable(Multitable<? extends R, ? extends C, ? extends V> multitable){
        nodes = new ArrayList<>(multitable.cellSet().stream().map(cell -> (Node<R,C,V>) cell).toList());
    }

    public static <R,C,V> ArrayMultitable<R,C,V> create(){
        return new ArrayMultitable<>();
    }

    public static <R,C,V> ArrayMultitable<R,C,V> create(int initialCapacity){
        return new ArrayMultitable<>(initialCapacity);
    }

    public static <R,C,V> ArrayMultitable<R,C,V> create(Multitable<? extends R, ? extends C, ? extends V> multitable){
        return new ArrayMultitable<>(multitable);
    }

    @Override
    public boolean put(R rowKey, C columnKey, V value) {
        for(Node<R,C,V> node : nodes){
            if(node.matches(rowKey,columnKey)){
                return node.addValue(value);
            }
        }
        nodes.add(new Node<>(rowKey,columnKey,value));
        return true;
    }

    @Override
    public Set<Cell<R, C, V>> cellSet() {
        return new HashSet<>(nodes);
    }

    static class Node<R,C,V> extends AbstractMultitable.AbstractCell<R,C,V>{
        final R rowKey;
        final C columnKey;
        final Collection<V> values;

        Node(R rowKey, C columnKey, Collection<V> values) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.values = values;
        }

        public Node(R rowKey, C columnKey, V value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.values = new ArrayList<>();
            this.values.add(value);
        }

        @Override
        public R getRowKey() {
            return rowKey;
        }

        @Override
        public C getColumnKey() {
            return columnKey;
        }

        @Override
        public Collection<V> getValue() {
            return values;
        }

        private boolean addValue(@NotNull V value){
            return values.add(value);
        }

        @Override
        protected boolean matches(R rowKey, C columnKey) {
            return super.matches(rowKey, columnKey);
        }
    }
}
