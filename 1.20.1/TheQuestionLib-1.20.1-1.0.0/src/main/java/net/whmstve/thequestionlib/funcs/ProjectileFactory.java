package net.whmstve.thequestionlib.funcs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ProjectileFactory<E extends Projectile> {
    E create(Level level, BlockPos pos);
}
