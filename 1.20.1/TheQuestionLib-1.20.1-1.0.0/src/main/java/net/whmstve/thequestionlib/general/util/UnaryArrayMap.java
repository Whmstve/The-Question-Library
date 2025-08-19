package net.whmstve.thequestionlib.general.util;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class UnaryArrayMap<E> extends AbstractMap<E,E> implements Map<E,E>, Serializable, Cloneable {
    private final ArrayList<Node> nodes;

    public UnaryArrayMap(){
        this.nodes = new ArrayList<>();
    }

    public UnaryArrayMap(int initialCapacity){
        this.nodes = new ArrayList<>(initialCapacity);
    }

    public UnaryArrayMap(Map<? extends E, ? extends E> map){
        this.nodes = new ArrayList<>(map.size());
        map.forEach((key,value)->this.nodes.add(new Node(key,value)));
    }

    @Override
    public E put(E key, E value) {
        for(Node node : nodes){
            if(node.getKey().equals(key)){
                return node.setValue(value);
            }
        }
        nodes.add(new Node(key, value));
        return null;
    }

    @NotNull
    @Override
    public Set<Entry<E, E>> entrySet() {
        return new HashSet<>(nodes);
    }

    @Override
    public Object clone() {
        try {
            return (UnaryArrayMap<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public HashMap<E,E> hash(){
        return new HashMap<>(this);
    }

    class Node implements Map.Entry<E,E>{
        private final E key;
        private E value;

        public Node(E key, E value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public E getKey() {
            return key;
        }

        @Override
        public E getValue() {
            return value;
        }

        @Override
        public E setValue(E value) {
            E prev = this.value;
            this.value = value;
            return prev;
        }
    }
}
