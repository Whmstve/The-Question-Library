package net.whmstve.thequestionlib.general.camera.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
    modid = "cameracommand",
    bus = Bus.MOD, value = {Dist.CLIENT}
)
public class CameraKeyBindings {
    public static final KeyMapping keyToggleFreeCamera = new KeyMapping("key.toggleFreeCamera", 66, "key.categories.misc");

    public CameraKeyBindings() {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(keyToggleFreeCamera);
    }
}