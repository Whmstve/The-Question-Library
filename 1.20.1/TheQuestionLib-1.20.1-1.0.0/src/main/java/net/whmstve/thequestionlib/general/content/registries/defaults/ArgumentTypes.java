package net.whmstve.thequestionlib.general.content.registries.defaults;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.whmstve.thequestionlib.TheQuestionLibMod;
import net.whmstve.thequestionlib.general.camera.registry.CameraModeArgument;
import net.whmstve.thequestionlib.general.camera.registry.EasingTypeArgument;
import net.whmstve.thequestionlib.general.content.registries.ContentRegistrar;

import java.util.List;

public class ArgumentTypes extends ContentRegistrar.Normal {
    private static final DeferredRegister<ArgumentTypeInfo<?,?>> COMMAND_ARGUMENT_TYPES;
    public static final RegistryObject<SingletonArgumentInfo<CameraModeArgument>> CAMERA_MODE_ARGUMENT;
    public static final RegistryObject<SingletonArgumentInfo<EasingTypeArgument>> EASING_TYPE_ARGUMENT;

    @Override
    public List<DeferredRegister<?>> getRegisters() {
        return List.of(COMMAND_ARGUMENT_TYPES);
    }

    static{
        COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, TheQuestionLibMod.MOD_ID);
        CAMERA_MODE_ARGUMENT = COMMAND_ARGUMENT_TYPES.register("camera_mode", ()-> ArgumentTypeInfos.registerByClass(CameraModeArgument.class,SingletonArgumentInfo.contextFree(CameraModeArgument::mode)));
        EASING_TYPE_ARGUMENT = COMMAND_ARGUMENT_TYPES.register("easing_type", ()-> ArgumentTypeInfos.registerByClass(EasingTypeArgument.class,SingletonArgumentInfo.contextFree(EasingTypeArgument::easing)));

    }
}
