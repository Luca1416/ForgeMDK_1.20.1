package net.superlucamon.luero.client;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = "heromod")
public class HeroCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<IHeroCapability> HERO_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final IHeroCapability instance = new HeroCapability();
    private final LazyOptional<IHeroCapability> lazyOptional = LazyOptional.of(() -> instance);

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == HERO_CAPABILITY ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation("heromod", "hero_capability"), new HeroCapabilityProvider());
        }
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(HERO_CAPABILITY).ifPresent(oldStore -> {
                event.getEntity().getCapability(HERO_CAPABILITY).ifPresent(newStore -> {
                    newStore.setCurrentHero(oldStore.getCurrentHero());
                });
            });
        }
    }
}
