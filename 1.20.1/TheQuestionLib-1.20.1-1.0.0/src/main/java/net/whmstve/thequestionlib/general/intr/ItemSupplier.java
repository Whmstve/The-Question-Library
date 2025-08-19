package net.whmstve.thequestionlib.general.intr;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemSupplier<T extends Item> {
    T getItem();
}
