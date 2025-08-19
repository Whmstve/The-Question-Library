package net.whmstve.thequestionlib.mixin;

import net.minecraft.client.CameraType;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.camera.client.ExtendedOptions;
import net.whmstve.thequestionlib.general.camera.client.ToggleableProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Options.class})
public class MixinOptions implements ExtendedOptions {
    private final OptionInstance<Boolean> cameraCommand = OptionInstance.createBoolean("options.cameraCommand", true);
    private final OptionInstance<Boolean> cameraIsFree = OptionInstance.createBoolean("options.cameraFree", false);

    public MixinOptions() {
    }

    @Inject(
        method = {"getCameraType"},
        at = {@At("HEAD")},
        cancellable = true
    )
    public void onGetCameraMode(CallbackInfoReturnable<CameraType> cir) {
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isEnabled()) {
            ToggleableProperty<CameraMode> cameraModeProperty = cameraManager.optionCameraMode;
            if (cameraModeProperty.isEnabled()) {
                CameraMode cameraMode = cameraModeProperty.getValue();
                switch (cameraMode.getParent()) {
                    case FIRST_PERSON -> cir.setReturnValue(CameraType.FIRST_PERSON);
                    case THIRD_PERSON -> cir.setReturnValue(CameraType.THIRD_PERSON_FRONT);
                    case THIRD_PERSON_BACK, FREE -> cir.setReturnValue(CameraType.THIRD_PERSON_BACK);
                }
            } else if (ExtendedOptions.isFreeCam()) {
                cir.setReturnValue(CameraType.THIRD_PERSON_BACK);
            }
        }

    }

    @Inject(
        method = {"processOptions"},
        at = {@At("HEAD")}
    )
    public void onProcessOptions(Options.FieldAccess fieldAccess, CallbackInfo ci) {
        fieldAccess.process("cameraCommand", this.cameraCommand);
    }

    public OptionInstance<Boolean> getCameraCommandOption() {
        return this.cameraCommand;
    }

    public OptionInstance<Boolean> getCameraIsFreeOption() {
        return this.cameraIsFree;
    }
}