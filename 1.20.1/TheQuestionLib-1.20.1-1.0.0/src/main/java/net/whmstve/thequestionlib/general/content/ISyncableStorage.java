package net.whmstve.thequestionlib.general.content;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.whmstve.thequestionlib.general.content.annotations.OnlyImplementedBy;

@OnlyImplementedBy(clazz = BlockEntity.class)
public interface ISyncableStorage {
    void setStorageData(int amount, int capacity);
}
