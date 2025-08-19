package net.whmstve.thequestionlib.general.content.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.whmstve.thequestionlib.general.content.ISyncableStorage;
import net.whmstve.thequestionlib.general.content.Networking;

import java.util.function.Function;
import java.util.function.Supplier;

public class StorageSyncPacket implements Packet<StorageSyncPacket> {
    private final BlockPos target;
    private final int amount;
    private final int capacity;

    public StorageSyncPacket(BlockPos target, int amount, int capacity) {
        this.target = target;
        this.amount = amount;
        this.capacity = capacity;
        Networking.store(this);
    }

    public StorageSyncPacket(FriendlyByteBuf buf){
        this(buf.readBlockPos(),buf.readInt(),buf.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(target);
        buf.writeInt(amount);
        buf.writeInt(capacity);
    }

    @Override
    public StorageSyncPacket decode(FriendlyByteBuf buf) {
        return new StorageSyncPacket(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level world = Minecraft.getInstance().level;
            if (world != null && world.getBlockEntity(target) instanceof ISyncableStorage syncableStorage) {
                syncableStorage.setStorageData(amount, capacity);
            }
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public Function<FriendlyByteBuf, StorageSyncPacket> factory() {
        return StorageSyncPacket::new;
    }

    @Override
    public NetworkDirection getNetworkDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
}