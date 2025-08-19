package net.whmstve.thequestionlib.general.camera.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;

public interface ExtendedCamera {
    void onCustomSetup(CameraManager manager, CameraMode mode, Entity target, float partialTicks);

    void setRotation(Vec2 rotation, boolean ease);

    static CameraMode getCameraMode() {
        ToggleableProperty<CameraMode> optionCameraMode = CameraManager.getInstance().optionCameraMode;
        if (optionCameraMode.isEnabled()) {
            return optionCameraMode.getValue();
        } else if (ExtendedOptions.isFreeCam()) {
            return CameraMode.FREE.get();
        } else {
            return switch (Minecraft.getInstance().options.getCameraType()) {
                case FIRST_PERSON -> CameraMode.FIRST_PERSON.get();
                case THIRD_PERSON_FRONT -> CameraMode.THIRD_PERSON.get();
                case THIRD_PERSON_BACK -> CameraMode.THIRD_PERSON_BACK.get();
            };
        }
    }
}
