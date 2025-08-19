package net.whmstve.thequestionlib.funcs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityFactory<E extends Entity> {
    E create(Level level, BlockPos pos);
}