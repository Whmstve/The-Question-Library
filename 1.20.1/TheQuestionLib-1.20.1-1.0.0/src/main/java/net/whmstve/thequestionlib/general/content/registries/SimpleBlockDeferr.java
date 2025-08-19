package net.whmstve.thequestionlib.general.content.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SimpleBlockDeferr implements ISimpleDeferr {
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;

    private SimpleBlockDeferr(String modid){
        blocks = DeferredRegister.create(ForgeRegistries.BLOCKS,modid);
        items = DeferredRegister.create(ForgeRegistries.ITEMS,modid);
    }

    public static SimpleBlockDeferr create(@NotNull String modid){
        return new SimpleBlockDeferr(modid);
    }

    public <BLOCK extends Block> SimpleBlockRegistry<BLOCK> register(String name, Supplier<BLOCK> supplier){
        RegistryObject<BLOCK> block = blocks.register(name,supplier);
        return new SimpleBlockRegistry<>(
                block,items.register(name,()->new BlockItem(block.get(),new Item.Properties()))
        );
    }

    public <BLOCK extends Block> SimpleBlockRegistry<BLOCK> registerSimple(String name, Supplier<BLOCK> supplier){
        return new SimpleBlockRegistry<>(
                blocks.register(name,supplier), null
        );
    }

    public <BLOCK extends Block, BLOCK_ITEM extends BlockItem> SimpleBlockRegistry<BLOCK> registerSpecial(String name, Supplier<BLOCK> supplier, Supplier<BLOCK_ITEM> itemSupplier){
        return new SimpleBlockRegistry<>(
                blocks.register(name,supplier),
                items.register(name,itemSupplier)
        );
    }

    public SimpleBlockRegistry<Block> registerSimpleCopy(String name, BlockBehaviour copy){
        return new SimpleBlockRegistry<>(
                blocks.register(name,()->new Block(BlockBehaviour.Properties.copy(copy))), null
        );
    }

    public SimpleBlockRegistry<Block> registerCopy(String name, BlockBehaviour copy){
        RegistryObject<Block> block = blocks.register(name,()->new Block(BlockBehaviour.Properties.copy(copy)));
        return new SimpleBlockRegistry<>(
                block, items.register(name,()->new BlockItem(block.get(),new Item.Properties()))
        );
    }

    public <BLOCK_ITEM extends BlockItem> SimpleBlockRegistry<Block> registerSpecialCopy(String name, BlockBehaviour copy, Supplier<BLOCK_ITEM> itemSupplier){
        return new SimpleBlockRegistry<>(
                blocks.register(name,()->new Block(BlockBehaviour.Properties.copy(copy))),
                items.register(name,itemSupplier)
        );
    }

    public void register(IEventBus modEventBus){
        blocks.register(modEventBus);
        items.register(modEventBus);
    }

    public void registerBlocks(IEventBus modEventBus){
        blocks.register(modEventBus);
    }

    public void registerItems(IEventBus modEventBus){
        items.register(modEventBus);
    }
}
