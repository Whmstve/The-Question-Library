package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SimpleBlockEntityDeferr implements ISimpleDeferr {
    private final DeferredRegister<BlockEntityType<?>> blockEntities;
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;

    private SimpleBlockEntityDeferr(String modid){
        blockEntities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,modid);
        blocks = DeferredRegister.create(ForgeRegistries.BLOCKS,modid);
        items = DeferredRegister.create(ForgeRegistries.ITEMS,modid);
    }

    public static SimpleBlockEntityDeferr create(@NotNull String modid){
        return new SimpleBlockEntityDeferr(modid);
    }


    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> register(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            Supplier<B> blockSupplier
    ) {
        RegistryObject<B> block = blocks.register(blockName, blockSupplier);

        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.<BE>of(beSupplier, block.get()).build(null)
        );

        RegistryObject<BlockItem> item = items.register(
                blockName,
                () -> new BlockItem(block.get(), new Item.Properties())
        );

        return new SimpleBlockEntityRegistry<>(blockEntity, block, item);
    }
    
    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE,Block> registerCopy(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            BlockBehaviour copy
    ){
        RegistryObject<Block> block = blocks.register(blockName,()->new Block(BlockBehaviour.Properties.copy(copy)));

        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.<BE>of(beSupplier, block.get()).build(null)
        );

        RegistryObject<BlockItem> item = items.register(
                blockName,
                () -> new BlockItem(block.get(), new Item.Properties())
        );

        return new SimpleBlockEntityRegistry<>(blockEntity, block, item);
    }

    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> registerSimple(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            Supplier<B> blockSupplier
    ){
        RegistryObject<B> block = blocks.register(blockName, blockSupplier);
        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.of(beSupplier, block.get()).build(null)
        );
        return new SimpleBlockEntityRegistry<>(blockEntity,block,null);
    }

    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE, Block> registerSimpleCopy(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            BlockBehaviour copy
    ){
        RegistryObject<Block> block = blocks.register(blockName, ()->new Block(BlockBehaviour.Properties.copy(copy)));
        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.of(beSupplier, block.get()).build(null)
        );
        return new SimpleBlockEntityRegistry<>(blockEntity,block,null);
    }
    
    public <BE extends BlockEntity, B extends Block> SimpleBlockEntityRegistry<BE, B> registerSpecial(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            Supplier<B> blockSupplier,
            Supplier<BlockItem> blockItemSupplier
    ){
        RegistryObject<B> block = blocks.register(blockName, blockSupplier);

        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.of(beSupplier, block.get()).build(null)
        );

        RegistryObject<BlockItem> item = items.register(blockName, blockItemSupplier);

        return new SimpleBlockEntityRegistry<>(blockEntity, block, item);
    }

    public <BE extends BlockEntity> SimpleBlockEntityRegistry<BE, Block> registerSpecialCopy(
            String blockName,
            BlockEntityType.BlockEntitySupplier<BE> beSupplier,
            BlockBehaviour copy,
            Supplier<BlockItem> blockItemSupplier
    ){
        RegistryObject<Block> block = blocks.register(blockName, ()->new Block(BlockBehaviour.Properties.copy(copy)));

        RegistryObject<BlockEntityType<BE>> blockEntity = blockEntities.register(
                blockName + "_be",
                () -> BlockEntityType.Builder.of(beSupplier, block.get()).build(null)
        );

        RegistryObject<BlockItem> item = items.register(blockName, blockItemSupplier);

        return new SimpleBlockEntityRegistry<>(blockEntity, block, item);
    }

    public void register(IEventBus modEventBus){
        blockEntities.register(modEventBus);
        blocks.register(modEventBus);
        items.register(modEventBus);
    }

    public void registerWithoutItem(IEventBus modEventBus){
        blockEntities.register(modEventBus);
        blocks.register(modEventBus);
    }

    public void registerOnlyItem(IEventBus modEventBus){
        items.register(modEventBus);
    }

    public void registerOnlyBlock(IEventBus modEventBus){
        blocks.register(modEventBus);
    }
}
