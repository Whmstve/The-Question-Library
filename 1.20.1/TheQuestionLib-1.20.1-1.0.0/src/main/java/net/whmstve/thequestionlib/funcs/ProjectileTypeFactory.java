package net.whmstve.thequestionlib.funcs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ProjectileTypeFactory<E extends Projectile> {
    E create(EntityType<E> type, Level level, BlockPos pos);
}
