package net.superlucamon.luero.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.heros.ironman.otherstuff.BeamTracking;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.BeamRenderS2CPacket;

@Mod.EventBusSubscriber
public class ServerTickHandler {

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.level.isClientSide && event.level instanceof ServerLevel serverLevel) {
            BeamTracking.serverTick(serverLevel);
            for (ServerPlayer player : serverLevel.players()) {
                if (BeamTracking.isFiring(player.getUUID())) {
                    ModPackets.sendToAllTrackingAndSelf(new BeamRenderS2CPacket(
                            player.getUUID(),
                            new Vec3(player.getEyePosition().x(), player.getEyePosition().y() - 0.5f, player.getEyePosition().z()),
                            player.getLookAngle(),
                            true
                    ), player);
                }
            }
        }
    }
}
