package net.whmstve.thequestionlib.general.util;

import java.util.Map;

public class NumberUnaryMap extends UnaryArrayMap<Number>{
    public NumberUnaryMap() {super();}

    public NumberUnaryMap(int initialCapacity) {
        super(initialCapacity);
    }

    public NumberUnaryMap(Map<? extends Number, ? extends Number> map) {
        super(map);
    }
}
