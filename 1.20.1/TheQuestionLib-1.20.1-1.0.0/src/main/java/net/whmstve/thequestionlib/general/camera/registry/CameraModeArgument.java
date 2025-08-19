package net.whmstve.thequestionlib.general.camera.registry;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.content.ServerCameraModeManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CameraModeArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("first_person", "minecraft:first_person");
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(obj->{
        return Component.translatable("argument.camera_mode.invalid", obj);
    });

    @Override
    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(ServerCameraModeManager.getCameraModes().stream().map(c->c.getLocation().toString()).toArray(String[]::new), builder) : Suggestions.empty();
    }

    public static CameraModeArgument mode(){
        return new CameraModeArgument();
    }

    public static CameraMode getMode(CommandContext<?> context, String name) throws CommandSyntaxException{
        ResourceLocation location = context.getArgument(name, ResourceLocation.class);
        CameraMode cameraMode = ServerCameraModeManager.getCameraMode(location);
        if (cameraMode == null) {
            throw ERROR_INVALID_VALUE.create(location);
        } else {
            return cameraMode;
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
