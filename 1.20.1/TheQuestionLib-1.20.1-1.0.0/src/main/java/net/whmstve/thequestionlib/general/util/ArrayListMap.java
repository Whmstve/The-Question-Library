package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.intr.AbstractListMap;
import net.whmstve.thequestionlib.general.intr.ListMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ArrayListMap<K,V> extends AbstractListMap<K,V> implements ListMap<K,V> {
     private final ArrayList<Node> nodes;

     public ArrayListMap(){
         nodes = new ArrayList<>();
     }

     public ArrayListMap(int initialCapacity){
         nodes = new ArrayList<>(initialCapacity);
     }

     public ArrayListMap(ListMap<? extends K, ? extends V> listMap){
         nodes = new ArrayList<>(listMap.size());
         for(ListMap.Entry<? extends K, ? extends V> entry : listMap.entrySet()){
             nodes.add(new Node(entry.getKey(),entry.getValue()));
         }
     }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(nodes);
    }

    class Node implements ListMap.Entry<K,V>{
        private final K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
}
