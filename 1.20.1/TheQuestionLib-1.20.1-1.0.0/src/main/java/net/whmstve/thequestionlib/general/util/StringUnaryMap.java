package net.whmstve.thequestionlib.general.util;

import java.util.Map;

public class StringUnaryMap extends UnaryArrayMap<String>{
    public StringUnaryMap() {super();}

    public StringUnaryMap(int initialCapacity) {super(initialCapacity);}

    public StringUnaryMap(Map<? extends String, ? extends String> map) {super(map);}
}
