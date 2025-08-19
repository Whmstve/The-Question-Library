package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleBlockRegistry<T extends Block> implements Supplier<T>, ItemLike {
    private final RegistryObject<T> block;
    private final RegistryObject<BlockItem> item;

    SimpleBlockRegistry(@NotNull RegistryObject<T> registryObject, @Nullable RegistryObject<BlockItem> blockItemRegistryObject){
        this.block = registryObject;
        this.item = blockItemRegistryObject;
    }

    @Override
    public T get() {
        return block.get();
    }

    @Override
    public @NotNull Item asItem() {
        return item!=null?item.get():Blocks.AIR.asItem();
    }
}
