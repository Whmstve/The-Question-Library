package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.funcs.NonNullTriFunction;
import net.whmstve.thequestionlib.general.intr.AbstractTensor;
import net.whmstve.thequestionlib.general.intr.Multitable;
import net.whmstve.thequestionlib.general.intr.Tensor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class NonNullTensor<P,S,T,V> extends AbstractTensor<P,S,T,V> implements Tensor<P,S,T,V> {
    private final Tensor<P,S,T,V> tensor;
    @Nullable
    private final V defaultValue;

    public static <P,S,T,V> NonNullTensor<P,S,T,V> create(){return new NonNullTensor<>(ArrayMultitable.create(), null);}

    public static <P,S,T,V> NonNullTensor<P,S,T,V> createWithCapacity(int pInitialCapacity){return new NonNullTensor<>(pInitialCapacity,ArrayMultitable.create(),null);}

    public static <P,S,T,V> NonNullTensor<P,S,T,V> withSize(int pSize, @NotNull V pDefaultValue){
        return new NonNullTensor<>(pSize,ArrayMultitable.create(),pDefaultValue);
    }

    public static <P,S,T,V> NonNullTensor<P,S,T,V> of(Multitable<P,S,T> pKeyTable, V pDefaultValue, NonNullTriFunction<P,S,T,V> pValueMapper){
        NonNullTensor<P,S,T,V> nonNullTensor = new NonNullTensor<>(pKeyTable,pDefaultValue);
        nonNullTensor.replaceDefaults(pValueMapper);
        return nonNullTensor;
    }

    protected NonNullTensor(Multitable<P,S,T> keyTable, @Nullable V defaultValue){
        this.defaultValue = defaultValue;
        this.tensor = new ArrayTensor<>();
        keyTable.forEach((p, s, t) -> this.tensor.put(p,s,t,defaultValue));
    }

    protected NonNullTensor(int pSize, Multitable<P,S,T> keyTable, @Nullable V defaultValue){
        this.defaultValue = defaultValue;
        this.tensor = new ArrayTensor<>(pSize);
        keyTable.forEach((p, s, t) -> this.tensor.put(p,s,t,defaultValue));
    }

    private void replaceDefaults(NonNullTriFunction<P,S,T,V> pValueMapper){
        tensor.forEach((p, s, t, v) -> tensor.put(p,s,t,pValueMapper.apply(p,s,t)));
    }

    @Override
    public V put(P primaryKey, S secondaryKey, T tertiaryKey, @NotNull V value) {
        return tensor.put(primaryKey, secondaryKey, tertiaryKey, value);
    }

    @Override
    public V putIfAbsent(P primaryKey, S secondaryKey, T tertiaryKey, @NotNull V value) {
        return tensor.putIfAbsent(primaryKey, secondaryKey, tertiaryKey, value);
    }

    @Override
    public Set<Quadrant<P, S, T, V>> quadrantSet() {
        return tensor.quadrantSet();
    }
}
