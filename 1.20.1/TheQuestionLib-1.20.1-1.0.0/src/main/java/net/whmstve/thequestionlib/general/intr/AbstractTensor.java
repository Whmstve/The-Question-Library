package net.whmstve.thequestionlib.general.intr;

import net.whmstve.thequestionlib.funcs.AtomicOperator;
import net.whmstve.thequestionlib.general.util.ArrayTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class AbstractTensor<Primary,Secondary,Tertiary,Value> implements Tensor<Primary,Secondary,Tertiary,Value>{
    protected AbstractTensor(){}

    @Override
    public boolean containsQuadrant(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey) {
        return containsPrimary(primaryKey) && containsSecondary(secondaryKey) && containsTertiary(tertiaryKey);
    }

    @Override
    public boolean containsPrimary(Primary primaryKey) {
        return quadrantSet().stream().anyMatch(quadrant->quadrant.getPrimaryKey().equals(primaryKey));
    }

    @Override
    public boolean containsSecondary(Secondary secondaryKey) {
        return quadrantSet().stream().anyMatch(quadrant->quadrant.getSecondaryKey().equals(secondaryKey));
    }

    @Override
    public boolean containsTertiary(Tertiary tertiaryKey) {
        return quadrantSet().stream().anyMatch(quadrant->quadrant.getTertiaryKey().equals(tertiaryKey));
    }

    @Override
    public boolean containsValue(Value value) {
        return quadrantSet().stream().anyMatch(quadrant->quadrant.getValue().equals(value));
    }

    @Override
    public Value get(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey) {
        return quadrantSet().stream().filter(quadrant->quadrant.getPrimaryKey().equals(primaryKey) && quadrant.getSecondaryKey().equals(secondaryKey) &&
                quadrant.getTertiaryKey().equals(tertiaryKey))
                .map(Quadrant::getValue).findFirst().orElse(null);
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public int size() {
        return quadrantSet().size();
    }

    @Override
    public void clear() {
        quadrantSet().clear();;
    }

    @Override public abstract Value put(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey, Value value);

    @Override
    public Value putIfAbsent(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey, Value value) {
        if(containsQuadrant(primaryKey, secondaryKey, tertiaryKey)){
            return null;
        }else{
            return put(primaryKey, secondaryKey, tertiaryKey, value);
        }
    }

    @Override
    public void putAll(Tensor<? extends Primary, ? extends Secondary, ? extends Tertiary, ? extends Value> tensor) {
        for (Quadrant<? extends Primary, ? extends Secondary, ? extends Tertiary, ? extends Value> quadrant : quadrantSet()){
            put(quadrant.getPrimaryKey(),quadrant.getSecondaryKey(),quadrant.getTertiaryKey(),quadrant.getValue());
        }
    }

    @Override
    public Value remove(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey) {
        for(Quadrant<Primary,Secondary,Tertiary,Value> quadrant : quadrantSet()){
            if(quadrant.getPrimaryKey().equals(primaryKey) && quadrant.getSecondaryKey().equals(secondaryKey) &&
                    quadrant.getTertiaryKey().equals(tertiaryKey)){
                Value result = quadrant.getValue();
                quadrantSet().remove(quadrant);
                return result;
            }
        }
        return null;
    }

    @Override
    public Table<Secondary, Tertiary, Value> primary(Primary primaryKey) {
        Table<Secondary, Tertiary, Value> result = new ArrayTable<>();
        quadrantSet().stream().filter(quadrant->quadrant.getPrimaryKey().equals(primaryKey)).forEach(quadrant->{
            result.put(quadrant.getSecondaryKey(),quadrant.getTertiaryKey(),quadrant.getValue());
        });
        return result;
    }

    @Override
    public Table<Primary, Tertiary, Value> secondary(Secondary secondaryKey) {
        Table<Primary, Tertiary, Value> result = new ArrayTable<>();
        quadrantSet().stream().filter(quadrant->quadrant.getSecondaryKey().equals(secondaryKey)).forEach(quadrant->{
            result.put(quadrant.getPrimaryKey(),quadrant.getTertiaryKey(),quadrant.getValue());
        });
        return result;
    }

    @Override
    public Table<Primary, Secondary, Value> tertiary(Tertiary tertiaryKey) {
        Table<Primary, Secondary, Value> result = new ArrayTable<>();
        quadrantSet().stream().filter(quadrant->quadrant.getTertiaryKey().equals(tertiaryKey)).forEach(quadrant->{
            result.put(quadrant.getPrimaryKey(),quadrant.getSecondaryKey(),quadrant.getValue());
        });
        return result;
    }

    @Override
    public abstract Set<Quadrant<Primary, Secondary, Tertiary, Value>> quadrantSet();

    @Override
    public Set<Primary> primaryKeySet() {
        return new HashSet<>(quadrantSet().stream().map(Quadrant::getPrimaryKey).toList());
    }

    @Override
    public Set<Secondary> secondaryKeySet() {
        return new HashSet<>(quadrantSet().stream().map(Quadrant::getSecondaryKey).toList());
    }

    @Override
    public Set<Tertiary> tertiaryKeySet() {
        return new HashSet<>(quadrantSet().stream().map(Quadrant::getTertiaryKey).toList());
    }

    @Override
    public Collection<Value> values() {
        return new ArrayList<>(quadrantSet().stream().map(Quadrant::getValue).toList());
    }

    @Override
    public Map<Primary, Map<Secondary, Map<Tertiary, Value>>> primaryMap() {
        return quadrantSet().stream()
                .collect(Collectors.groupingBy(
                        Quadrant::getPrimaryKey,
                        Collectors.groupingBy(
                                Quadrant::getSecondaryKey,
                                Collectors.toMap(Quadrant::getTertiaryKey,Quadrant::getValue)
                        )
                ));
    }

    @Override
    public Map<Secondary, Map<Primary, Map<Tertiary, Value>>> secondaryMap() {
        return quadrantSet().stream()
                .collect(Collectors.groupingBy(
                        Quadrant::getSecondaryKey,
                        Collectors.groupingBy(
                                Quadrant::getPrimaryKey,
                                Collectors.toMap(Quadrant::getTertiaryKey,Quadrant::getValue)
                        )
                ));
    }

    @Override
    public Map<Tertiary, Map<Primary, Map<Secondary, Value>>> tertiaryMap() {
        return quadrantSet().stream()
                .collect(Collectors.groupingBy(
                        Quadrant::getTertiaryKey,
                        Collectors.groupingBy(
                                Quadrant::getPrimaryKey,
                                Collectors.toMap(Quadrant::getSecondaryKey,Quadrant::getValue)
                        )
                ));
    }

    protected abstract static class AbstractQuadrant<Primary,Secondary,Tertiary,Value> implements Quadrant<Primary,Secondary,Tertiary,Value>{
        protected boolean matches(Primary primaryKey, Secondary secondaryKey, Tertiary tertiaryKey){
            return getPrimaryKey().equals(primaryKey) && getSecondaryKey().equals(secondaryKey) && getTertiaryKey().equals(tertiaryKey);
        }

        protected final Value changeValue(@NotNull AtomicOperator<Value> operator){
            return operator.applyAsAtomic(new AtomicReference<>(getValue()));
        }
    }
}
