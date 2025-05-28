package net.superlucamon.luero.server;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;
import net.superlucamon.luero.heros.ironman.otherstuff.BeamTracking;

@Mod.EventBusSubscriber
public class ServerTickHandler {

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide && event.level instanceof ServerLevel serverLevel) {
            BeamTracking.serverTick(serverLevel);
        }
    }
}
