package net.whmstve.thequestionlib.general.content.registries;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContentRegistrar {
    private static final List<Normal> normals = new ArrayList<>();
    private static final List<Simple> simples = new ArrayList<>();

    public abstract static class Normal{
        protected Normal(){
            normals.add(this);
        }

        public abstract List<DeferredRegister<?>> getRegisters();
    }

    public abstract static class Simple{
        protected Simple(){
            simples.add(this);
        }

        @NotNull abstract List<ISimpleDeferr> getRegisters();
    }

    public static void registerContents(@NotNull IEventBus modEventBus){
        normals.forEach(normal -> normal.getRegisters().forEach(reg->reg.register(modEventBus)));
        simples.forEach(simple -> simple.getRegisters().forEach(reg->reg.register(modEventBus)));
    }
}
