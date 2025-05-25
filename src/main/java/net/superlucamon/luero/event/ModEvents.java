package net.superlucamon.luero.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.RenderAbilitiesSyncS2Packet;
import net.superlucamon.luero.server.HeroCapabilityProvider;
import net.superlucamon.luero.util.ArmorChecker;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(HeroCapabilityProvider.HERO_CAPABILITY).ifPresent(hero -> {
                    if (hero.getCurrentHero() != null) {
                       ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet(hero.getCurrentHero().getName()), player);
                    }
                    else {
                        ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet("null"), player);
                    }
                });
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player != null) {
            ArmorChecker.checkAndSetHero(player);
        }
    }
}
