package net.whmstve.thequestionlib.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.network.chat.Component;
import net.whmstve.thequestionlib.general.camera.client.CameraKeyBindings;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.ExtendedOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    @Final
    public LevelRenderer levelRenderer;
    @Shadow @Final public Options options;
    @Shadow @Nullable
    public LocalPlayer player;
    @Shadow @Final public Gui gui;

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void onHandleKeyBinds(CallbackInfo ci) {
        if (CameraManager.getInstance().isEnabled() && !CameraManager.getInstance().optionCameraMode.isEnabled()) {
            OptionInstance<Boolean> freeCamOption = ((ExtendedOptions) this.options).getCameraIsFreeOption();

            while (CameraKeyBindings.keyToggleFreeCamera.consumeClick()) {
                boolean newValue = !freeCamOption.get();
                if (newValue) {
                    this.gui.setOverlayMessage(Component.translatable("freecam.enabled"), false);
                } else {
                    this.gui.setOverlayMessage(Component.translatable("freecam.disabled"), false);
                }

                freeCamOption.set(newValue);
                this.levelRenderer.needsUpdate();
            }
        }
    }

    @Inject(
        method = "handleKeybinds",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onCancelKeyTogglePerspective(CallbackInfo ci) {
        if (CameraManager.getInstance().isEnabled() &&
            (CameraManager.getInstance().optionCameraMode.isEnabled() || ExtendedOptions.isFreeCam())) {
            ci.cancel();
        }
    }
}
