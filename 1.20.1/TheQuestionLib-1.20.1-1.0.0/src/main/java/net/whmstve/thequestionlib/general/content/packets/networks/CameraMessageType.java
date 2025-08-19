//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.whmstve.thequestionlib.general.content.packets.networks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.whmstve.thequestionlib.general.camera.common.CameraLookTarget;
import net.whmstve.thequestionlib.general.camera.common.EasingType;
import net.whmstve.thequestionlib.general.content.ServerCameraModeManager;

import java.util.function.Function;

public enum CameraMessageType {
    CLEAR((buffer) -> new CameraMessageHandle.Clear(buffer.readBoolean())),
    FADE_TIMINGS((buffer) -> new CameraMessageHandle.FadeTimings(buffer.readInt(), buffer.readInt(), buffer.readInt())),
    FADE_COLOR((buffer) -> new CameraMessageHandle.FadeColor(buffer.readInt(), buffer.readInt(), buffer.readInt())),
    MODE((buffer) -> new CameraMessageHandle.SetMode(ServerCameraModeManager.getCameraMode(buffer.readResourceLocation()))),
    DEFAULT((buffer) -> new CameraMessageHandle.Default()),
    EASING((buffer) -> new CameraMessageHandle.SetEasing(buffer.readEnum(EasingType.class), buffer.readFloat())),
    POSITION((buffer) -> new CameraMessageHandle.SetPosition(new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()))),
    ROTATION((buffer) -> new CameraMessageHandle.SetRotation(new Vec2(buffer.readFloat(), buffer.readFloat()))),
    LOOK_AT((buffer) -> new CameraMessageHandle.SetLookAt(CameraLookTarget.decode(buffer)));

    private Function<FriendlyByteBuf, CameraMessageHandle> decoder;

    private CameraMessageType(Function<FriendlyByteBuf, CameraMessageHandle> decode) {
        this.decoder = decode;
    }

    public Function<FriendlyByteBuf, CameraMessageHandle> getDecoder() {
        return this.decoder;
    }
}
