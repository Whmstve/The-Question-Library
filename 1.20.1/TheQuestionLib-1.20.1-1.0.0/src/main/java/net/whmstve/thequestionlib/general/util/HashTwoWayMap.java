package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.intr.AbstractTwoWayMap;
import net.whmstve.thequestionlib.general.intr.TwoWayMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HashTwoWayMap<Key,Value> extends AbstractTwoWayMap<Key,Value> implements TwoWayMap<Key,Value> {
    private final List<Node<Key,Value>> nodes;

    public HashTwoWayMap() {
        nodes = new ArrayList<>();
    }

    public HashTwoWayMap(int initialCapacity){
        nodes = new ArrayList<>(initialCapacity);
    }

    public HashTwoWayMap(TwoWayMap<? extends Key,? extends Value> twoWayMap){
        nodes = new ArrayList<>(twoWayMap.size());
        for(TwoWayMap.Entry<? extends Key,? extends Value> entry : twoWayMap.entrySet()){
            nodes.add(new Node<>(entry.getKey(),entry.getValue()));
        }
    }

    public static <Key,Value> HashTwoWayMap<Key,Value> fromMap(Map<? extends Key,? extends Value> map){
        HashTwoWayMap<Key,Value> result = new HashTwoWayMap<>();
        for(Map.Entry<? extends Key,? extends Value> entry : map.entrySet()){
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }

    @Override
    public boolean put(Key key, Value value) {
        for(Node<Key,Value> node : nodes){
            if(node.getKey().equals(key)){
                return node.setValue(value);
            }else if(node.getValue().equals(value)){
                return node.setKey(key);
            }
        }
        return false;
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return new HashSet<>(nodes);
    }

    static class Node<Key,Value> implements Entry<Key,Value>{
        private Key key;
        private Value value;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return value;
        }

        private boolean setKey(@NotNull Key key){
            this.key = key;
            return true;
        }

        private boolean setValue(@NotNull Value value){
            this.value = value;
            return true;
        }
    }
}
