package net.whmstve.thequestionlib.funcs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.logging.Level;

@FunctionalInterface
public interface EntityTypeFactory<E extends Entity> {
    E create(EntityType<E> type, Level level, BlockPos pos);
}