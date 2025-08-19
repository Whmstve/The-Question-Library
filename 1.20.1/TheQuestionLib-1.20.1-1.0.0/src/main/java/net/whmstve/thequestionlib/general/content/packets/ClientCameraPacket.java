package net.whmstve.thequestionlib.general.content.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.camera.common.CameraLookTarget;
import net.whmstve.thequestionlib.general.camera.common.EasingType;
import net.whmstve.thequestionlib.general.content.Networking;
import net.whmstve.thequestionlib.general.content.packets.networks.CameraMessageHandle;
import net.whmstve.thequestionlib.general.content.packets.networks.CameraMessageType;
import net.whmstve.thequestionlib.general.util.CameraMessageGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientCameraPacket implements Packet<ClientCameraPacket> {
    private final List<CameraMessageGroup> handles = new ArrayList<>();

    public ClientCameraPacket(Collection<CameraMessageGroup> groups) {
       handles.addAll(groups);
       Networking.store(this);
    }

    public ClientCameraPacket(){
        Networking.store(this);
    }

    public ClientCameraPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            CameraMessageType messageType = buf.readEnum(CameraMessageType.class);
            this.handles.add(new CameraMessageGroup(messageType, messageType.getDecoder().apply(buf)));
        }
    }

    public ClientCameraPacket with(CameraMessageGroup pair) {
        this.handles.add(pair);
        return this;
    }

    public ClientCameraPacket mode(CameraMode cameraMode) {
        return this.with(new CameraMessageGroup(CameraMessageType.MODE, new CameraMessageHandle.SetMode(cameraMode)));
    }

    public ClientCameraPacket easing(EasingType easingType, float easeTime) {
        return this.with(new CameraMessageGroup(CameraMessageType.EASING, new CameraMessageHandle.SetEasing(easingType, easeTime)));
    }

    public ClientCameraPacket position(net.minecraft.world.phys.Vec3 position) {
        return this.with(new CameraMessageGroup(CameraMessageType.POSITION, new CameraMessageHandle.SetPosition(position)));
    }

    public ClientCameraPacket rotation(net.minecraft.world.phys.Vec2 rotation) {
        return this.with(new CameraMessageGroup(CameraMessageType.ROTATION, new CameraMessageHandle.SetRotation(rotation)));
    }

    public ClientCameraPacket clear(boolean disableFreeCam) {
        return this.with(new CameraMessageGroup(CameraMessageType.CLEAR, new CameraMessageHandle.Clear(disableFreeCam)));
    }

    public ClientCameraPacket lookAt(CameraLookTarget lookTarget) {
        return this.with(new CameraMessageGroup(CameraMessageType.LOOK_AT, new CameraMessageHandle.SetLookAt(lookTarget)));
    }

    public ClientCameraPacket defaultRotPos() {
        return this.with(new CameraMessageGroup(CameraMessageType.DEFAULT, new CameraMessageHandle.Default()));
    }

    public ClientCameraPacket fadeTimings(int fadeInSeconds, int holdSeconds, int fadeOutSeconds) {
        return this.with(new CameraMessageGroup(CameraMessageType.FADE_TIMINGS, new CameraMessageHandle.FadeTimings(fadeInSeconds, holdSeconds, fadeOutSeconds)));
    }

    public ClientCameraPacket fadeColor(int r, int g, int b) {
        return this.with(new CameraMessageGroup(CameraMessageType.FADE_COLOR, new CameraMessageHandle.FadeColor(r, g, b)));
    }

    // --- Serialization ---
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(handles.size());
        for (CameraMessageGroup handle : handles) {
            buf.writeEnum(handle.type());
            handle.handle().encode(buf);
        }
    }

    @Override
    public ClientCameraPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<CameraMessageGroup> groups = new ArrayList<>(size);
        for (int i = 0; i<size; i++){
            CameraMessageType type = buf.readEnum(CameraMessageType.class);
            CameraMessageHandle handle = type.getDecoder().apply(buf);
            groups.add(new CameraMessageGroup(type,handle));
        }
        return new ClientCameraPacket(groups);
    }

    // --- Handling ---
    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<Context> ctxSupplier) {
        Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            this.handles.forEach(pair -> pair.handle().accept(ctx));
        });
        ctx.setPacketHandled(true);
    }

    @Override
    public Function<FriendlyByteBuf, ClientCameraPacket> factory() {
        return ClientCameraPacket::new;
    }

    @Override
    public NetworkDirection getNetworkDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
}