package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.intr.AbstractTensor;
import net.whmstve.thequestionlib.general.intr.Tensor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayTensor<P,S,T,V> extends AbstractTensor<P,S,T,V> implements Tensor<P,S,T,V> {
    private final List<Node<P,S,T,V>> nodes;

    public ArrayTensor(){
        nodes = new ArrayList<>();
    }

    public ArrayTensor(int initialCapacity){
        nodes = new ArrayList<>(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    public ArrayTensor(Tensor<? extends P, ? extends S, ? extends T, ? extends V> tensor){
        nodes = new ArrayList<>(tensor.quadrantSet().stream().map(quad->(Node<P,S,T,V>)quad).toList());
    }

    @Override
    public V put(P primaryKey, S secondaryKey, T tertiaryKey, V value) {
        for (Node<P,S,T,V> node : nodes){
            if(node.matches(primaryKey,secondaryKey,tertiaryKey)){
                return node.setValue(value);
            }
        }
        nodes.add(new Node<>(primaryKey,secondaryKey,tertiaryKey,value));
        return null;
    }

    @Override
    public Set<Quadrant<P, S, T, V>> quadrantSet() {
        return new HashSet<>(nodes);
    }

    static class Node<Primary,Secondary,Tertiary,Value> extends AbstractQuadrant<Primary,Secondary,Tertiary,Value>{
        final Primary primaryKey;
        final Secondary secondaryKey;
        final Tertiary tertiaryKey;
        final Value value;

        Node(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey, Value value) {
            this.primaryKey = primaryKey;
            this.secondaryKey = secondaryKey;
            this.tertiaryKey = tertiaryKey;
            this.value = value;
        }

        @Override
        public Primary getPrimaryKey() {
            return primaryKey;
        }

        @Override
        public Secondary getSecondaryKey() {
            return secondaryKey;
        }

        @Override
        public Tertiary getTertiaryKey() {
            return tertiaryKey;
        }

        @Override
        public Value getValue() {
            return value;
        }

        private Value setValue(@NotNull Value other){
            return changeValue((ref)->ref.getAndSet(other));
        }

        @Override
        protected final boolean matches(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey) {
            return super.matches(primaryKey, secondaryKey, tertiaryKey);
        }
    }
}
