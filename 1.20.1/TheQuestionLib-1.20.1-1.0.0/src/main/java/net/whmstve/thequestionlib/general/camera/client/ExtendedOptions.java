package net.whmstve.thequestionlib.general.camera.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.whmstve.thequestionlib.general.content.ServerCameraModeManager;

public interface ExtendedOptions {
    OptionInstance<Boolean> getCameraCommandOption();
    OptionInstance<Boolean> getCameraIsFreeOption();

    static boolean isFreeCam(){
        return ((ExtendedOptions) Minecraft.getInstance().options).getCameraIsFreeOption().get();
    }

    static boolean isCameraCommandEnabled() {
        return ((ExtendedOptions) Minecraft.getInstance().options)
                .getCameraCommandOption().get()
                && ServerCameraModeManager.isLoaded();
    }

    static void disableFreecam() {
        ((ExtendedOptions) Minecraft.getInstance().options)
                .getCameraIsFreeOption().set(false);
    }
}
