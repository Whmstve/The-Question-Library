package net.whmstve.thequestionlib.funcs;

import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

import java.util.function.BiFunction;

@FunctionalInterface
public interface EntityClientFactory<T> extends BiFunction<PlayMessages.SpawnEntity, Level,T> {}