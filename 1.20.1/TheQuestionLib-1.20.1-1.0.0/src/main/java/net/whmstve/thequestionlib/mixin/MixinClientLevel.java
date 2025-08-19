package net.whmstve.thequestionlib.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.ExtendedOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public class MixinClientLevel {
    public MixinClientLevel() {
    }

    @Inject(method = {"disconnect"}, at = {@At("HEAD")})
    public void onDisconnect(CallbackInfo ci) {
        CameraManager.getInstance().clear();
        ExtendedOptions.disableFreecam();
    }
}