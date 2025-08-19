package net.whmstve.thequestionlib.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import net.whmstve.thequestionlib.general.camera.client.ExtendedOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class MixinVideoSettingsScreen extends OptionsSubScreen {
    @Shadow
    private OptionsList list;

    public MixinVideoSettingsScreen(Screen parent, Options options, Component title) {
        super(parent, options, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        this.list.addSmall(new OptionInstance[]{((ExtendedOptions) this.options).getCameraCommandOption()});

    }
}