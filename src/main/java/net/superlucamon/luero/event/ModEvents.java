package net.superlucamon.luero.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.RenderAbilitiesSyncS2Packet;
import net.superlucamon.luero.server.HeroCapabilityProvider;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class ModEvents {



    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(HeroCapabilityProvider.HERO_CAPABILITY).ifPresent(hero -> {
                    ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet(hero.getCurrentHero().getName()), player);
                });
            }
        }
    }
}
