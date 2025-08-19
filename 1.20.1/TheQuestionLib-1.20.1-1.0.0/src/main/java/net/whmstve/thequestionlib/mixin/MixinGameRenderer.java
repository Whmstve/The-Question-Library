package net.whmstve.thequestionlib.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.whmstve.thequestionlib.general.camera.client.CameraFade;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.ExtendedCamera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class MixinGameRenderer {
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    public MixinGameRenderer() {
    }

    @Inject(
        method = {"bobView"},
        at = {@At("HEAD")},
        cancellable = true
    )
    public void onBobView(PoseStack poseStack, float partialTick, CallbackInfo ci) {
        if (CameraManager.getInstance().isEnabled() && !ExtendedCamera.getCameraMode().isBobView()) {
            ci.cancel();
        }

    }

    @Inject(
        method = {"render"},
        at = {@At(
    value = "INVOKE",
    target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V",
    shift = At.Shift.AFTER)}
    )
    public void onGuiRender(float partialTicks, long nanoTime, boolean renderWorldIn, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        GuiGraphics guigraphics = new GuiGraphics(minecraft, this.renderBuffers.bufferSource());
        CameraFade.getInstance().render(guigraphics, partialTicks);
    }

    @Inject(
        method = {"tick"},
        at = {@At("HEAD")}
    )
    public void onGuiTick(CallbackInfo ci) {
        CameraFade.getInstance().tick();
    }
}