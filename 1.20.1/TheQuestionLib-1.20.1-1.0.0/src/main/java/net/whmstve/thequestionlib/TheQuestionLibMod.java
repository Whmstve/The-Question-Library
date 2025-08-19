package net.whmstve.thequestionlib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.whmstve.thequestionlib.events.EventReader;
import net.whmstve.thequestionlib.general.camera.client.CameraFade;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.content.Networking;
import net.whmstve.thequestionlib.general.content.registries.ContentRegistrar;

@Mod(TheQuestionLibMod.MOD_ID)
public class TheQuestionLibMod {
    public static final String MOD_ID = "thequestionlib";
    private static CameraManager cameraManager;
    private static CameraFade cameraFade;

    public TheQuestionLibMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventReader.register(MinecraftForge.EVENT_BUS);
        Networking.registerAll();
        ContentRegistrar.registerContents(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()-> ()->{
            cameraManager = new CameraManager();
            cameraFade = new CameraFade();
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static CameraManager getCameraManager() {
        return cameraManager;
    }

    @OnlyIn(Dist.CLIENT)
    public static CameraFade getCameraFade() {
        return cameraFade;
    }
}
