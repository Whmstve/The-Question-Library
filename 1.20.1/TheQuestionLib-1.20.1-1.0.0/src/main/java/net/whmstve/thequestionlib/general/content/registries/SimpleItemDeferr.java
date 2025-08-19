package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SimpleItemDeferr implements ISimpleDeferr {
    private final DeferredRegister<Item> items;

    private SimpleItemDeferr(String modid){
        this.items = DeferredRegister.create(ForgeRegistries.ITEMS,modid);
    }

    public static SimpleItemDeferr create(@NotNull String modid){
        return new SimpleItemDeferr(modid);
    }

    public <T extends Item> SimpleItemRegistry<T> register(String name, Supplier<T> supplier){
        return new SimpleItemRegistry<>(items.register(name,supplier));
    }

    public SimpleItemRegistry<Item> registerSimple(String name){
        return new SimpleItemRegistry<>(items.register(name,()->new Item(new Item.Properties())));
    }

    public SimpleItemRegistry<Item> registerSimple(String name, UnaryOperator<Item.Properties> operator){
        return new SimpleItemRegistry<>(items.register(name,()->new Item(operator.apply(new Item.Properties()))));
    }

    public void register(IEventBus modEventBus){
        items.register(modEventBus);
    }
}
