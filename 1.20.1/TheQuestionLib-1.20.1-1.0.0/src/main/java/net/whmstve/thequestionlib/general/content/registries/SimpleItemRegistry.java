package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SimpleItemRegistry<T extends Item> implements Supplier<T> {
    private final RegistryObject<T> item;

    SimpleItemRegistry(@NotNull RegistryObject<T> item){
        this.item = item;
    }

    @Override
    public T get() {
        return item.get();
    }
}
