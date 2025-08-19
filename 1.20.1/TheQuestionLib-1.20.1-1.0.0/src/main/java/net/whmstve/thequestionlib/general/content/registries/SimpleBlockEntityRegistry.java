package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.whmstve.thequestionlib.general.intr.BlockSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleBlockEntityRegistry<A extends BlockEntity, B extends Block> implements Supplier<BlockEntityType<A>>, BlockSupplier<B>, ItemLike {
    private final RegistryObject<BlockEntityType<A>> blockEntity;
    private final RegistryObject<B> block;
    private final RegistryObject<BlockItem> item;

    SimpleBlockEntityRegistry(@NotNull RegistryObject<BlockEntityType<A>> blockEntity, @NotNull RegistryObject<B> block, @Nullable RegistryObject<BlockItem> item) {
        this.blockEntity = blockEntity;
        this.block = block;
        this.item = item;
    }

    @Override
    public @NotNull Item asItem()  {
        return item!=null?item.get(): Blocks.AIR.asItem();
    }

    @Override
    public B getBlock() {
        return block.get();
    }

    @Override
    public BlockEntityType<A> get() {
        return blockEntity.get();
    }
}
