package net.whmstve.thequestionlib.general.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.whmstve.thequestionlib.TheQuestionLibMod;
import net.whmstve.thequestionlib.general.content.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class Networking {
    private static final String PROTOCOL = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(TheQuestionLibMod.MOD_ID,"main"), ()->PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    private static int packetId = 0;

    private static final List<Packet<?>> packets = new ArrayList<>();

    public static <T extends Packet<T>> void store(T packet){
        packets.add(packet);
    }

    public static void registerAll(){
        packets.forEach(packet -> packet.registerOnNetwork(CHANNEL,nextId()));
    }

    private static int nextId() {
        return packetId++;
    }

    /** Send a packet to the server */
    public static void sendToServer(Packet<?> packet) {
        CHANNEL.sendToServer(packet);
    }

    /** Send a packet to a specific player */
    public static void sendToPlayer(Packet<?> packet, ServerPlayer player) {
        CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    /** Broadcast a packet to all players on the server */
    public static void sendToAll(Packet<?> packet, Iterable<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            sendToPlayer(packet, player);
        }
    }
}
