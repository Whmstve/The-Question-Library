package net.whmstve.thequestionlib.general.util;

import java.util.Map;

public class ObjectUnaryMap extends UnaryArrayMap<Object>{
    public ObjectUnaryMap() {super();}

    public ObjectUnaryMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ObjectUnaryMap(Map<?, ?> map) {
        super(map);
    }
}
