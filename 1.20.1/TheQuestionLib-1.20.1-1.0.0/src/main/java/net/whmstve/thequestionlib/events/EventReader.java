package net.whmstve.thequestionlib.events;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.whmstve.thequestionlib.general.content.Networking;
import net.whmstve.thequestionlib.general.content.ServerCameraModeManager;
import net.whmstve.thequestionlib.general.content.packets.ClientCameraPacket;
import net.whmstve.thequestionlib.general.intr.ILaserable;
import net.whmstve.thequestionlib.general.util.Transform;
import net.whmstve.thequestionlib.memory.GlobalRequests;
import net.whmstve.thequestionlib.rendering.Block2BlockLine;

@Mod.EventBusSubscriber
public class EventReader {
    public static void register(IEventBus eventBus){
        eventBus.register(new ModBusEvents());
        eventBus.register(new ForgeBusEvents());
    }

    /** ========== MOD BUS EVENTS ========== */
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        public static void onRenderWorldLast(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
                return;

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null)
                return;

            Level world = player.level();

            if (!GlobalRequests.isEmpty(ColoredLineDrawRequestEvent.WithPlayerAndLevel.class)) {
                GlobalRequests.forEach(ColoredLineDrawRequestEvent.WithPlayerAndLevel.class, (ILaserable laser) -> {
                    laser.targetPositions().forEach(targetPos -> {
                        ColoredLineDrawRequestEvent.WithPlayerAndLevel drawEvent = ColoredLineDrawRequestEvent
                                .withPlayerAndLevel(world, player, laser.mainPosition(), targetPos,
                                        laser.getLaserColor());
                        onDrawRequest(event, drawEvent);
                    });
                });
            }
        }

        private static void onDrawRequest(RenderLevelStageEvent renderEvent,
                                          ColoredLineDrawRequestEvent.WithPlayerAndLevel drawEvent) {
            Block2BlockLine.render(renderEvent,
                    drawEvent.getColor().getRed(),
                    drawEvent.getColor().getGreen(),
                    drawEvent.getColor().getBlue(),
                    1.5f,
                    drawEvent.getEntity(),
                    Transform.fromPosition(drawEvent.getStartingPosition()),
                    Transform.fromPosition(drawEvent.getEndingPosition()));
        }
    }

    /** ========== FORGE BUS EVENTS ========== */
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeBusEvents {
        public static void onAddReloadListener(AddReloadListenerEvent event) {
            event.addListener(new ServerCameraModeManager());
        }

        public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                Networking.sendToPlayer((new ClientCameraPacket()).clear(true), serverPlayer);
            }
        }

        public static void onPlayerDie(LivingDeathEvent event) {
            LivingEntity player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                Networking.sendToPlayer((new ClientCameraPacket()).clear(true), serverPlayer);
            }
        }
    }
}
