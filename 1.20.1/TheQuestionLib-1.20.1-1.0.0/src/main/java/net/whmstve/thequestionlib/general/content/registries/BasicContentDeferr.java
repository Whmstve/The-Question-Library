package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class BasicContentDeferr implements ISimpleDeferr {
    private final SimpleBlockDeferr blockDeferr;
    private final SimpleBlockEntityDeferr blockEntityDeferr;
    private final SimpleItemDeferr itemDeferr;

    private BasicContentDeferr(String modid){
        this.blockDeferr = SimpleBlockDeferr.create(modid);
        this.blockEntityDeferr = SimpleBlockEntityDeferr.create(modid);
        this.itemDeferr = SimpleItemDeferr.create(modid);
    }
    
    public static BasicContentDeferr create(@NotNull String modid){
        return new BasicContentDeferr(modid);
    }

    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> register(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, Supplier<B> blockSupplier) {
        return blockEntityDeferr.register(blockName, beSupplier, blockSupplier);
    }

    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE, Block> registerCopy(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, BlockBehaviour copy) {
        return blockEntityDeferr.registerCopy(blockName, beSupplier, copy);
    }

    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> registerSimple(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, Supplier<B> blockSupplier) {
        return blockEntityDeferr.registerSimple(blockName, beSupplier, blockSupplier);
    }

    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE, Block> registerSimpleCopy(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, BlockBehaviour copy) {
        return blockEntityDeferr.registerSimpleCopy(blockName, beSupplier, copy);
    }

    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> registerSpecial(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, Supplier<B> blockSupplier, Supplier<BlockItem> blockItemSupplier) {
        return blockEntityDeferr.registerSpecial(blockName, beSupplier, blockSupplier, blockItemSupplier);
    }

    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE, Block> registerSpecialCopy(String blockName, BlockEntityType.BlockEntitySupplier<BE> beSupplier, BlockBehaviour copy, Supplier<BlockItem> blockItemSupplier) {
        return blockEntityDeferr.registerSpecialCopy(blockName, beSupplier, copy, blockItemSupplier);
    }

    public <BLOCK extends Block> SimpleBlockRegistry<BLOCK> register(String name, Supplier<BLOCK> supplier) {
        return blockDeferr.register(name, supplier);
    }

    public <BLOCK extends Block> SimpleBlockRegistry<BLOCK> registerSimple(String name, Supplier<BLOCK> supplier) {
        return blockDeferr.registerSimple(name, supplier);
    }

    public <BLOCK extends Block, BLOCK_ITEM extends BlockItem> SimpleBlockRegistry<BLOCK> registerSpecial(String name, Supplier<BLOCK> supplier, Supplier<BLOCK_ITEM> itemSupplier) {
        return blockDeferr.registerSpecial(name, supplier, itemSupplier);
    }

    public SimpleBlockRegistry<Block> registerSimpleCopy(String name, BlockBehaviour copy) {
        return blockDeferr.registerSimpleCopy(name, copy);
    }

    public SimpleBlockRegistry<Block> registerCopy(String name, BlockBehaviour copy) {
        return blockDeferr.registerCopy(name, copy);
    }

    public <BLOCK_ITEM extends BlockItem> SimpleBlockRegistry<Block> registerSpecialCopy(String name, BlockBehaviour copy, Supplier<BLOCK_ITEM> itemSupplier) {
        return blockDeferr.registerSpecialCopy(name, copy, itemSupplier);
    }

    public <T extends Item> SimpleItemRegistry<T> registerItem(String name, Supplier<T> supplier){
        return itemDeferr.register(name,supplier);
    }

    public SimpleItemRegistry<Item> registerSimple(String name){
        return itemDeferr.registerSimple(name);
    }

    public SimpleItemRegistry<Item> registerSimple(String name, UnaryOperator<Item.Properties> operator){
        return itemDeferr.registerSimple(name,operator);
    }

    public void register(IEventBus modEventBus) {
        blockEntityDeferr.register(modEventBus);
        blockDeferr.register(modEventBus);
        itemDeferr.register(modEventBus);
    }

    public void registerWithoutItem(IEventBus modEventBus) {
        blockEntityDeferr.registerWithoutItem(modEventBus);
        blockDeferr.registerBlocks(modEventBus);
    }

    public void registerBlocks(IEventBus modEventBus) {
        blockDeferr.registerBlocks(modEventBus);
        blockEntityDeferr.registerOnlyBlock(modEventBus);
    }

    public void registerItems(IEventBus modEventBus) {
        blockDeferr.registerItems(modEventBus);
        blockEntityDeferr.registerOnlyItem(modEventBus);
        itemDeferr.register(modEventBus);
    }
}
