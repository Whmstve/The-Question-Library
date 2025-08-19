package net.whmstve.thequestionlib.general.intr;

import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockSupplier<T extends Block> {
    T getBlock();
}