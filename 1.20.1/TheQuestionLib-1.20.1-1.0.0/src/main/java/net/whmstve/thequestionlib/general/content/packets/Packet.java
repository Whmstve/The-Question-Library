package net.whmstve.thequestionlib.general.content.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simplified packet contract for encoding/decoding/handling.
 *
 * @param <T> The packet class itself
 */
public interface Packet<T extends Packet<T>> {
    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> context);

    Function<FriendlyByteBuf,T> factory();

    NetworkDirection getNetworkDirection();

    /**
     * Register this packet type on the given channel.
     */
    @SuppressWarnings("unchecked")
    default void registerOnNetwork(SimpleChannel channel, int id) {
        Class<T> clazz = (Class<T>) this.getClass();
        channel.messageBuilder(clazz, id, getNetworkDirection())
                .encoder((pkt, buf) -> pkt.encode(buf))
                .decoder(factory())
                .consumerMainThread((pkt, ctx) -> pkt.handle(() -> (NetworkEvent.Context) ctx))
                .add();
    }
}
