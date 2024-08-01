package net.superlucamon.luero.test;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.server.IHeroCapability;
import net.superlucamon.luero.entity.types.nonhostile.SentryEntity;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CustomEntityRegister.CUSTOM_SMALL_FIREBALL.get(), CustomFlyingRocket.createMissileAttributes().build());
        event.put(CustomEntityRegister.CUSTOM_LINE.get(), BeamEntity.createSentryAttributes().build());
        event.put(CustomEntityRegister.SENTRY_ENTITY.get(), SentryEntity.createSentryAttributes().build());
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IHeroCapability.class);
    }
}
